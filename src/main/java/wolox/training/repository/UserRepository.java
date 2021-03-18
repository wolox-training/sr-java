package wolox.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
