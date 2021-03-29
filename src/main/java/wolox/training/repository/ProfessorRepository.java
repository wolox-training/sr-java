package wolox.training.repository;

import org.springframework.stereotype.Repository;
import wolox.training.model.Professor;

@Repository
public interface ProfessorRepository extends UserBaseRepository<Professor> {

}