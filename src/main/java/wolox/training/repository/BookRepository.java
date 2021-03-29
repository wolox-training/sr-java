package wolox.training.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.model.Book;

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

  @Query(value = "select b "
      + "from Book b "
      + "where (b.publisher = :publisher or :publisher is null )"
      + "and (b.genre = :genre or :genre is null )"
      + "and (b.author = :author or :author is null )"
      + "and (b.title = :title or :title is null )"
      + "and (b.subtitle = :subtitle or :subtitle is null )"
      + "and (b.year = :year or :year is null )"
      + "and (b.pages = :pages or :pages is null )"
      + "and (b.id = :id or :id is null )"
      + "and (b.isbn = :isbn or :isbn is null )")
  List<Book> findAllFilters(
      @Param("publisher") String publisher,
      @Param("genre") String genre,
      @Param("year") String year,
      @Param("author") String author,
      @Param("title") String title,
      @Param("subtitle") String subtitle,
      @Param("isbn") String isbn,
      @Param("pages") String pages,
      @Param("id") Long id
  );

}
