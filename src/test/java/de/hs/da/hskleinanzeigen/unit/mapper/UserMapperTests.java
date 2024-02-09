package de.hs.da.hskleinanzeigen.unit.mapper;
import de.hs.da.hskleinanzeigen.dto.UserDTO;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.mapper.UserMapper;
import de.hs.da.hskleinanzeigen.mapper.UserMapperImpl;
import de.hs.da.hskleinanzeigen.payloads.requests.UserPostRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class UserMapperTests {

    private final UserMapper userMapper = new UserMapperImpl();
    @Test
    public void testToUserDTO_with_correct_User() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setLast_name("last_name");
        user.setFirst_name("first_name");

        UserDTO result = userMapper.toUserDTO(user);

        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("last_name", result.getLastName());
        assertEquals("first_name", result.getFirstName());
    }

    @Test
    public void testToUserDTO_with_null_User() {

        UserDTO result = userMapper.toUserDTO(null);

        assertNull(result);
    }

    @Test
    public void testToUser_with_correct_UserPostRequest() {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setFirstName("first_name");
        userPostRequest.setLastName("last_name");
        userPostRequest.setEmail("test@gmail.com");
        userPostRequest.setPassword("password");

        User result = userMapper.toUser(userPostRequest);

        assertEquals("first_name", result.getFirst_name());
        assertEquals("last_name", result.getLast_name());
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void testToUser_with_null_UserPostRequest() {

        User result = userMapper.toUser(null);

        assertNull(result);
    }
}
