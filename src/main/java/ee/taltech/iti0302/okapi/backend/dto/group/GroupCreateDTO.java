package ee.taltech.iti0302.okapi.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GroupCreateDTO {
    private String name;
    private Long adminId;
}
