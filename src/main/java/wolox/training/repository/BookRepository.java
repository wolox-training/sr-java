package wolox.training.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByAuthor(String author);


}
