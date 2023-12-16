package ee.taltech.iti0302.okapi.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ee.taltech.iti0302.okapi.backend.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByTitle(String title, Pageable pageable);
}
