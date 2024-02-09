package de.hs.da.hskleinanzeigen.controllers;

import de.hs.da.hskleinanzeigen.dto.NotepadDTO;
import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.entities.Notepad;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.exceptions.AdvertisementBadRequestException;
import de.hs.da.hskleinanzeigen.exceptions.UserBadRequestException;
import de.hs.da.hskleinanzeigen.exceptions.UserNotFoundException;
import de.hs.da.hskleinanzeigen.mapper.NotepadMapper;
import de.hs.da.hskleinanzeigen.payloads.requests.NotepadPutRequest;
import de.hs.da.hskleinanzeigen.payloads.responds.NotepadPutRespond;
import de.hs.da.hskleinanzeigen.repositories.AdvertisementRepository;
import de.hs.da.hskleinanzeigen.repositories.NotepadRepository;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@Tag(name = "Notepad", description = "Read and set Notepad of the User")
@Secured({"USER", "ADMIN"})
public class NotepadController {
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final NotepadRepository notepadRepository;
    private final NotepadMapper notepadMapper;

    public NotepadController(UserRepository userRepository, AdvertisementRepository advertisementRepository, NotepadRepository notepadRepository, NotepadMapper notepadMapper) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.notepadRepository = notepadRepository;
        this.notepadMapper = notepadMapper;
    }

    @PutMapping(path = "/users/{userId}/notepad")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Inserts the AD to the Notepad of a specific User by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Error: Payload incomplete, User or Advertisement not found")
    })
    public NotepadPutRespond addNotepad(
            @Parameter(description = "Id of the user", example = "1", required = true)
            @PathVariable final int userId, @RequestBody NotepadPutRequest notepadPutRequest) {
        boolean incompleteRequest = Stream.of(
                notepadPutRequest.getNote(),
                notepadPutRequest.getAdvertisementId()
        ).anyMatch(Objects::isNull);
        if (incompleteRequest) {
            throw new AdvertisementBadRequestException();
        }
        Optional<Advertisement> advertisement = this.advertisementRepository.findById(notepadPutRequest.getAdvertisementId());
        if (advertisement.isEmpty()) {
            throw new AdvertisementBadRequestException();
        }
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserBadRequestException();
        }
        Notepad newNotepad = notepadMapper.toNodepad(notepadPutRequest);
        newNotepad.setUser(user.get());
        newNotepad.setAdvertisement(advertisement.get());
        Notepad persistedNotepad = notepadRepository.save(newNotepad);
        return notepadMapper.toNotepadPutRespond(persistedNotepad);
    }

    @GetMapping(path = "/users/{userId}/notepad")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns the Notepad of ia specific User by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "No Entry found"),
            @ApiResponse(responseCode = "404", description = "No User found")
    })
    public List<NotepadDTO> getNotepad(
            @Parameter(description = "Id of the user", example = "1", required = true)
            @PathVariable final int userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<NotepadDTO> respond  = new ArrayList<>();

        for (Notepad notepad : user.get().getNotepads()) {
            respond.add(notepadMapper.toNotepadDTO(notepad));
        }
        return respond;
    }

    @DeleteMapping("/users/{userId}/notepad")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 status code
    @Operation(summary = "Delete an Notepad Entry of a User specified by the userId and advertisementId")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Advertisement deleted"),
    })
    public void deleteNotepadEntry(
            @Parameter(description = "Id of the User",example = "1", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Id of the Advertisement",example = "1")
            @RequestParam Long advertisementId) {
        notepadRepository.deleteByUserIdAndAdvertisementId(userId, advertisementId);
    }
}
