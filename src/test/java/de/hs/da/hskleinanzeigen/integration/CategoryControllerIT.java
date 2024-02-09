package de.hs.da.hskleinanzeigen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.payloads.requests.CategoryPostRequest;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private final String category_name = "Category_Exist";

    @BeforeEach
    public void setup() {
        Category category = new Category();
        category.setName(category_name);

        categoryRepository.save(category);
    }

    @AfterEach
    public void tearDown() {
        this.categoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostCategory_success() throws Exception {
        CategoryPostRequest categoryPostRequest = new CategoryPostRequest();
        categoryPostRequest.setName("Test_Category");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryPostRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostCategory_error_payload_incomplete() throws Exception {
        CategoryPostRequest categoryPostRequest = new CategoryPostRequest();

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryPostRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostCategory_error_already_exists() throws Exception {
        CategoryPostRequest categoryPostRequest = new CategoryPostRequest();
        categoryPostRequest.setName(category_name);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryPostRequest)))
                .andExpect(status().isConflict());
    }

}
