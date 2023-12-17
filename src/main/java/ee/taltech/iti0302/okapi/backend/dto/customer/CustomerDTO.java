package ee.taltech.iti0302.okapi.backend.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank
    private String username;

    private Long groupId;
    private Long timerId;
    private String token;
}
