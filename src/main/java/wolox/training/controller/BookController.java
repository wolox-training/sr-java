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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/author")
    public ResponseEntity<Book> findOneByAuthor(@RequestParam(name = "author") String author) {
        return ResponseEntity.ok(bookRepository.findByAuthor(author)
                .orElseThrow(BookNotFoundException::new));
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        if (book.getId() != null && bookRepository.existsById(book.getId())) {
            throw new BookException("bad request the id belongs to a registered book");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookRepository.save(book));
    }

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {

        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        bookRepository.deleteById(id);
    }
}
