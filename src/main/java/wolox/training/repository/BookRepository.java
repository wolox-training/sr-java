package wolox.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByAuthor(String author);


}
