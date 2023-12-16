package ee.taltech.iti0302.okapi.backend.dto.group;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDTO {
    private Long id;
    private String name;
    private String adminUsername;
}

