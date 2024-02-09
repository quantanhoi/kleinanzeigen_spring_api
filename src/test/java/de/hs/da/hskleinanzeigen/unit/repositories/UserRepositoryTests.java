package de.hs.da.hskleinanzeigen.unit.repositories;

import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User testUser = new User();
        testUser.setEmail("test1@example.com");
        testUser.setPassword("password");
        testUser.setFirst_name("John");
        testUser.setLast_name("Doe");

        this.userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    public void test_UserRepository_found_user_by_existing_email() {
        Optional<User> userOptional = this.userRepository.findUserByEmail("test1@example.com");
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        assertEquals("test1@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("John", user.getFirst_name());
        assertEquals("Doe", user.getLast_name());
    }

    @Test
    public void test_UserRepository_notfound_user_by_non_existing_email() {
        Optional<User> userOptional = this.userRepository.findUserByEmail("nonexistent@example.com");
        assertFalse(userOptional.isPresent());
    }
}
