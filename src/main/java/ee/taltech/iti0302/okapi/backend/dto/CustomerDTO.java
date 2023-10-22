package ee.taltech.iti0302.okapi.backend.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private String username;
    private String password;

    public CustomerDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
