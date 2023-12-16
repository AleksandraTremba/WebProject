package ee.taltech.iti0302.okapi.backend.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageResponse {
    private String message;
}
