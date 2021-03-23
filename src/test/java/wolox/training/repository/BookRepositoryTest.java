package wolox.training.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.factory.BookFactory;
import wolox.training.model.Book;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  BookRepository bookRepository;

  private Book testBook;

  @BeforeEach
  void setUp() {
    testBook = new BookFactory().newInstance();

  }

  @Test
  void whenSave_thenBookIsPersisted() {
    Book persistedBook = bookRepository.save(testBook);

    assertThat(persistedBook.getAuthor())
        .isEqualTo(testBook.getAuthor());
    assertThat(persistedBook.getIsbn())
        .isEqualTo(testBook.getIsbn());
    assertThat(persistedBook.getTitle())
        .isEqualTo(testBook.getTitle());
    assertThat(persistedBook.getSubtitle())
        .isEqualTo(testBook.getSubtitle());
    assertThat(persistedBook.getImage())
        .isEqualTo(testBook.getImage());
    assertThat(persistedBook.getPublisher())
        .isEqualTo(testBook.getPublisher());
    assertThat(persistedBook.getYear())
        .isEqualTo(testBook.getYear());
    assertThat(persistedBook.getGenre())
        .isEqualTo(testBook.getGenre());
  }

  @Test
  void whenSaveBookWithoutAuthor_thenThrowException() {
    testBook.setAuthor(null);

    Assertions
        .assertThrows(DataIntegrityViolationException.class, () -> bookRepository.save(testBook));
  }

  @Test
  void whenGetAll_thenReturnBooks() {
    assertThat(bookRepository.findAll().size() > 0).isTrue();
  }

  @Test
  void whenGetAllByPublisherAndGenreAndYear_thenReturnBooks() {
    String year = "1994";
    String genre = "terror";
    String publisher = "Zhizhong Cai";
    assertThat(bookRepository
        .findAllByPublisherAndGenreAndYear(publisher, genre, year).size() > 0)
        .isTrue();
  }
}