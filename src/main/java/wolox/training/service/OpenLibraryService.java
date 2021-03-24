package wolox.training.service;

import wolox.training.model.Book;

public interface OpenLibraryService {

  Book findBookByIsbn(String isbn);
}
