package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamCategory;
import kz.iitu.Library.exception.OteuIslamBadRequestException;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamCategoryRepository;
import kz.iitu.Library.web.dto.request.OteuIslamCategoryRequest;
import kz.iitu.Library.web.dto.response.OteuIslamCategoryResponse;
import kz.iitu.Library.web.mapper.OteuIslamCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OteuIslamCategoryService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamCategoryService.class);

    private final OteuIslamCategoryRepository categoryRepository;
    private final OteuIslamCategoryMapper categoryMapper;

    public List<OteuIslamCategoryResponse> getAll() {
        log.info("Fetching all categories");
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OteuIslamCategoryResponse getById(Long id) {
        log.info("Fetching category by id: {}", id);
        OteuIslamCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    public OteuIslamCategoryResponse create(OteuIslamCategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        if (categoryRepository.existsByName(request.getName())) {
            throw new OteuIslamBadRequestException("Category already exists: " + request.getName());
        }
        OteuIslamCategory saved = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toResponse(saved);
    }

    public OteuIslamCategoryResponse update(Long id, OteuIslamCategoryRequest request) {
        log.info("Updating category id: {}", id);
        OteuIslamCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new OteuIslamNotFoundException("Category not found with id: " + id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        log.info("Deleting category id: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new OteuIslamNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}