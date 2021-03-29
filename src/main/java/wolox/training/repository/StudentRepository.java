package wolox.training.repository;

import org.springframework.stereotype.Repository;
import wolox.training.model.Student;

@Repository
public interface StudentRepository extends UserBaseRepository<Student> {

}