package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "records")
public class Records {

    @Id
    private Long id = 1L;

    @Setter
    @Column(nullable = false)
    private Integer numberOfCustomers = 0;

    @Setter
    @Column(nullable = false)
    private Integer numberOfTimers = 0;

    @Setter
    @Column(nullable = false)
    private Integer numberOfGroups = 0;

    @Setter
    @Column(nullable = false)
    private Integer numberOfTasks = 0;
}
