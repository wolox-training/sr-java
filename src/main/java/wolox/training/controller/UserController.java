package wolox.training.controller;

import static wolox.training.constants.MessageSwagger.INTERNAL_ERROR;
import static wolox.training.constants.MessageSwagger.RESOURCE_NOT_FOUND;
import static wolox.training.constants.MessageSwagger.SOMETHING_WRONG;
import static wolox.training.constants.MessageSwagger.SUCCESS_ADD_BOOKS_USER;
import static wolox.training.constants.MessageSwagger.SUCCESS_CREATE_USER;
import static wolox.training.constants.MessageSwagger.SUCCESS_GET_USER;
import static wolox.training.constants.MessageSwagger.SUCCESS_REMOVE_BOOKS_USER;
import static wolox.training.constants.MessageSwagger.SUCCESS_UPDATE_USER;
import static wolox.training.constants.MessageSwagger.TAGS_USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UserPasswordsMismatchException;
import wolox.training.model.User;
import wolox.training.model.dto.PasswordDto;
import wolox.training.repository.BookRepository;
import wolox.training.repository.UserRepository;
import wolox.training.utils.PageableUtils;

@RestController
@RequestMapping("/api/users")
@Api(value = TAGS_USER, tags = {TAGS_USER})
public class UserController {

  private final UserRepository userRepository;

  private final BookRepository bookRepository;

  private final PasswordEncoder passwordEncoder;

  private final PageableUtils pageableUtils;

  private final ObjectMapper objectMapper;

  @Autowired
  public UserController(UserRepository userRepository, BookRepository bookRepository,
      PasswordEncoder passwordEncoder, PageableUtils pageableUtils, ObjectMapper objectMapper) {
    this.userRepository = userRepository;
    this.bookRepository = bookRepository;
    this.passwordEncoder = passwordEncoder;
    this.pageableUtils = pageableUtils;
    this.objectMapper = objectMapper;
  }

  /**
   * This method gets list of {@link User}
   */
  @GetMapping
  @ApiOperation(value = "return users", response = User.class)
  @ResponseStatus(HttpStatus.OK)
  public Iterable<User> findAll(@RequestParam Map<String, String> params) {
    Pageable page = pageableUtils.paramsToPage(params);

    if (params.size() == 0) {
      return userRepository.findAll(page);
    }
    User userParams = objectMapper.convertValue(params, User.class);
    ExampleMatcher userMatcherExample = ExampleMatcher.matching();
    Example<User> userExample = Example.of(userParams, userMatcherExample);
    return userRepository.findAll(userExample, page);
  }

  /**
   * This method gets one {@link User} by username
   *
   * @param id id of the user (Long)
   * @return got {@link User} for username.
   * @throws UserNotFoundException if there is no user associated with that username
   */
  @GetMapping("/{id}")
  @ApiOperation(value = "Giving an id, return the user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SUCCESS_GET_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new));
  }

  /**
   * This method creates an {@link User} with the following parameters
   *
   * @param user: Representation the user like object (User)
   * @return created {@link ResponseEntity<User>}.
   * @throws UserException            if id field is not null
   * @throws IllegalArgumentException if the Object book contain attr with values illegals
   */
  @PostMapping
  @ApiOperation(value = "creates user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = SUCCESS_CREATE_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> create(@RequestBody User user) {
    if (user.getId() != null) {
      throw new UserException("for the creation request the id field must be null");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userRepository.save(user));
  }

  /**
   * This method updates an {@link User} with the following parameters
   *
   * @param id:   Identifier of user (long)
   * @param user: Representation the user like object (User)
   * @return updated {@link ResponseEntity<User>}.
   * @throws UserNotFoundException   if user not found on database
   * @throws UserIdMismatchException if id path no math with id RequestBody (User)
   * @throws UserException           if the Object user contain attr with values illegals
   */
  @PutMapping("/{id}")
  @ApiOperation(value = "updates user", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SUCCESS_UPDATE_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> update(@PathVariable long id, @RequestBody User user) {
    try {
      if (user.getId() != id) {
        throw new UserIdMismatchException();
      }

      User userDB = userRepository.findById(id)
          .orElseThrow(UserNotFoundException::new);

      user.setPassword(userDB.getPassword());

      return ResponseEntity.ok(userRepository.save(user));
    } catch (IllegalArgumentException e) {
      throw new UserException("the object request not is valid", e.getCause());
    }
  }

  /**
   * This method deletes an {@link User} with the attribute:
   *
   * @param id: Identifier of user (long)
   * @throws UserNotFoundException if user not found on database
   */
  @DeleteMapping("/{id}")
  @ApiOperation(value = "deletes user")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public void delete(@PathVariable long id) {

    userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
    userRepository.deleteById(id);
  }

  /**
   * this method add a book from a user's collection
   *
   * @param id:     Identifier of user (long)
   * @param idBook: Identifier of book (long)
   * @return got {@link User} updated
   * @throws UserNotFoundException if user not found on database
   * @throws BookNotFoundException if book not found on database
   */
  @PatchMapping("/{id}/add_book/{idBook}")
  @ApiOperation(value = "add book to a user' collection", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SUCCESS_ADD_BOOKS_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> addBook(@PathVariable long id, @PathVariable long idBook) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    user.addBook(bookRepository.findById(idBook).orElseThrow(BookNotFoundException::new));
    userRepository.save(user);
    return ResponseEntity.ok(user);
  }

  /**
   * this method removes a book from a user's collection
   *
   * @param id:     Identifier of user (long)
   * @param idBook: Identifier of book (long)
   * @return got {@link User} updated
   * @throws UserNotFoundException if user not found on database
   */
  @PatchMapping("/{id}/remove_book/{idBook}")
  @ApiOperation(value = "remove book from a user' collection", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SUCCESS_REMOVE_BOOKS_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> removeBook(@PathVariable long id, @PathVariable long idBook) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    user.removeBook(idBook);
    userRepository.save(user);
    return ResponseEntity.ok(user);
  }

  /**
   * This method updates an {@link User} with the following parameters
   *
   * @param id:          Identifier of user (long)
   * @param newPassword: Representation the password confirmation like object (PasswordDto)
   * @return updated {@link ResponseEntity<User>}.
   * @throws UserNotFoundException          if user not found on database
   * @throws UserPasswordsMismatchException if password no math with verifiedPassword (PasswordDto)
   * @throws UserException                  if the Object user contain attr with values illegals
   */
  @PatchMapping("/{id}/change_password")
  @ApiOperation(value = "change password", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = SUCCESS_UPDATE_USER),
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> changePassword(@PathVariable long id,
      @RequestBody PasswordDto newPassword) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    if (!newPassword.validatePasswordMismatch()) {
      throw new UserPasswordsMismatchException();
    }

    user.setPassword(passwordEncoder.encode(newPassword.getPassword()));

    return ResponseEntity.ok(userRepository.save(user));
  }

  @GetMapping("/me")
  @ApiOperation(value = "get user logged in", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<User> getUserSecurityContext() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    return ResponseEntity.ok(userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new));
  }

  @GetMapping("/")
  @ApiOperation(value = "Giving an date_start, date_end and name_like, return the users", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = SOMETHING_WRONG),
      @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
      @ApiResponse(code = 500, message = INTERNAL_ERROR)})
  public ResponseEntity<List<User>> getUsersByBirthdateAndName(
      @RequestParam(value = "date_start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
      @RequestParam(value = "date_end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
      @RequestParam(value = "name_like", required = false) String nameCharacters) {

    return ResponseEntity.ok(userRepository
        .findAllByBirthdateBetweenAndNameContaining(dateStart, dateEnd, nameCharacters));
  }


}
