package de.hs.da.hskleinanzeigen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.entities.Type;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.payloads.requests.AdvertisementPostRequest;
import de.hs.da.hskleinanzeigen.repositories.AdvertisementRepository;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvertisementControllerIT {
    private final String ADVERTISEMENT_URL = "/api/advertisements";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private Advertisement testAdvertisement;

    private Category testCategory;

    @BeforeEach
    public void setup() {
        testCategory = new Category();
        testCategory.setName("testCategory");
        testCategory = categoryRepository.save(testCategory);

        testUser = new User();
        testUser.setEmail("testemail@test.de");
        testUser.setPassword("testpassword");
        testUser.setFirst_name("testfirstname");
        testUser.setLast_name("testlastname");
        testUser.setPhone("+49123456789");
        testUser.setLocation("Mörfelden-Walldorf");
        testUser = userRepository.save(testUser);

        testAdvertisement = new Advertisement();
        testAdvertisement.setId(99);
        testAdvertisement.setCategory(testCategory);
        testAdvertisement.setUser(testUser);
        testAdvertisement.setDescription("test adding advertisement");
        testAdvertisement.setTitle("test");
        testAdvertisement.setType(Type.OFFER);
        testAdvertisement.setPrice(999);
        testAdvertisement.setCreated(new Timestamp(System.currentTimeMillis()));
        testAdvertisement.setLocation("darmstadt");
        testAdvertisement = advertisementRepository.save(testAdvertisement);
    }

    @AfterEach
    public void tearDown() {
        this.advertisementRepository.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddAdvertisement() throws Exception {
        String NEW_ADVERTISEMENT_TITLE = "NEW advertisement title";
        String NEW_ADVERTISEMENT_DESCRIPTION = "NEW advertisement description";
        String NEW_ADVERTISEMENT_LOCATION = "frankfurt";
        Type NEW_ADVERTISEMENT_TYPE = Type.REQUEST;
        int NEW_ADVERTISEMENT_PRICE = 28;

        AdvertisementPostRequest request = new AdvertisementPostRequest();
        request.setUserId(this.testUser.getId());
        request.setCategoryId(this.testCategory.getId());

        request.setDescription(NEW_ADVERTISEMENT_DESCRIPTION);
        request.setTitle(NEW_ADVERTISEMENT_TITLE);
        request.setType(NEW_ADVERTISEMENT_TYPE);
        request.setPrice(NEW_ADVERTISEMENT_PRICE);
        request.setLocation(NEW_ADVERTISEMENT_LOCATION);
        String requestAsString = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post(this.ADVERTISEMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString)).andExpect(status()
                .isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("category").isNotEmpty())
                .andExpect(jsonPath("category.id").value(testAdvertisement.getCategory().getId()))
                .andExpect(jsonPath("category.name").value(testAdvertisement.getCategory().getName()))
                .andExpect(jsonPath("user").isNotEmpty())
                .andExpect(jsonPath("user.id").value(testAdvertisement.getUser().getId()))
                .andExpect(jsonPath("user.email").value(testAdvertisement.getUser().getEmail()))
                .andExpect(jsonPath("user.firstName").value(testAdvertisement.getUser().getFirst_name()))
                .andExpect(jsonPath("user.lastName").value(testAdvertisement.getUser().getLast_name()))
                .andExpect(jsonPath("user.phone").value(testAdvertisement.getUser().getPhone()))
                .andExpect(jsonPath("user.location").value(testAdvertisement.getUser().getLocation()))
                .andExpect(jsonPath("title").value(NEW_ADVERTISEMENT_TITLE))
                .andExpect(jsonPath("description").value(NEW_ADVERTISEMENT_DESCRIPTION))
                .andExpect(jsonPath("price").value(NEW_ADVERTISEMENT_PRICE))
                .andExpect(jsonPath("location").value(NEW_ADVERTISEMENT_LOCATION));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAdvertisement() throws Exception {
        mockMvc.perform(get(this.ADVERTISEMENT_URL + "/" + testAdvertisement.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("category").isNotEmpty())
                .andExpect(jsonPath("category.id").value(testAdvertisement.getCategory().getId()))
                .andExpect(jsonPath("category.name").value(testAdvertisement.getCategory().getName()))
                .andExpect(jsonPath("user").isNotEmpty())
                .andExpect(jsonPath("user.id").value(testAdvertisement.getUser().getId()))
                .andExpect(jsonPath("user.email").value(testAdvertisement.getUser().getEmail()))
                .andExpect(jsonPath("user.firstName").value(testAdvertisement.getUser().getFirst_name()))
                .andExpect(jsonPath("user.lastName").value(testAdvertisement.getUser().getLast_name()))
                .andExpect(jsonPath("user.phone").value(testAdvertisement.getUser().getPhone()))
                .andExpect(jsonPath("user.location").value(testAdvertisement.getUser().getLocation()))
                .andExpect(jsonPath("title").value(testAdvertisement.getTitle()))
                .andExpect(jsonPath("description").value(testAdvertisement.getDescription()))
                .andExpect(jsonPath("price").value(testAdvertisement.getPrice()))
                .andExpect(jsonPath("location").value(testAdvertisement.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Transactional
    public void testPostAdvertisementBadRequest() throws Exception {

        String NEW_ADVERTISEMENT_DESCRIPTION = "NEW advertisement description";
        String NEW_ADVERTISEMENT_LOCATION = "frankfurt";
        Type NEW_ADVERTISEMENT_TYPE = Type.REQUEST;
        int NEW_ADVERTISEMENT_PRICE = 28;

        AdvertisementPostRequest request = new AdvertisementPostRequest();
        request.setUserId(this.testUser.getId());
        request.setCategoryId(this.testCategory.getId());

        request.setDescription(NEW_ADVERTISEMENT_DESCRIPTION);
        request.setType(NEW_ADVERTISEMENT_TYPE);
        request.setPrice(NEW_ADVERTISEMENT_PRICE);
        request.setLocation(NEW_ADVERTISEMENT_LOCATION);
        String requestAsString = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post(this.ADVERTISEMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString)).andExpect(status()
                .isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostAdvertisementBadRequestForUser() throws Exception {
        String NEW_ADVERTISEMENT_DESCRIPTION = "NEW advertisement description";
        String NEW_ADVERTISEMENT_LOCATION = "frankfurt";
        Type NEW_ADVERTISEMENT_TYPE = Type.REQUEST;
        int NEW_ADVERTISEMENT_PRICE = 28;

        AdvertisementPostRequest request = new AdvertisementPostRequest();
        request.setCategoryId(this.testCategory.getId());

        request.setDescription(NEW_ADVERTISEMENT_DESCRIPTION);
        request.setType(NEW_ADVERTISEMENT_TYPE);
        request.setPrice(NEW_ADVERTISEMENT_PRICE);
        request.setLocation(NEW_ADVERTISEMENT_LOCATION);
        String requestAsString = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post(this.ADVERTISEMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString)).andExpect(status()
                .isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostAdvertisementBadRequestForCategory() throws Exception {
        String NEW_ADVERTISEMENT_DESCRIPTION = "NEW advertisement description";
        String NEW_ADVERTISEMENT_LOCATION = "frankfurt";
        Type NEW_ADVERTISEMENT_TYPE = Type.REQUEST;
        int NEW_ADVERTISEMENT_PRICE = 28;

        AdvertisementPostRequest request = new AdvertisementPostRequest();
        request.setUserId(this.testUser.getId());

        request.setDescription(NEW_ADVERTISEMENT_DESCRIPTION);
        request.setType(NEW_ADVERTISEMENT_TYPE);
        request.setPrice(NEW_ADVERTISEMENT_PRICE);
        request.setLocation(NEW_ADVERTISEMENT_LOCATION);
        String requestAsString = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post(this.ADVERTISEMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString)).andExpect(status()
                .isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAdvertisementNotFound() throws Exception {
        int NON_EXISTING_ADVERTISEMENT_ID = 123;
        mockMvc.perform(get(this.ADVERTISEMENT_URL + "/" + NON_EXISTING_ADVERTISEMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostAdvertisementBadRequestForType() throws Exception {
        String NON_EXISTING_CATEGORY = "Autowired";
        mockMvc.perform(get(this.ADVERTISEMENT_URL)
                        .param("type", NON_EXISTING_CATEGORY)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAdvertisements() throws Exception {
        mockMvc.perform(get(this.ADVERTISEMENT_URL)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").isNumber())
                .andExpect(jsonPath("content[0].category").isNotEmpty())
                .andExpect(jsonPath("content[0].category.id").isNumber())
                .andExpect(jsonPath("content[0].category.name").value(testAdvertisement.getCategory().getName()))
                .andExpect(jsonPath("content[0].user").isNotEmpty())
                .andExpect(jsonPath("content[0].user.id").isNumber())
                .andExpect(jsonPath("content[0].user.email").value(testAdvertisement.getUser().getEmail()))
                .andExpect(jsonPath("content[0].user.firstName").value(testAdvertisement.getUser().getFirst_name()))
                .andExpect(jsonPath("content[0].user.lastName").value(testAdvertisement.getUser().getLast_name()))
                .andExpect(jsonPath("content[0].user.phone").value(testAdvertisement.getUser().getPhone()))
                .andExpect(jsonPath("content[0].user.location").value(testAdvertisement.getUser().getLocation()))
                .andExpect(jsonPath("content[0].title").value(testAdvertisement.getTitle()))
                .andExpect(jsonPath("content[0].description").value(testAdvertisement.getDescription()))
                .andExpect(jsonPath("content[0].price").value(testAdvertisement.getPrice()))
                .andExpect(jsonPath("content[0].location").value(testAdvertisement.getLocation()));
    }
}
