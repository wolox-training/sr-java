package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "the book is already associated with the user")
public class BookAlReadyOwnedException extends RuntimeException {

    public BookAlReadyOwnedException() {
    }
}
