package kz.iitu.Library.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OteuIslamBookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String isbn;

    private String description;

    private Integer publishedYear;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private List<Long> authorIds;
}