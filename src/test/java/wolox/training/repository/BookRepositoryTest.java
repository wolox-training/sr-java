package wolox.training.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.model.Book;
import wolox.training.model.User;

import javax.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


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
        testBook = new Book();
        testBook.setGenre("Mystery");
        testBook.setAuthor("Dan Brown");
        testBook.setImage("https://upload.wikimedia.org/wikipedia/en/b/bb/Inferno-cover.jpg");
        testBook.setTitle("Inferno");
        testBook.setSubtitle("Robert Langdon series");
        testBook.setPublisher("Doubleday");
        testBook.setYear("2013");
        testBook.setPages("642");
        testBook.setIsbn("978-0-385-53785-8");

    }

    @Test
    void whenSave_thenBookIsPersisted() {
        Book persistedBook = bookRepository.save(testBook);

        assertThat(persistedBook.getAuthor()
                .equals(testBook.getAuthor())).isTrue();

        assertThat(persistedBook.getIsbn()
                .equals(testBook.getIsbn())).isTrue();

        assertThat(persistedBook.getTitle()
                .equals(testBook.getTitle())).isTrue();

        assertThat(persistedBook.getSubtitle()
                .equals(testBook.getSubtitle())).isTrue();

        assertThat(persistedBook.getImage()
                .equals(testBook.getImage())).isTrue();

        assertThat(persistedBook.getPublisher()
                .equals(testBook.getPublisher())).isTrue();

        assertThat(persistedBook.getYear()
                .equals(testBook.getYear())).isTrue();

        assertThat(persistedBook.getGenre()
                .equals(testBook.getGenre())).isTrue();
    }

    @Test
    void whenSaveBookWithoutAuthor_thenThrowException() {
        testBook.setAuthor(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            bookRepository.save(testBook);
        });
    }

    @Test
    void whenGetAll_thenReturnBooks(){
        assertThat(bookRepository.findAll().size() > 0).isTrue();
    }
}