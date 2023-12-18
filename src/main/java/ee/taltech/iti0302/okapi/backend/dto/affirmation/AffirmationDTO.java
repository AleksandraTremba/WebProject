package ee.taltech.iti0302.okapi.backend.dto.affirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AffirmationDTO {
    private Long id;
    private String affirmation;
}
