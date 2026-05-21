package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamUser;
import kz.iitu.Library.domain.entity.OteuIslamUserFile;
import kz.iitu.Library.exception.OteuIslamBadRequestException;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamUserFileRepository;
import kz.iitu.Library.repository.OteuIslamUserRepository;
import kz.iitu.Library.web.dto.response.OteuIslamUserFileResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OteuIslamProfileService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamProfileService.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private final OteuIslamUserFileRepository userFileRepository;
    private final OteuIslamUserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ---- AVATAR ----

    public OteuIslamUserFileResponse uploadAvatar(String email, MultipartFile file) throws IOException {
        validateNotEmpty(file, "Avatar file is required");
        validateFileSize(file);
        validateAvatarType(file.getContentType());

        OteuIslamUser user = getUserByEmail(email);

        // Удаляем старую аватарку
        List<OteuIslamUserFile> oldAvatars =
                userFileRepository.findByUserIdAndFileType(user.getId(), "AVATAR");
        oldAvatars.forEach(this::deleteFileFromDisk);
        userFileRepository.deleteAll(oldAvatars);

        OteuIslamUserFile saved = saveFile(user, file, "AVATAR");
        log.info("Avatar uploaded for user: {}", email);
        return toResponse(saved);
    }

    public Resource downloadAvatar(String email) throws IOException {
        OteuIslamUser user = getUserByEmail(email);
        OteuIslamUserFile avatar = userFileRepository
                .findFirstByUserIdAndFileType(user.getId(), "AVATAR")
                .orElseThrow(() -> new OteuIslamNotFoundException("Avatar not found"));
        return loadResource(avatar);
    }

    public String getAvatarContentType(String email) {
        OteuIslamUser user = getUserByEmail(email);
        return userFileRepository
                .findFirstByUserIdAndFileType(user.getId(), "AVATAR")
                .map(OteuIslamUserFile::getContentType)
                .orElse("image/jpeg");
    }

    public boolean hasAvatar(String email) {
        OteuIslamUser user = getUserByEmail(email);
        return !userFileRepository
                .findByUserIdAndFileType(user.getId(), "AVATAR").isEmpty();
    }

    // ---- DOCUMENTS ----

    public List<OteuIslamUserFileResponse> uploadDocuments(String email,
                                                           List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new OteuIslamBadRequestException("At least one document is required");
        }

        OteuIslamUser user = getUserByEmail(email);
        List<OteuIslamUserFile> saved = new java.util.ArrayList<>();

        for (MultipartFile file : files) {
            validateNotEmpty(file, "Document file is required");
            validateFileSize(file);
            validateDocumentType(file.getContentType());
            saved.add(saveFile(user, file, "DOCUMENT"));
        }

        log.info("Uploaded {} documents for user: {}", saved.size(), email);
        return saved.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OteuIslamUserFileResponse> getMyDocuments(String email) {
        OteuIslamUser user = getUserByEmail(email);
        return userFileRepository.findByUserIdAndFileType(user.getId(), "DOCUMENT")
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Resource downloadDocument(Long fileId) throws IOException {
        OteuIslamUserFile file = userFileRepository.findById(fileId)
                .orElseThrow(() -> new OteuIslamNotFoundException("File not found"));
        return loadResource(file);
    }

    public String getDocumentFileName(Long fileId) {
        return userFileRepository.findById(fileId)
                .orElseThrow(() -> new OteuIslamNotFoundException("File not found"))
                .getFileName();
    }

    public String getDocumentContentType(Long fileId) {
        return userFileRepository.findById(fileId)
                .orElseThrow(() -> new OteuIslamNotFoundException("File not found"))
                .getContentType();
    }

    public void deleteDocument(String email, Long fileId) {
        OteuIslamUserFile file = userFileRepository.findById(fileId)
                .orElseThrow(() -> new OteuIslamNotFoundException("File not found"));

        if (!file.getUser().getEmail().equals(email)) {
            throw new OteuIslamBadRequestException("This file does not belong to you");
        }

        deleteFileFromDisk(file);
        userFileRepository.delete(file);
        log.info("Document {} deleted by user: {}", fileId, email);
    }

    // ---- INTERNAL ----

    private OteuIslamUserFile saveFile(OteuIslamUser user, MultipartFile file,
                                       String fileType) throws IOException {
        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0) extension = originalName.substring(dotIndex);

        String uniqueName = UUID.randomUUID() + extension;

        Path userDir = Paths.get(uploadDir, String.valueOf(user.getId())).toAbsolutePath();
        Files.createDirectories(userDir);

        Path destination = userDir.resolve(uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        OteuIslamUserFile userFile = OteuIslamUserFile.builder()
                .user(user)
                .fileName(originalName)
                .fileType(fileType)
                .contentType(file.getContentType())
                .path(destination.toAbsolutePath().toString())
                .build();

        return userFileRepository.save(userFile);
    }

    private Resource loadResource(OteuIslamUserFile file) throws IOException {
        Path filePath = Paths.get(file.getPath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) return resource;
        throw new OteuIslamNotFoundException("File not readable: " + file.getId());
    }

    private void deleteFileFromDisk(OteuIslamUserFile file) {
        try {
            Files.deleteIfExists(Paths.get(file.getPath()));
        } catch (IOException e) {
            log.error("Failed to delete file from disk: {}", file.getPath());
        }
    }

    private OteuIslamUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OteuIslamNotFoundException("User not found"));
    }

    private OteuIslamUserFileResponse toResponse(OteuIslamUserFile file) {
        OteuIslamUserFileResponse response = new OteuIslamUserFileResponse();
        response.setId(file.getId());
        response.setFileName(file.getFileName());
        response.setFileType(file.getFileType());
        response.setContentType(file.getContentType());
        response.setUserId(file.getUser().getId());
        response.setUploadedAt(file.getUploadedAt());
        return response;
    }

    // ---- VALIDATION ----

    private void validateNotEmpty(MultipartFile file, String message) {
        if (file == null || file.isEmpty()) {
            throw new OteuIslamBadRequestException(message);
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new OteuIslamBadRequestException("File size exceeds 5MB limit");
        }
    }

    private void validateAvatarType(String contentType) {
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new OteuIslamBadRequestException("Avatar must be a JPEG or PNG image");
        }
    }

    private void validateDocumentType(String contentType) {
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new OteuIslamBadRequestException("Documents must be PDF files");
        }
    }
}