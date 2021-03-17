package wolox.training.controller;

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

import java.time.LocalDate;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.model.Book;
import wolox.training.model.User;
import wolox.training.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final List<User> userList = new ArrayList<>();
    private String CONTENT_WITHOUT_ID;
    private String CONTENT;
    private User testUser;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        Book testBook = new Book();
        testBook.setGenre("Mystery");
        testBook.setAuthor("Dan Brown");
        testBook.setImage("https://upload.wikimedia.org/wikipedia/en/b/bb/Inferno-cover.jpg");
        testBook.setTitle("Inferno");
        testBook.setSubtitle("Robert Langdon series");
        testBook.setPublisher("Doubleday");
        testBook.setYear("2013");
        testBook.setPages("642");
        testBook.setIsbn("978-0-385-53785-8");

        testUser = new User();
        testUser.setName("Sebastian Rincón");
        testUser.setUsername("srincon");
        testUser.setBirthdate(LocalDate.parse("1997-06-05"));
        testUser.addBook(testBook);

        userList.add(testUser);
        CONTENT_WITHOUT_ID = "{\"id\": null,\"username\": \"srincon\", \"name\": \"Sebastian Rincón\", \"birthdate\": \"2021-03-16T15:00:01.460Z\", \"books\":[]}";
        CONTENT = "{\"id\": 1,\"username\": \"srincon\", \"name\": \"Sebastian Rincón\", \"birthdate\": \"2021-03-16T15:00:01.460Z\", \"books\":[]}";
        User testUserWithoutBooks = new User();
        testUserWithoutBooks.setName("Sebastian Rincón");
        testUserWithoutBooks.setUsername("srincon");
        testUserWithoutBooks.setBirthdate(LocalDate.parse("1997-06-05"));

    }

    @Test
    void whenFindAll_thenUsersIsReturned() throws Exception {
        when(mockUserRepository.findAll()).thenReturn(userList);

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(testUser.getName())))
                .andExpect(jsonPath("$[0].username", is(testUser.getUsername())));
    }

    @Test
    void whenFindAll_thenNoUserExit() throws Exception {
        when(mockUserRepository.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenFindOneByUsername_thenUserIsReturned() throws Exception {
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        mvc.perform(get("/api/users/username?username=srincon")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())));
    }

    @Test
    void whenFindOneByUsername_thenNotFound() throws Exception {
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        mvc.perform(get("/api/users/username?username=srincon")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateUser_thenUserIsPersisted() throws Exception {
        when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT_WITHOUT_ID))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())));
    }

    @Test
    void whenCreateUserWithId_thenThrowException() throws Exception {
        when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdatedUser_thenUserIsPersisted() throws Exception {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

        mvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())));

    }

    @Test
    void whenUpdatedUserWithIdNotExist_thenThrowException() throws Exception {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdatedUserWithIdNotMismatch_thenThrowException() throws Exception {

        mvc.perform(put("/api/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CONTENT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() throws Exception {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        mvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Long ID_PATH_VARIABLE = 1L;
        verify(mockUserRepository).deleteById(ID_PATH_VARIABLE);

    }

    @Test
    void whenDeleteUserNotExist_thenThrowException() throws Exception {
        when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}