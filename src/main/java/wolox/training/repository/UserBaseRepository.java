package wolox.training.repository;

import java.lang.annotation.Native;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import wolox.training.model.User;
import wolox.training.model.dto.UserInfo;

@NoRepositoryBean
public interface UserBaseRepository<T extends User>
        extends JpaRepository<T, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "select u "
            + "from User u "
            + "where ((u.birthdate <= :end and u.birthdate >= :start))"
            + "and ((lower(u.name) like concat('%',:name,'%')) or :name is null )")
    List<T> findAllByBirthdateBetweenAndNameContaining(
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate,
            @Param("name") String sequenceCharacters);

    @Query(value ="select id as id, "
            + " name as name, "
            + " username as username,"
            + " password as password,"
            + " subject as subject,"
            + " year as year,"
            + " birthdate as birthdate,"
            + " user_type as userType"
            + " from users",
            nativeQuery = true)
    List<UserInfo> findListAll();


}