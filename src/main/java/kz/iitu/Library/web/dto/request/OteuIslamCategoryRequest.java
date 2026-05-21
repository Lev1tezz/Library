package kz.iitu.Library.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OteuIslamCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}