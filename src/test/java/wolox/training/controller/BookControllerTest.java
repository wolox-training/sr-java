package wolox.training.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.factory.DataTestConstants.AUTH_PASSWORD;
import static wolox.training.factory.DataTestConstants.AUTH_USERNAME;
import static wolox.training.factory.DataTestConstants.BOOK_CONTENT;
import static wolox.training.factory.DataTestConstants.BOOK_CONTENT_WITHOUT_ID;
import static wolox.training.factory.DataTestConstants.PASSWORD_CONTENT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.factory.BookFactory;
import wolox.training.model.Book;
import wolox.training.repository.BookRepository;
import wolox.training.service.impl.OpenLibraryServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

  public static final String API_BOOKS = "/api/books";
  public static final String API_BOOKS_AUTHOR = "/api/books/author?author=Dan Brown";
  public static final String API_BOOKS_ISBN = "/api/books/isbn?isbn=0385472579";
  public static final String API_BOOKS_1 = "/api/books/1";
  public static final String API_BOOKS_2 = "/api/books/2";
  public static final String API_BOOK_ADVANCE_METHOD = "/api/books/?publisher=Zhizhong Cai&genre=terror&year=1994";
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

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindAll_thenBooksIsReturned() throws Exception {
    Page<Book> pagedBooks = new PageImpl(bookList);

    when(mockBookRepository.findAll(any(Example.class), any(Pageable.class)))
        .thenReturn(pagedBooks);
    when(mockBookRepository.findAll(any(Pageable.class)))
        .thenReturn(pagedBooks);

    mvc.perform(get(API_BOOKS)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].author", is(testBook.getAuthor())))
        .andExpect(jsonPath("$.content[0].genre", is(testBook.getGenre())))
        .andExpect(jsonPath("$.content[0].publisher", is(testBook.getPublisher())))
        .andExpect(jsonPath("$.content[0].isbn", is(testBook.getIsbn())));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindAll_thenNoBooksExist() throws Exception {
    Page<Book> pagedBooks = new PageImpl(Collections.emptyList());

    when(mockBookRepository.findAll(any(Example.class), any(Pageable.class)))
        .thenReturn(pagedBooks);
    when(mockBookRepository.findAll(any(Pageable.class)))
        .thenReturn(pagedBooks);

    when(mockBookRepository.findAll()).thenReturn(Collections.emptyList());
    mvc.perform(get(API_BOOKS)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
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

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindOneByIsbn_thenUserIsCreated() throws Exception {
    when(mockBookRepository.findBookByIsbn(anyString())).thenReturn(Optional.empty());
    mvc.perform(get(API_BOOKS_ISBN)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.author", is(testBook.getAuthor())))
        .andExpect(jsonPath("$.genre", is(testBook.getGenre())))
        .andExpect(jsonPath("$.publisher", is(testBook.getPublisher())))
        .andExpect(jsonPath("$.isbn", is(testBook.getIsbn())));
  }


  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
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

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
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

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenUpdatedUserWithIdNotExist_thenThrowException() throws Exception {
    when(mockBookRepository.findById(anyLong())).thenReturn(Optional.empty());

    mvc.perform(put(API_BOOKS_1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(BOOK_CONTENT))
        .andExpect(status().isNotFound());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenUpdatedUserWithIdNotMismatch_thenThrowException() throws Exception {

    mvc.perform(put(API_BOOKS_2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(BOOK_CONTENT))
        .andExpect(status().isBadRequest());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenDeleteUser_thenUserIsDeleted() throws Exception {
    when(mockBookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));

    mvc.perform(delete(API_BOOKS_1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    Long ID_PATH_VARIABLE = 1L;
    verify(mockBookRepository).deleteById(ID_PATH_VARIABLE);

  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenDeleteUserNotExist_thenThrowException() throws Exception {
    when(mockBookRepository.findById(anyLong())).thenReturn(Optional.empty());

    mvc.perform(delete(API_BOOKS_1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenGetUsersByBirthdateAndName_thenUsersReturn() throws Exception {
    when(mockBookRepository
        .findAllByPublisherAndGenreAndYear(
            anyString(),
            anyString(),
            anyString()))
        .thenReturn(bookList);

    mvc.perform(get(API_BOOK_ADVANCE_METHOD)
        .contentType(MediaType.APPLICATION_JSON)
        .content(PASSWORD_CONTENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].publisher", is(testBook.getPublisher())))
        .andExpect(jsonPath("$[0].isbn", is(testBook.getIsbn())));
  }

}