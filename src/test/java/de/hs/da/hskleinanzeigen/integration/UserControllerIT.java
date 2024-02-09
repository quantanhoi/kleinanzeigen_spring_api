package de.hs.da.hskleinanzeigen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.dto.UserDTO;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.payloads.requests.UserPostRequest;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis_container = new GenericContainer("redis:7.0.12")
            .withExposedPorts(6379);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeAll
    static public void setUpRedis(){
        redis_container.start();
    }

    @BeforeEach
    public void setUp() {
        cacheManager.getCache("users").clear();
        testUser = new User();
        testUser.setEmail("test@web.de");
        testUser.setPassword("password");
        testUser.setFirst_name("first");
        testUser.setLast_name("last");
        testUser.setPhone("12345");
        testUser.setLocation("darmstadt");
        testUser = userRepository.save(testUser);
    }



    @AfterEach
    public void tearDown() throws IOException {
        this.userRepository.deleteAll();
        //cacheManager.getCache("users").clear();
    }



    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostUser_success() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setEmail("email@web.de");
        userPostRequest.setPassword("123456");
        userPostRequest.setFirstName("Frodo");
        userPostRequest.setLastName("Beutlin");
        userPostRequest.setPhone("4321");
        userPostRequest.setLocation("Auenland");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userPostRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostUser_error_payload_incomplete() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setEmail("email@web.de");
        userPostRequest.setPassword("123456");
        userPostRequest.setFirstName("Frodo");
        userPostRequest.setLastName("Beutlin");
        userPostRequest.setPhone("4321");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userPostRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testPostUser_error_already_exists() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setEmail("test@web.de");
        userPostRequest.setPassword("password");
        userPostRequest.setFirstName("first");
        userPostRequest.setLastName("last");
        userPostRequest.setPhone("12345");
        userPostRequest.setLocation("darmstadt");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userPostRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUsers_success() throws Exception {
        userRepository.save(testUser);

        mockMvc.perform(get("/api/users")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").isNumber())
                .andExpect(jsonPath("content[0].id").value(testUser.getId()))
                .andExpect(jsonPath("content[0].email").value(testUser.getEmail()))
                .andExpect(jsonPath("content[0].firstName").value(testUser.getFirst_name()))
                .andExpect(jsonPath("content[0].lastName").value(testUser.getLast_name()))
                .andExpect(jsonPath("content[0].phone").value(testUser.getPhone()))
                .andExpect(jsonPath("content[0].location").value(testUser.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUsers_no_user_found() throws Exception {
        mockMvc.perform(get("/api/users")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUser_success() throws Exception {
        userRepository.save(testUser);

        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("email").value(testUser.getEmail()))
                .andExpect(jsonPath("firstName").value(testUser.getFirst_name()))
                .andExpect(jsonPath("lastName").value(testUser.getLast_name()))
                .andExpect(jsonPath("phone").value(testUser.getPhone()))
                .andExpect(jsonPath("location").value(testUser.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUser_error_user_not_found() throws Exception {
        mockMvc.perform(get("/api/users/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCache() throws Exception {


        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk());

        Cache userCache = cacheManager.getCache("users");

        int userId = userCache.get(testUser.getId(), UserDTO.class).getId();
        Assert.assertEquals(userId, testUser.getId());
    }

















}