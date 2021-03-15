package wolox.training.model;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ",sequenceName = "BOOK_SEQ")
    private Long id;

    @Column
    private String genre;

    @NotNull
    @Column(nullable = false)
    private String author;

    @NotNull
    @Column(nullable = false)
    private String image;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String subtitle;

    @NotNull
    @Column(nullable = false)
    private String publisher;

    @NotNull
    @Column(nullable = false)
    private String year;

    @NotNull
    @Column(nullable = false)
    private String pages;

    @NotNull
    @Column(nullable = false)
    private String isbn;
}
