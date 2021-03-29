package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The id not match")

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
    }

    public UserIdMismatchException(String message) {
        super(message);
    }

    public UserIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
