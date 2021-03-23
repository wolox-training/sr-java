package wolox.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllByBirthdateBetweenAndNameContaining(LocalDate startDate, LocalDate endDate, String sequenceCharacters);
}