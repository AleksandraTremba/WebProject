package ee.taltech.iti0302.okapi.backend.dto;

import lombok.*;

@Data
public class CustomerDTO {
    private Long id;
    private String username;
    private String password;

    private String newUsername;
    private String newPassword;
}
