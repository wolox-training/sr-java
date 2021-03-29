package wolox.training.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.factory.BookFactory;
import wolox.training.model.Book;
import wolox.training.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.factory.DataTestConstants.BOOK_CONTENT;
import static wolox.training.factory.DataTestConstants.BOOK_CONTENT_WITHOUT_ID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    public static final String API_BOOKS = "/api/books";
    public static final String API_BOOKS_AUTHOR = "/api/books/author?author=Dan Brown";
    public static final String API_BOOKS_1 = "/api/books/1";
    public static final String API_BOOKS_2 = "/api/books/2";
    private final List<Book> bookList = new ArrayList<>();

    private Book testBook;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository mockBookRepository;

    @BeforeEach
    void setUp() {
        testBook = new BookFactory().newInstance();
        bookList.add(testBook);
    }

    @Test
    void whenFindAll_thenBooksIsReturned() throws Exception {
        when(mockBookRepository.findAll()).thenReturn(bookList);

        mvc.perform(get(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(testBook.getAuthor())))
                .andExpect(jsonPath("$[0].genre", is(testBook.getGenre())))
                .andExpect(jsonPath("$[0].publisher", is(testBook.getPublisher())))
                .andExpect(jsonPath("$[0].isbn", is(testBook.getIsbn())));
    }

    @Test
    void whenFindAll_thenNoBooksExist() throws Exception {
        when(mockBookRepository.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenFindOneByAuthor_thenUserIsReturned() throws Exception {
        when(mockBookRepository.findByAuthor(testBook.getAuthor())).thenReturn(Optional.of(testBook));

        mvc.perform(get(API_BOOKS_AUTHOR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is(testBook.getAuthor())))
                .andExpect(jsonPath("$.genre", is(testBook.getGenre())))
                .andExpect(jsonPath("$.publisher", is(testBook.getPublisher())))
                .andExpect(jsonPath("$.isbn", is(testBook.getIsbn())));
    }

    @Test
    void whenFindOneByAuthor_thenNotFound() throws Exception {
        when(mockBookRepository.findByAuthor(testBook.getAuthor())).thenReturn(Optional.empty());

        mvc.perform(get(API_BOOKS_AUTHOR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateBook_thenBookIsPersisted() throws Exception {
        when(mockBookRepository.save(any(Book.class))).thenReturn(testBook);

        mvc.perform(post(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BOOK_CONTENT_WITHOUT_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author", is(testBook.getAuthor())))
                .andExpect(jsonPath("$.genre", is(testBook.getGenre())))
                .andExpect(jsonPath("$.publisher", is(testBook.getPublisher())))
                .andExpect(jsonPath("$.isbn", is(testBook.getIsbn())));
    }


    @Test
    void whenUpdatedUser_thenUserIsPersisted() throws Exception {
        when(mockBookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));
        when(mockBookRepository.save(any(Book.class))).thenReturn(testBook);

        mvc.perform(put(API_BOOKS_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BOOK_CONTENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is(testBook.getAuthor())))
                .andExpect(jsonPath("$.genre", is(testBook.getGenre())))
                .andExpect(jsonPath("$.publisher", is(testBook.getPublisher())))
                .andExpect(jsonPath("$.isbn", is(testBook.getIsbn())));

    }

    @Test
    void whenUpdatedUserWithIdNotExist_thenThrowException() throws Exception {
        when(mockBookRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(put(API_BOOKS_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BOOK_CONTENT))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdatedUserWithIdNotMismatch_thenThrowException() throws Exception {

        mvc.perform(put(API_BOOKS_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(BOOK_CONTENT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() throws Exception {
        when(mockBookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));

        mvc.perform(delete(API_BOOKS_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Long ID_PATH_VARIABLE = 1L;
        verify(mockBookRepository).deleteById(ID_PATH_VARIABLE);

    }

    @Test
    void whenDeleteUserNotExist_thenThrowException() throws Exception {
        when(mockBookRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(delete(API_BOOKS_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}