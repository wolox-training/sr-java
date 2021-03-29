package wolox.training.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookException;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.model.Book;
import wolox.training.repository.BookRepository;
import wolox.training.service.OpenLibraryService;

import java.util.List;

import static wolox.training.constants.MessageSwagger.INTERNAL_ERROR;
import static wolox.training.constants.MessageSwagger.RESOURCE_NOT_FOUND;
import static wolox.training.constants.MessageSwagger.SOMETHING_WRONG;
import static wolox.training.constants.MessageSwagger.SUCCESS_CREATE_BOOK;
import static wolox.training.constants.MessageSwagger.SUCCESS_GET_BOOK;
import static wolox.training.constants.MessageSwagger.SUCCESS_UPDATE_BOOK;
import static wolox.training.constants.MessageSwagger.TAGS_BOOK;

@RestController
@RequestMapping("/api/books")
@Api(value = TAGS_BOOK, tags = {TAGS_BOOK})
public class BookController {

    private final BookRepository bookRepository;

    private final OpenLibraryService openLibraryService;


    @Autowired
    public BookController(BookRepository bookRepository, OpenLibraryService openLibraryService) {
        this.bookRepository = bookRepository;
        this.openLibraryService = openLibraryService;
    }

    /**
     * this method get all {@link Book}
     *
     * @return got {@link List<Book> }
     */
    @GetMapping
    @ApiOperation(value = "return books", response = Book.class)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }


    /**
     * This method gets one {@link Book} by name of author
     *
     * @param author Name of the book author (String)
     * @return got {@link Book} for author.
     * @throws BookNotFoundException if there is no book associated with that author
     */
    @GetMapping("/author")
    @ApiOperation(value = "Giving an author, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_GET_BOOK),
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public ResponseEntity<Book> findOneByAuthor(@RequestParam(name = "author") String author) {
        return ResponseEntity.ok(bookRepository.findByAuthor(author)
                .orElseThrow(BookNotFoundException::new));
    }

    /**
     * This method creates an {@link Book} with the following parameters
     *
     * @param book: Representation the book like object (Book)
     * @return created {@link ResponseEntity<Book>}.
     * @throws BookException            if bad request the id belongs to a registered book
     * @throws IllegalArgumentException if the Object book contain attr with values illegals
     */
    @PostMapping
    @ApiOperation(value = "creates book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = SUCCESS_CREATE_BOOK),
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public ResponseEntity<Book> create(@RequestBody Book book) {
        if (book.getId() != null && bookRepository.existsById(book.getId())) {
            throw new BookException("bad request the id belongs to a registered book");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookRepository.save(book));
    }

    /**
     * This method updates an {@link Book} with the following parameters
     *
     * @param id:   Identifier of book (long)
     * @param book: Representation the book like object (Book)
     * @return updated {@link ResponseEntity<Book>}.
     * @throws BookNotFoundException   if book not found on database
     * @throws BookIdMismatchException if id path no math with id RequestBody (Book)
     * @throws BookException           if the Object book contain attr with values illegals
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "updates book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_UPDATE_BOOK),
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public ResponseEntity<Book> update(@PathVariable long id, @RequestBody Book book) {
        try {
            if (book.getId() != id) {
                throw new BookIdMismatchException();
            }

            bookRepository.findById(id)
                    .orElseThrow(BookNotFoundException::new);

            return ResponseEntity.ok(bookRepository.save(book));
        } catch (IllegalArgumentException e) {
            throw new BookException("the object request not is valid");
        }
    }

    /**
     * This method deletes an {@link Book} with the attribute:
     *
     * @param id: Identifier of book (long)
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "deletes book")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public void delete(@PathVariable long id) {

        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        bookRepository.deleteById(id);
    }

    @GetMapping("/isbn")
    @ApiOperation(value = "Giving an isbn, return the book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_GET_BOOK),
            @ApiResponse(code = 201, message = SUCCESS_CREATE_BOOK),
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public ResponseEntity<Book> findByIsbn(@RequestParam("isbn") String isbn) {

        return bookRepository.findBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(bookRepository.save(openLibraryService.findBookByIsbn(isbn))
                        ));
    }

    @GetMapping("/")
    @ApiOperation(value = "Giving an publisher,genre and year, return the books", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_GET_BOOK),
            @ApiResponse(code = 400, message = SOMETHING_WRONG),
            @ApiResponse(code = 404, message = RESOURCE_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_ERROR)})
    public ResponseEntity<List<Book>> findByPublisherAndGenreAndYear(@RequestParam("publisher") String publisher,
                                                                     @RequestParam("genre") String genre,
                                                                     @RequestParam("year") String year) {
        return ResponseEntity.ok(bookRepository.findAllByPublisherAndGenreAndYear(publisher, genre, year));
    }
}
