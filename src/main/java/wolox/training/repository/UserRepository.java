package wolox.training.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  @Query(value = "select u "
      + "from User u "
      + "where ((u.birthdate <= :end and u.birthdate >= :start))"
      + "and ((lower(u.name) like concat('%',:name,'%')) or :name is null )")
  List<User> findAllByBirthdateBetweenAndNameContaining(
      @Param("start") LocalDate startDate,
      @Param("end") LocalDate endDate,
      @Param("name") String sequenceCharacters);
}