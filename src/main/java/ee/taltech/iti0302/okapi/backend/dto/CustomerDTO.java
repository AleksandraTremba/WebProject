package ee.taltech.iti0302.okapi.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank
    private String username;
    private String password;

    private String newUsername;
    private String newPassword;
}
