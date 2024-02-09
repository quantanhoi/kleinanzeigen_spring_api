package de.hs.da.hskleinanzeigen.unit.repositories;

import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.entities.Type;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.repositories.AdvertisementRepository;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdvertisementRepositoryTests {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User testUser = new User();
        testUser.setEmail("test1@example.com");
        testUser.setPassword("password");
        testUser.setFirst_name("John");
        testUser.setLast_name("Doe");

        userRepository.save(testUser);

        Category testCategory = new Category();
        testCategory.setName("Category_1");

        categoryRepository.save(testCategory);

        Advertisement testAdvertisement = new Advertisement();
        testAdvertisement.setType(Type.OFFER);
        testAdvertisement.setTitle("Title of Advertisement");
        testAdvertisement.setCategory(testCategory);
        testAdvertisement.setUser(testUser);
        testAdvertisement.setDescription("Description of Advertisement");

        testAdvertisement.setId(1234);

        advertisementRepository.save(testAdvertisement);

    }

    @AfterEach
    public void tearDown() {
        this.advertisementRepository.deleteAll();
    }

    @Test
    public void test_AdvertisementRepository_get_all_Ads() {
        List<Advertisement> advertisementList = this.advertisementRepository.findAll();

        Assertions.assertThat(advertisementList).hasSizeGreaterThan(0);
    }

    @Test
    public void test_AdvertisementRepository_ad_has_correct_user_by_existing_id() {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(1234);

        assertTrue(advertisementOptional.isPresent());
        assertEquals(advertisementOptional.get().getUser().getEmail(), "test1@example.com");
    }

    @Test
    public void test_AdvertisementRepository_ad_has_correct_user_by_non_existing_id() {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(4321);

        assertFalse(advertisementOptional.isPresent());
    }

    @Test
    public void test_AdvertisementRepository_ad_has_correct_category_by_existing_id() {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(1234);

        assertTrue(advertisementOptional.isPresent());
        assertEquals(advertisementOptional.get().getCategory().getName(), "Category_1");
    }

    @Test
    public void test_AdvertisementRepository_ad_has_correct_category_by_non_existing_id() {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(4321);

        assertFalse(advertisementOptional.isPresent());
    }
}
