package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The password not match")

public class UserPasswordsMismatchException extends RuntimeException {

    public UserPasswordsMismatchException() {
    }

    public UserPasswordsMismatchException(String message) {
        super(message);
    }

    public UserPasswordsMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
