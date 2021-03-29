package wolox.training.service;

import wolox.training.model.Book;
import wolox.training.model.dto.BookInfoDto;

import java.util.HashMap;

public interface OpenLibraryService {
   Book findBookByIsbn(String isbn);
}
