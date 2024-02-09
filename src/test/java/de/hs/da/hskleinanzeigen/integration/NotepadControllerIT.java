package de.hs.da.hskleinanzeigen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.entities.*;
import de.hs.da.hskleinanzeigen.payloads.requests.NotepadPutRequest;
import de.hs.da.hskleinanzeigen.repositories.AdvertisementRepository;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import de.hs.da.hskleinanzeigen.repositories.NotepadRepository;
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

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotepadControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AdvertisementRepository advertisementRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private NotepadRepository notepadRepository;

  private User testUser;
  private Advertisement testAd;
  private Notepad testNotepad;

  @BeforeEach
  public void setup() {

    testUser = new User();
    testUser.setEmail("test1@example.com");
    testUser.setPassword("password");
    testUser.setFirst_name("John");
    testUser.setLast_name("Doe");
    testUser = userRepository.save(testUser);

    Category testCategory = new Category();
    testCategory.setName("Category_1");
    testCategory = categoryRepository.save(testCategory);

    testAd = new Advertisement();
    testAd.setType(Type.OFFER);
    testAd.setTitle("Title of Advertisement");
    testAd.setCategory(testCategory);
    testAd.setUser(testUser);
    testAd.setDescription("Description of Advertisement");
    testAd.setId(1234);
    testAd.setCreated(new Timestamp(System.currentTimeMillis()));
    testAd = advertisementRepository.save(testAd);

    testNotepad = new Notepad();
    testNotepad.setUser(testUser);
    testNotepad.setAdvertisement(testAd);
    testNotepad.setNote("Sample note for notepad");
    testNotepad = notepadRepository.save(testNotepad);

  }
  @AfterEach
  public void tearDown() {
    this.notepadRepository.deleteAll();
    this.advertisementRepository.deleteAll();
    this.userRepository.deleteAll();
    this.categoryRepository.deleteAll();

  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testAddNotepad_success() throws Exception {
    NotepadPutRequest putRequest = new NotepadPutRequest();
    putRequest.setNote("Integration Test Note");
    putRequest.setAdvertisementId(testAd.getId());

    mockMvc.perform(put("/api/users/" + testUser.getId() + "/notepad")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(putRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").isNumber());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testAddNotepad_advertisement_not_found() throws Exception {
    NotepadPutRequest putRequest = new NotepadPutRequest();
    putRequest.setNote("Integration Test Note");
    putRequest.setAdvertisementId(testAd.getId()-1);    // wrong id

    mockMvc.perform(put("/api/users/" + testUser.getId() + "/notepad")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(putRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testAddNotepad_user_not_found() throws Exception {
    NotepadPutRequest putRequest = new NotepadPutRequest();
    User fakeUser = new User();
    fakeUser.setId(testUser.getId()+1); // wrong id
    putRequest.setNote("Integration Test Note");
    putRequest.setAdvertisementId(testAd.getId());

    mockMvc.perform(put("/api/users/" + fakeUser.getId() + "/notepad")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(putRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testAddNotepad_incomplete_request() throws Exception {
    NotepadPutRequest putRequest = new NotepadPutRequest();
    //Remove setNote
    putRequest.setAdvertisementId(testAd.getId());

    mockMvc.perform(put("/api/users/" + testUser.getId() + "/notepad")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(putRequest)))
        .andExpect(status().isBadRequest());
  }


  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testGetNotepad_success() throws Exception {
    mockMvc.perform(get("/api/users/" + testUser.getId() + "/notepad"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].note").value(testNotepad.getNote()));
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testGetNotepad_noUser() throws Exception {
    notepadRepository.deleteAll();
    mockMvc.perform(get("/api/users/" + testUser.getId()+1 + "/notepad"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testDeleteNotepadEntry_success() throws Exception {
    mockMvc.perform(delete("/api/users/" + testUser.getId() + "/notepad")
        .param("advertisementId", String.valueOf(testAd.getId())))
      .andExpect(status().isNoContent());
  }
}
