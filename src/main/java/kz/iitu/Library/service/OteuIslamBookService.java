package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamAuthor;
import kz.iitu.Library.domain.entity.OteuIslamBook;
import kz.iitu.Library.domain.entity.OteuIslamCategory;
import kz.iitu.Library.exception.OteuIslamBadRequestException;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamAuthorRepository;
import kz.iitu.Library.repository.OteuIslamBookRepository;
import kz.iitu.Library.repository.OteuIslamCategoryRepository;
import kz.iitu.Library.web.dto.request.OteuIslamBookRequest;
import kz.iitu.Library.web.dto.response.OteuIslamBookResponse;
import kz.iitu.Library.web.mapper.OteuIslamBookMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OteuIslamBookService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamBookService.class);

    private final OteuIslamBookRepository bookRepository;
    private final OteuIslamCategoryRepository categoryRepository;
    private final OteuIslamAuthorRepository authorRepository;
    private final OteuIslamBookMapper bookMapper;

    public Page<OteuIslamBookResponse> getAll(String search, Long categoryId,
                                              int page, int size, String sortBy) {
        log.info("Fetching books - search: {}, categoryId: {}, page: {}", search, categoryId, page);

        String searchParam = (search == null || search.trim().isEmpty()) ? null : search.trim();

        // Конвертируем camelCase в snake_case для нативного SQL
        String sortColumn = sortBy.equals("publishedYear") ? "published_year" : sortBy;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortColumn).ascending());
        return bookRepository.findAllWithFilters(searchParam, categoryId, pageable)
                .map(bookMapper::toResponse);
    }

    public OteuIslamBookResponse getById(Long id) {
        log.info("Fetching book by id: {}", id);
        OteuIslamBook book = bookRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Book not found with id: " + id));
        return bookMapper.toResponse(book);
    }

    public OteuIslamBookResponse create(OteuIslamBookRequest request) {
        log.info("Creating book: {}", request.getTitle());
        if (request.getIsbn() != null && !request.getIsbn().isEmpty()
                && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new OteuIslamBadRequestException("Book with this ISBN already exists");
        }

        OteuIslamCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new OteuIslamNotFoundException("Category not found"));

        List<OteuIslamAuthor> authors = new ArrayList<>();
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            authors = authorRepository.findAllById(request.getAuthorIds());
        }

        OteuIslamBook book = OteuIslamBook.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .publishedYear(request.getPublishedYear())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .category(category)
                .authors(authors)
                .build();

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public OteuIslamBookResponse update(Long id, OteuIslamBookRequest request) {
        log.info("Updating book id: {}", id);
        OteuIslamBook book = bookRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Book not found with id: " + id));

        OteuIslamCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new OteuIslamNotFoundException("Category not found"));

        List<OteuIslamAuthor> authors = new ArrayList<>();
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            authors = authorRepository.findAllById(request.getAuthorIds());
        }

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPublishedYear(request.getPublishedYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setCategory(category);
        book.setAuthors(authors);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public void delete(Long id) {
        log.info("Deleting book id: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new OteuIslamNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}