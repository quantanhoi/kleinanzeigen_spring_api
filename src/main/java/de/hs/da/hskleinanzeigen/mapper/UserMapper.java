package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.UserDTO;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.payloads.requests.UserPostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "first_name", target = "firstName")
    @Mapping(source = "last_name", target = "lastName")
    UserDTO toUserDTO(User user);

    @Mapping(source = "firstName", target = "first_name")
    @Mapping(source = "lastName", target = "last_name")
    User toUser(UserPostRequest userPostRequest);


}


