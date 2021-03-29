package wolox.training.repository;

import org.springframework.stereotype.Repository;
import wolox.training.model.User;

@Repository
public interface UserRepository extends UserBaseRepository<User> {

}