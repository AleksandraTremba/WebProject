package ee.taltech.iti0302.okapi.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupInfoTransferDTO {
    private Long id;
    private Long name;
    private List<String> members;
}
