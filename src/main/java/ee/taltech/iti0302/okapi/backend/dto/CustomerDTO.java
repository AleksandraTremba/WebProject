package ee.taltech.iti0302.okapi.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CustomerDTO {
    @NonNull private Long id;
    @NonNull private String username;
    @NonNull private String password;

    private String newUsername;
    private String newPassword;
}
