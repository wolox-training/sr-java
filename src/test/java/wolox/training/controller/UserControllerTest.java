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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.factory.DataTestConstants.AUTH_PASSWORD;
import static wolox.training.factory.DataTestConstants.AUTH_USERNAME;
import static wolox.training.factory.DataTestConstants.PASSWORD_CONTENT;
import static wolox.training.factory.DataTestConstants.PASSWORD_WRONG_CONTENT;
import static wolox.training.factory.DataTestConstants.USER_CONTENT;
import static wolox.training.factory.DataTestConstants.USER_CONTENT_WITHOUT_ID;

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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.factory.BookFactory;
import wolox.training.factory.UserFactory;
import wolox.training.model.Book;
import wolox.training.model.User;
import wolox.training.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  public static final String API_USERS = "/api/users";
  public static final String API_USERS_1 = "/api/users/1";
  public static final String API_USERS_2 = "/api/users/2";
  public static final String API_USERS_2_PASSWORD = "/api/users/2/change_password";
  public static final String API_USER_ADVANCE_METHOD = "/api/users/?date_start=2021-01-05&date_end=2021-02-06&name_like=inter";

  private final List<User> userList = new ArrayList<>();
  private User testUser;
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserRepository mockUserRepository;

  @BeforeEach
  void setUp() {
    Book testBook = new BookFactory().newInstance();
    testUser = new UserFactory().newInstance();
    testUser.addBook(testBook);
    userList.add(testUser);
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindAll_thenUsersIsReturned() throws Exception {
    Page<User> pagedUsers = new PageImpl(userList);
    when(mockUserRepository.findAll(any(Example.class), any(Pageable.class)))
        .thenReturn(pagedUsers);
    when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(pagedUsers);

    mvc.perform(get(API_USERS)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].name", is(testUser.getName())))
        .andExpect(jsonPath("$.content[0].username", is(testUser.getUsername())));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindAll_thenNoUserExist() throws Exception {
    Page<User> pagedUsers = new PageImpl(Collections.emptyList());
    when(mockUserRepository.findAll(any(Example.class), any(Pageable.class)))
        .thenReturn(pagedUsers);
    when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(pagedUsers);
    mvc.perform(get(API_USERS)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindById_thenUserIsReturned() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

    mvc.perform(get(API_USERS_1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(testUser.getName())))
        .andExpect(jsonPath("$.username", is(testUser.getUsername())));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenFindById_thenNotFound() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

    mvc.perform(get(API_USERS_2)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void whenCreateUser_thenUserIsPersisted() throws Exception {
    when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

    mvc.perform(post(API_USERS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(USER_CONTENT_WITHOUT_ID))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is(testUser.getName())))
        .andExpect(jsonPath("$.username", is(testUser.getUsername())));
  }

  @Test
  void whenCreateUserWithId_thenThrowException() throws Exception {
    when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

    mvc.perform(post(API_USERS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(USER_CONTENT))
        .andExpect(status().isBadRequest());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenUpdatedUser_thenUserIsPersisted() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
    when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

    mvc.perform(put(API_USERS_1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(USER_CONTENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(testUser.getName())))
        .andExpect(jsonPath("$.username", is(testUser.getUsername())));

  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenUpdatedUserWithIdNotExist_thenThrowException() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

    mvc.perform(put(API_USERS_1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(USER_CONTENT))
        .andExpect(status().isNotFound());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenUpdatedUserWithIdNotMismatch_thenThrowException() throws Exception {

    mvc.perform(put(API_USERS_2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(USER_CONTENT))
        .andExpect(status().isBadRequest());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenDeleteUser_thenUserIsDeleted() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

    mvc.perform(delete(API_USERS_1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    Long ID_PATH_VARIABLE = 1L;
    verify(mockUserRepository).deleteById(ID_PATH_VARIABLE);

  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenDeleteUserNotExist_thenThrowException() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());

    mvc.perform(delete(API_USERS_1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenChangePasswordNotMatch_thenThrowException() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

    mvc.perform(patch(API_USERS_2_PASSWORD)
        .contentType(MediaType.APPLICATION_JSON)
        .content(PASSWORD_WRONG_CONTENT))
        .andExpect(status().isBadRequest());
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenChangePassword_thenPasswordIsPersisted() throws Exception {
    when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
    when(mockUserRepository.save(any(User.class))).thenReturn(testUser);

    mvc.perform(patch(API_USERS_2_PASSWORD)
        .contentType(MediaType.APPLICATION_JSON)
        .content(PASSWORD_CONTENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(testUser.getName())))
        .andExpect(jsonPath("$.username", is(testUser.getUsername())))
        .andExpect(jsonPath("$.password", is(testUser.getPassword())));
  }

  @WithMockUser(username = AUTH_USERNAME, password = AUTH_PASSWORD)
  @Test
  void whenGetUsersByBirthdateAndName_thenUsersReturn() throws Exception {
    when(mockUserRepository
        .findAllByBirthdateBetweenAndNameContaining(
            any(LocalDate.class),
            any(LocalDate.class),
            anyString()))
        .thenReturn(userList);

    mvc.perform(get(API_USER_ADVANCE_METHOD)
        .contentType(MediaType.APPLICATION_JSON)
        .content(PASSWORD_CONTENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is(testUser.getName())))
        .andExpect(jsonPath("$[0].username", is(testUser.getUsername())));
  }

}