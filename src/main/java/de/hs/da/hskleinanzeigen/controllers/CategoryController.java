package de.hs.da.hskleinanzeigen.controllers;

import de.hs.da.hskleinanzeigen.dto.CategoryDTO;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.exceptions.CategoryBadRequestException;
import de.hs.da.hskleinanzeigen.exceptions.CategoryConflictException;
import de.hs.da.hskleinanzeigen.mapper.CategoryMapper;
import de.hs.da.hskleinanzeigen.payloads.requests.CategoryPostRequest;
import de.hs.da.hskleinanzeigen.repositories.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Category", description = "Creates a Category")
@Secured({"USER", "ADMIN"})
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping(path = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a Category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Error: Payload incomplete"),
            @ApiResponse(responseCode = "409", description = "Error: Category already exists")
    })
    public CategoryDTO postCategory(@RequestBody CategoryPostRequest categoryPostRequest) {
        if (categoryPostRequest.getName() == null) {
            throw new CategoryBadRequestException();
        }
        Optional<Category> category = this.categoryRepository.findOneByName(categoryPostRequest.getName());
        if (category.isPresent()) {
            throw new CategoryConflictException();
        }
        Category newCategory = categoryMapper.toCategory(categoryPostRequest);
        this.categoryRepository.save(newCategory);
        return categoryMapper.toCategoryDTO(newCategory);
    }
}
