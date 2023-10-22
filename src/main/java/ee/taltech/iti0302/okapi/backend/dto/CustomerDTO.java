package ee.taltech.iti0302.okapi.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerDTO {
    @NonNull private String username;
    @NonNull private String password;

    @Getter
    @Setter
    private String newUsername;

    @Getter
    @Setter
    private String newPassword;
}
