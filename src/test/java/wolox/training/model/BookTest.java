package wolox.training.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static wolox.training.constants.MessageSwagger.MESSAGE_NOT_NULL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookTest {

    public static final long ID = 1L;
    public static final String GENRE = "Terror";

    @Test
    void whenSetId_thenThrow() {
        Book book = new Book();
        assertThatThrownBy(
                () -> book.setId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(MESSAGE_NOT_NULL, new Object[]{null}).hasNoCause();
    }

    @Test
    void whenSetId_thenSet() {
        Book book = new Book();
        book.setId(ID);
        assertThat(book.getId()).isEqualTo(ID);
    }


    @Test
    void whenSetGenre_thenThrow() {
        Book book = new Book();
        assertThatThrownBy(
                () -> book.setGenre(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(MESSAGE_NOT_NULL, new Object[]{null}).hasNoCause();
    }

    @Test
    void whenSetGenre_thenSet() {
        Book book = new Book();
        book.setGenre(GENRE);
        assertThat(book.getGenre()).isEqualTo(GENRE);
    }

}