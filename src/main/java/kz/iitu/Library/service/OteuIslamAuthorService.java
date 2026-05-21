package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamAuthor;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamAuthorRepository;
import kz.iitu.Library.web.dto.request.OteuIslamAuthorRequest;
import kz.iitu.Library.web.dto.response.OteuIslamAuthorResponse;
import kz.iitu.Library.web.mapper.OteuIslamAuthorMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OteuIslamAuthorService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamAuthorService.class);

    private final OteuIslamAuthorRepository authorRepository;
    private final OteuIslamAuthorMapper authorMapper;

    public List<OteuIslamAuthorResponse> getAll() {
        log.info("Fetching all authors");
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OteuIslamAuthorResponse getById(Long id) {
        log.info("Fetching author by id: {}", id);
        OteuIslamAuthor author = authorRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Author not found with id: " + id));
        return authorMapper.toResponse(author);
    }

    public OteuIslamAuthorResponse create(OteuIslamAuthorRequest request) {
        log.info("Creating author: {} {}", request.getFirstName(), request.getLastName());
        OteuIslamAuthor saved = authorRepository.save(authorMapper.toEntity(request));
        return authorMapper.toResponse(saved);
    }

    public OteuIslamAuthorResponse update(Long id, OteuIslamAuthorRequest request) {
        log.info("Updating author id: {}", id);
        OteuIslamAuthor author = authorRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Author not found with id: " + id));
        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        author.setBio(request.getBio());
        return authorMapper.toResponse(authorRepository.save(author));
    }

    public void delete(Long id) {
        log.info("Deleting author id: {}", id);
        if (!authorRepository.existsById(id)) {
            throw new OteuIslamNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}