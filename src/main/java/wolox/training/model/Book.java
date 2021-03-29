package wolox.training.model;

import static wolox.training.constants.MessageSwagger.MESSAGE_NOT_NULL;

import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @Setter(AccessLevel.NONE)
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

    public void setId(Long id) {
        Preconditions.checkNotNull(id, MESSAGE_NOT_NULL, new Object[]{null});
        this.id = id;
    }

    public void setGenre(String genre) {
        Preconditions.checkNotNull(genre, MESSAGE_NOT_NULL, new Object[]{null});
        this.genre = genre;
    }
}
