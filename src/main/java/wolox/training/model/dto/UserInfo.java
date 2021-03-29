package wolox.training.model.dto;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import wolox.training.model.Book;

public interface UserInfo {

    Long getId();

    LocalDate getBirthdate();

    String getName();

    String getPassword();

    String getSubject();

    String getUsername();

    String getYear();

    String getUserType();

}
