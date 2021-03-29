package wolox.training.model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private LocalDate birthdate;
    private String password;
    private String subject;
    private String year;
    private String userType;

}
