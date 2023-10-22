package ee.taltech.iti0302.okapi.backend.dto;

public class CustomerDTO {
    private Long id;
    private String username;
    private String password;

    public CustomerDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
