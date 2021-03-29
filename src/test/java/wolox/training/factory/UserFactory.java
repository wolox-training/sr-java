package wolox.training.factory;

import wolox.training.model.User;

import java.time.LocalDate;

import static wolox.training.factory.DataTestConstants.BIRTHDATE;
import static wolox.training.factory.DataTestConstants.NAME;
import static wolox.training.factory.DataTestConstants.PASSWORD;
import static wolox.training.factory.DataTestConstants.USERNAME;

public class UserFactory {


    private Long id;
    private String username;
    private String name;
    private LocalDate birthdate;
    private String password;

    public UserFactory() {
        this.id = null;
        this.name = NAME;
        this.username = USERNAME;
        this.password = PASSWORD;
        this.birthdate = LocalDate.parse(BIRTHDATE);
    }

    public User newInstance() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setPassword(this.password);
        user.setUsername(this.username);
        user.setBirthdate(this.birthdate);

        return user;
    }

    public UserFactory setId(Long id) {
        this.id = id;
        return this;
    }

    public UserFactory setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserFactory setName(String name) {
        this.name = name;
        return this;
    }

    public UserFactory setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

}
