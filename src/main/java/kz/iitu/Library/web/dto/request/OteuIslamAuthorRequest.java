package kz.iitu.Library.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OteuIslamAuthorRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String bio;
}