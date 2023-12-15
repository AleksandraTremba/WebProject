package ee.taltech.iti0302.okapi.backend.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CustomerInitDTO {
    private String username;
    private String password;
}
