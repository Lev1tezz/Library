package kz.iitu.Library.web.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class OteuIslamBookResponse {

    private Long id;
    private String title;
    private String isbn;
    private String description;
    private Integer publishedYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private String categoryName;
    private List<String> authorNames;
}