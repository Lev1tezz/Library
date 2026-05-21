package kz.iitu.Library.web.mapper;

import kz.iitu.Library.domain.entity.OteuIslamCategory;
import kz.iitu.Library.web.dto.request.OteuIslamCategoryRequest;
import kz.iitu.Library.web.dto.response.OteuIslamCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class OteuIslamCategoryMapper {

    public OteuIslamCategoryResponse toResponse(OteuIslamCategory category) {
        OteuIslamCategoryResponse response = new OteuIslamCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return response;
    }

    public OteuIslamCategory toEntity(OteuIslamCategoryRequest request) {
        return OteuIslamCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}