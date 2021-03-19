package wolox.training.model.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import wolox.training.model.Book;
import wolox.training.model.dto.BookInfoDto;

import java.util.HashMap;

@Component
public class BookMapper {

    public Book bookInfoToBook(BookInfoDto bookInfo, String isbn){

        Book book = new Book();
        if(!bookInfo.getAuthors().isEmpty()){
            book.setAuthor(bookInfo.getAuthors().get(0).getName());
        }

        if(!bookInfo.getPublishers().isEmpty()){
            book.setPublisher(bookInfo.getAuthors().get(0).getName());
        }

        book.setIsbn(isbn);
        book.setTitle(bookInfo.getTitle());
        book.setSubtitle(bookInfo.getSubtitle());
        book.setPages(bookInfo.getNumberOfPages());
        book.setYear(bookInfo.getPublishDate());
        book.setImage(bookInfo.getCover().getMedium());

        return book;
    }

    public BookInfoDto mapperMapToBookInfo(HashMap data, String isbn) {
        final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper.convertValue(data.get("ISBN:" + isbn), BookInfoDto.class);
    }
}
