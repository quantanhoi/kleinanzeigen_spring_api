package de.hs.da.hskleinanzeigen.controllers;

import de.hs.da.hskleinanzeigen.dto.UserDTO;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.exceptions.UserBadRequestException;
import de.hs.da.hskleinanzeigen.exceptions.UserConflictException;
import de.hs.da.hskleinanzeigen.exceptions.UserNotFoundException;
import de.hs.da.hskleinanzeigen.mapper.UserMapper;
import de.hs.da.hskleinanzeigen.payloads.requests.UserPostRequest;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@Tag(name = "User", description = "Register User, read all User and read User by Id")
@Secured({"USER", "ADMIN"})
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Error: Payload incomplete"),
            @ApiResponse(responseCode = "409", description = "Error: E-Mail Adress is already registered")
    })
    @CachePut(value = "users", key = "#result.id")
    public UserDTO postUser(@Valid @RequestBody UserPostRequest userPostRequest) {
        boolean incompleteRequest = Stream.of(userPostRequest.getEmail(), userPostRequest.getPassword(),
                userPostRequest.getFirstName(), userPostRequest.getLastName(), userPostRequest.getPhone(),
                userPostRequest.getLocation()).anyMatch(Objects::isNull);
        if (incompleteRequest) {
            throw new UserBadRequestException();
        }
        Optional<User> user = this.userRepository.findUserByEmail(userPostRequest.getEmail());
        if (user.isPresent()) {
            throw new UserConflictException();
        }

        User newUser = userMapper.toUser(userPostRequest);

        this.userRepository.save(newUser);
        return userMapper.toUserDTO(newUser);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Read all Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No User found")
    })
    public Iterable<UserDTO> getUsers(
            @Parameter(description = "Page Number",example = "10")
            @RequestParam(value="page") int page,
            @Parameter(description = "Number of Advertisements per page",example = "10")
            @RequestParam(value="size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").ascending());
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> respond = new PageImpl<>(users.stream().map(u -> userMapper.toUserDTO(u)).toList());
        return respond;
    }


    @GetMapping(produces = "application/json", path = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Read a specific User by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Error: User not found")
    })
    @Cacheable(value = "users", key = "#id")
    public UserDTO getUser(
            @Parameter(description = "Id of the user", example = "1", required = true)

            @PathVariable final int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        return userMapper.toUserDTO(user);
    }
}