package kz.iitu.Library.web.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamUserRepository;
import kz.iitu.Library.service.OteuIslamProfileService;
import kz.iitu.Library.web.dto.response.OteuIslamUserFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import kz.iitu.Library.domain.entity.OteuIslamUser;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile")
public class OteuIslamProfileController {

    private final OteuIslamProfileService profileService;

    @PostMapping("/avatar")
    @Operation(summary = "Upload or replace avatar (JPEG/PNG, max 5MB)")
    public ResponseEntity<OteuIslamUserFileResponse> uploadAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                profileService.uploadAvatar(userDetails.getUsername(), file));
    }

    @GetMapping("/avatar")
    @Operation(summary = "Download my avatar")
    public ResponseEntity<Resource> downloadAvatar(
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Resource resource = profileService.downloadAvatar(userDetails.getUsername());
        String contentType = profileService.getAvatarContentType(userDetails.getUsername());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/documents")
    @Operation(summary = "Upload PDF documents (max 5MB each)")
    public ResponseEntity<List<OteuIslamUserFileResponse>> uploadDocuments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(
                profileService.uploadDocuments(userDetails.getUsername(), files));
    }

    @GetMapping("/documents")
    @Operation(summary = "Get my documents list")
    public ResponseEntity<List<OteuIslamUserFileResponse>> getDocuments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                profileService.getMyDocuments(userDetails.getUsername()));
    }

    @GetMapping("/documents/{fileId}/download")
    @Operation(summary = "Download document by id")
    public ResponseEntity<Resource> downloadDocument(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long fileId) throws IOException {
        Resource resource = profileService.downloadDocument(fileId);
        String fileName = profileService.getDocumentFileName(fileId);
        String contentType = profileService.getDocumentContentType(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/documents/{fileId}")
    @Operation(summary = "Delete my document")
    public ResponseEntity<Void> deleteDocument(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long fileId) {
        profileService.deleteDocument(userDetails.getUsername(), fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get my profile info")
    public ResponseEntity<Map<String, String>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        OteuIslamUser user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new OteuIslamNotFoundException("User not found"));
        Map<String, String> info = new HashMap<>();
        info.put("firstName", user.getFirstName());
        info.put("lastName", user.getLastName());
        info.put("email", user.getEmail());
        info.put("role", user.getRole().name());
        return ResponseEntity.ok(info);
    }
    private final OteuIslamUserRepository userRepository;
}