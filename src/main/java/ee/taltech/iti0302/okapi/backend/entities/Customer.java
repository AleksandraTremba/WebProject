package ee.taltech.iti0302.okapi.backend.entities;

import ee.taltech.iti0302.okapi.backend.enums.GroupRoles;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @NonNull
    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @JoinColumn(name = "timer_id")
    private Long timerId;

    @Setter
    @JoinColumn(name = "group_id")
    private Long groupId;

    @Setter
    @Enumerated(EnumType.STRING)
    private GroupRoles groupRole;
}
