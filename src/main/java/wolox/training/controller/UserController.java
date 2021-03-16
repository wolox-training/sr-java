package wolox.training.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import wolox.training.model.User;
import wolox.training.repository.BookRepository;
import wolox.training.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@Api(value = "Users", tags = {"Users"})
public class UserController {

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    @Autowired
    public UserController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * This method gets list of {@link User}
     */
    @GetMapping
    @ApiOperation(value = "return users", response = User.class)
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * This method gets one {@link User} by username
     *
     * @param username Username of the user (String)
     *
     * @return got {@link User} for username.
     * @throws UserNotFoundException if there is no user associated with that username
     */
    @GetMapping("/username")
    @ApiOperation(value = "Giving an username, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<User> findOneByAuthor(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new));
    }

    /**
     * This method creates an {@link User} with the following parameters
     *
     * @param user: Representation the user like object (User)
     *
     * @return created {@link ResponseEntity<User>}.
     * @throws UserException            if id field is not null
     * @throws IllegalArgumentException if the Object book contain attr with values illegals
     */
    @PostMapping
    @ApiOperation(value = "creates user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<User> create(@RequestBody User user) {
        if (user.getId() != null) {
            throw new UserException("for the creation request the id field must be null");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRepository.save(user));
    }

    /**
     * This method updates an {@link User} with the following parameters
     *
     * @param id:   Identifier of user (long)
     * @param user: Representation the user like object (User)
     *
     * @return updated {@link ResponseEntity<User>}.
     * @throws UserNotFoundException   if user not found on database
     * @throws UserIdMismatchException if id path no math with id RequestBody (User)
     * @throws UserException           if the Object user contain attr with values illegals
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "updates user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<User> update(@PathVariable long id, @RequestBody User user) {
        try {
            if (user.getId() != id) {
                throw new UserIdMismatchException();
            }

            userRepository.findById(id)
                    .orElseThrow(UserNotFoundException::new);

            return ResponseEntity.ok(userRepository.save(user));
        } catch (IllegalArgumentException e) {
            throw new UserException("the object request not is valid", e.getCause());
        }
    }

    /**
     * This method deletes an {@link User} with the attribute:
     *
     * @param id: Identifier of user (long)
     *
     * @throws UserNotFoundException if user not found on database
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deletes user")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
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
     *
     * @return got {@link User} updated
     * @throws UserNotFoundException if user not found on database
     * @throws BookNotFoundException if book not found on database
     */
    @PatchMapping("/{id}/add_book/{idBook}")
    @ApiOperation(value = "add book to a user' collection", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added book"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
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
     *
     * @return got {@link User} updated
     * @throws UserNotFoundException if user not found on database
     */
    @PatchMapping("/{id}/remove_book/{idBook}")
    @ApiOperation(value = "remove book from a user' collection", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed book"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<User> removeBook(@PathVariable long id, @PathVariable long idBook) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.removeBook(idBook);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}
