package ee.taltech.iti0302.okapi.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long groupId;
}
