package wolox.training.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.model.Book;
import wolox.training.model.dto.BookInfoDto;
import wolox.training.model.mapper.BookMapper;
import wolox.training.service.OpenLibraryService;

import java.util.HashMap;
import java.util.Optional;

@PropertySource("classpath:url.properties")
@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final BookMapper bookMapper;

    @Value("${endpoint.open.library.book}")
    private String bookUrl;

    @Value("${format.response}")
    private String formatResponse;

    public OpenLibraryServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        String url = bookUrl + isbn + formatResponse;

        ResponseEntity<Optional<HashMap>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        BookInfoDto bookInfo = response.getBody()
                .map(data -> bookMapper.mapperMapToBookInfo(data, isbn))
                .orElseThrow(BookNotFoundException::new);

        return bookMapper.bookInfoToBook(bookInfo, isbn);
    }
}

