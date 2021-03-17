package wolox.training.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setName("Sebastian Rincón");
        testUser.setUsername("srincon");
        testUser.setBirthdate(LocalDate.parse("1997-06-05"));

    }

    @Test
    void whenSave_thenUserIsPersisted() {
        User persistedUser = userRepository.save(testUser);

        assertThat(persistedUser.getUsername()
                .equals(testUser.getUsername())).isTrue();

        assertThat(persistedUser.getName()
                .equals(testUser.getName())).isTrue();

        assertThat(persistedUser.getBirthdate()
                .equals(testUser.getBirthdate())).isTrue();

        assertThat(persistedUser.getBooks().size() == testUser.getBooks().size()).isTrue();
    }

    @Test
    void whenSaveUserWithoutName_thenThrowException() {
        testUser.setName(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(testUser);
        });
    }

    @Test
    void whenGetAll_thenReturnUsers(){
        assertThat(userRepository.findAll().size() > 0).isTrue();
    }
}