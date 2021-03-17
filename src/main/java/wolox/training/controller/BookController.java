package wolox.training.controller;

import java.util.List;
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

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * this method get all {@link Book}
     * @return got {@link List<Book> }
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }


    /**
     * This method gets one {@link Book} by name of author
     *
     * @param author Name of the book author (String)
     *
     * @return got {@link Book} for author.
     * @throws BookNotFoundException if there is no book associated with that author
     */
    @GetMapping("/author")
    public ResponseEntity<Book> findOneByAuthor(@RequestParam(name = "author") String author) {
        return ResponseEntity.ok(bookRepository.findByAuthor(author)
                .orElseThrow(BookNotFoundException::new));
    }

    /**
     * This method creates an {@link Book} with the following parameters
     *
     * @param book: Representation the book like object (Book)
     *
     * @return created {@link ResponseEntity<Book>}.
     * @throws BookException       if bad request the id belongs to a registered book
     * @throws IllegalArgumentException if the Object book contain attr with values illegals
     */
    @PostMapping
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
     *
     * @return updated {@link ResponseEntity<Book>}.
     * @throws BookNotFoundException if book not found on database
     * @throws BookIdMismatchException if id path no math with id RequestBody (Book)
     * @throws BookException         if the Object book contain attr with values illegals
     */
    @PutMapping("/{id}")
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
    public void delete(@PathVariable long id) {

        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        bookRepository.deleteById(id);
    }
}
