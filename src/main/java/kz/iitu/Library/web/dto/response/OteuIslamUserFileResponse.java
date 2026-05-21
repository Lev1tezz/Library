package kz.iitu.Library.web.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OteuIslamUserFileResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String contentType;
    private Long userId;
    private LocalDateTime uploadedAt;
}