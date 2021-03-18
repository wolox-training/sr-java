package wolox.training.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class PasswordDto {

    @NonNull
    private String password;

    @NonNull
    private String verifiedPassword;

    public boolean validatePasswordMismatch(){
        return password.equals(verifiedPassword);
    }
}
