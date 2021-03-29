package wolox.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByAuthor(String author);

    Optional<Book> findBookByIsbn(String isbn);

    @Query(value = "select b "
        + "from Book b "
        + "where (b.publisher = :publisher or :publisher is null )"
        + "and (b.genre = :genre or :genre is null )"
        + "and (b.year = :year or :year is null )")
    List<Book> findAllByPublisherAndGenreAndYear(
        @Param("publisher") String publisher,
        @Param("genre") String genre,
        @Param("year") String year);

}
