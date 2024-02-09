package de.hs.da.hskleinanzeigen.controllers;

import de.hs.da.hskleinanzeigen.dto.AdvertismentDTO;
import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.entities.Type;
import de.hs.da.hskleinanzeigen.entities.User;
import de.hs.da.hskleinanzeigen.exceptions.AdvertisementBadRequestException;
import de.hs.da.hskleinanzeigen.exceptions.AdvertisementNotFoundException;
import de.hs.da.hskleinanzeigen.mapper.AdvertismentMapper;
import de.hs.da.hskleinanzeigen.payloads.requests.AdvertisementPostRequest;
import de.hs.da.hskleinanzeigen.repositories.AdvertisementRepository;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import de.hs.da.hskleinanzeigen.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@Tag(name = "Advertisement", description = "Creates an Advertisement, reads all Advertisements and reads a specific Advertisement by Id")
@Secured({"USER", "ADMIN"})
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AdvertismentMapper advertismentMapper;

    @Autowired
    public AdvertisementController(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository, UserRepository userRepository, AdvertismentMapper advertismentMapper) {
        this.advertisementRepository = advertisementRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.advertismentMapper = advertismentMapper;
    }

    @PostMapping(path = "/advertisements")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates Advertisement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Error: Payload incomplete"),
            @ApiResponse(responseCode = "400", description = "Error: Category or User not Found")
    })
    public AdvertismentDTO addAdvertisement(@RequestBody AdvertisementPostRequest advertisementRequestBody) {
        int categoryId = advertisementRequestBody.getCategoryId();
        int userId = advertisementRequestBody.getUserId();

        Optional<Category> category = categoryRepository.findById(categoryId);
        Optional<User> user = userRepository.findById(userId);

        if (!category.isPresent() || !user.isPresent()) {
            throw new AdvertisementBadRequestException();
        }

        boolean incompleteRequest = Stream.of(
                        advertisementRequestBody.getType(),
                        advertisementRequestBody.getDescription(),
                        advertisementRequestBody.getTitle(),
                        advertisementRequestBody.getLocation(),
                        advertisementRequestBody.getPrice())
                .anyMatch(Objects::isNull);
        if(incompleteRequest){
            throw new AdvertisementBadRequestException();
        }
        Advertisement advertisement = advertismentMapper.toAdvertisement(advertisementRequestBody);
        advertisement.setCategory(category.get());
        advertisement.setUser(user.get());
        advertisement = advertisementRepository.save(advertisement);

        return advertismentMapper.toAdvertismentDTO(advertisement);
    }


    @GetMapping(produces = "application/json", path = "/advertisements/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reads a specific Advertisement by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Error: Advertisement not found")
    })
    public AdvertismentDTO getAdvertisement(
            @Parameter(description = "Id of the Advertisement", example = "1", required = true)
            @PathVariable final int id) {
        Advertisement ad = advertisementRepository.findById(id).orElseThrow(() -> new AdvertisementNotFoundException());
        return advertismentMapper.toAdvertismentDTO(ad);
    }

    @GetMapping("/advertisements")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reads all Advertisements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok"),
    })
    public Iterable<AdvertismentDTO> getAdvertisements(
            @Parameter(description = "Page Number",example = "10")
            @RequestParam(value="page") int page,
            @Parameter(description = "Number of Advertisements per page",example = "10")
            @RequestParam(value="size") int size,
            @Parameter(description = "Type of the Advertisement",example = "OFFER")
            @RequestParam(value="type", required = false) String type,
            @Parameter(description = "Id of the Category",example = "1")
            @RequestParam(value="categoryId", required = false) Integer categoryId,
            @Parameter(description = "Filter for Price to",example = "100")
            @RequestParam(value="priceFrom", required = false) Integer priceFrom,
            @Parameter(description = "Filter for Price to",example = "100")
            @RequestParam(value="priceTo", required = false) Integer priceTo
    ) {
        Type typeEnum = null;
        final int DEFAULT_PRICE_FROM = 0;
        final int DEFAULT_PRICE_TO = Integer.MAX_VALUE;

        if (type != null) {
            try {
                typeEnum = Type.valueOf(type);
            } catch (IllegalArgumentException e) {
                throw new AdvertisementBadRequestException();
            }
        }
        if (priceFrom == null)
            priceFrom = DEFAULT_PRICE_FROM;
        if (priceTo == null)
            priceTo = DEFAULT_PRICE_TO;

        Pageable pageable = PageRequest.of(page, size, Sort.by("created").ascending());
        Page<Advertisement> d = this.advertisementRepository.findByTypeAndCategoryAndPriceBetween(typeEnum, categoryId, priceFrom, priceTo, pageable);


        Page<AdvertismentDTO> respond = new PageImpl<>(d.stream().map(ad -> advertismentMapper.toAdvertismentDTO(ad)).toList());


        return respond;
    }
}
