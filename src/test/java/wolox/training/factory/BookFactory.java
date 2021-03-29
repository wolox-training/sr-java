package wolox.training.factory;


import wolox.training.model.Book;

import static wolox.training.factory.DataTestConstants.AUTHOR;
import static wolox.training.factory.DataTestConstants.GENRE;
import static wolox.training.factory.DataTestConstants.IMAGE;
import static wolox.training.factory.DataTestConstants.ISBN;
import static wolox.training.factory.DataTestConstants.PAGES;
import static wolox.training.factory.DataTestConstants.PUBLISHER;
import static wolox.training.factory.DataTestConstants.SUBTITLE;
import static wolox.training.factory.DataTestConstants.TITLE;
import static wolox.training.factory.DataTestConstants.YEAR;

public class BookFactory {


    private Long id;
    private String genre;
    private String author;
    private String image;
    private String title;
    private String subtitle;
    private String publisher;
    private String year;
    private String pages;
    private String isbn;

    public BookFactory() {
        this.id = null;
        this.genre = GENRE;
        this.author = AUTHOR;
        this.image = IMAGE;
        this.title = TITLE;
        this.subtitle = SUBTITLE;
        this.publisher = PUBLISHER;
        this.year = YEAR;
        this.pages = PAGES;
        this.isbn = ISBN;
    }

    public Book newInstance(){
        Book testBook = new Book();
        testBook.setId(this.id);
        testBook.setGenre(this.genre);
        testBook.setAuthor(this.author);
        testBook.setImage(this.image);
        testBook.setTitle(this.title);
        testBook.setSubtitle(this.subtitle);
        testBook.setPublisher(this.publisher);
        testBook.setYear(this.year);
        testBook.setPages(this.pages);
        testBook.setIsbn(this.isbn);
        return testBook;
    }

    public BookFactory setId(Long id){
        this.id = id;
        return this;
    }

    public BookFactory setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public BookFactory setAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookFactory setImage(String image) {
        this.image = image;
        return this;
    }

    public BookFactory setTitle(String title) {
        this.title = title;
        return this;
    }

    public BookFactory setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public BookFactory setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public BookFactory setYear(String year) {
        this.year = year;
        return this;
    }

    public BookFactory setPages(String pages) {
        this.pages = pages;
        return this;
    }

    public BookFactory setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }
}
