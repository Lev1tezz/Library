package kz.iitu.Library.web.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OteuIslamBorrowResponse {

    private Long id;
    private String bookTitle;
    private String userEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private String status;
}