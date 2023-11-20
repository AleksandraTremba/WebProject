package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Long timerId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
