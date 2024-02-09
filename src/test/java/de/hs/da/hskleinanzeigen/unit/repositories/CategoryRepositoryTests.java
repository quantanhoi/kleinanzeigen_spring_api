package de.hs.da.hskleinanzeigen.unit.repositories;

import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    private final String category_name_success = "Category_success";

    @BeforeEach
    public void setUp() {
        Category testCategory = new Category();
        testCategory.setName(category_name_success);

        this.categoryRepository.save(testCategory);
    }

    @AfterEach
    public void tearDown() {
        this.categoryRepository.deleteAll();
    }

    @Test
    public void test_CategoryRepository_found_category_by_existing_name() {
        Optional<Category> categoryOptional = this.categoryRepository.findOneByName(category_name_success);

        assertTrue(categoryOptional.isPresent());
    }

    @Test
    public void test_CategoryRepository_not_found_category_by_non_existing_name() {
        String category_name_failure = "Category_failure";
        Optional<Category> categoryOptional = this.categoryRepository.findOneByName(category_name_failure);

        assertFalse(categoryOptional.isPresent());
    }
}
