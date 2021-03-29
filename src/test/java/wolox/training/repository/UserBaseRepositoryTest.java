package wolox.training.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.factory.UserFactory;
import wolox.training.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserBaseRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  UserBaseRepository userBaseRepository;

  private User testUser;

  @BeforeEach
  void setUp() {

    testUser = new UserFactory().newInstance();
  }

  @Test
  void whenSave_thenUserIsPersisted() {
    User persistedUser = userBaseRepository.save(testUser);

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

    Assertions
        .assertThrows(DataIntegrityViolationException.class, () -> userBaseRepository.save(testUser));
  }

  @Test
  void whenGetAll_thenReturnUsers() {
    assertThat(userBaseRepository.findAll().size() > 0).isTrue();
  }

  @Test
  void whenGetUsersByBirthdateAndName_thenReturnUsers() {
    LocalDate dateStart = LocalDate.parse("2021-01-01");
    LocalDate dateEnd = LocalDate.parse("2021-03-20");
    String nameLike = "seb";
    assertThat(
        userBaseRepository
            .findAllByBirthdateBetweenAndNameContaining(dateStart, dateEnd, nameLike)
            .size() > 0).isTrue();
  }
}