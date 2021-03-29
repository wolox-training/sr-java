package wolox.training.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.factory.UserFactory;
import wolox.training.model.User;

import javax.persistence.EntityManager;

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

        testUser = new UserFactory().newInstance();
    }

    @Test
    void whenSave_thenUserIsPersisted() {
        User persistedUser = userRepository.save(testUser);

        assertThat(persistedUser.getUsername())
                .isEqualTo(testUser.getUsername());

        assertThat(persistedUser.getName())
                .isEqualTo(testUser.getName());

        assertThat(persistedUser.getBirthdate())
                .isEqualTo(testUser.getBirthdate());

        assertThat(persistedUser.getBooks().size()).isSameAs(testUser.getBooks().size());
    }

    @Test
    void whenSaveUserWithoutName_thenThrowException() {
        testUser.setName(null);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(testUser));
    }

    @Test
    void whenGetAll_thenReturnUsers() {
        assertThat(userRepository.findAll().size() > 0).isTrue();
    }
}