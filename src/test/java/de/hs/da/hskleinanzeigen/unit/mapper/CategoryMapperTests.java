package de.hs.da.hskleinanzeigen.unit.mapper;

import de.hs.da.hskleinanzeigen.dto.CategoryDTO;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.mapper.CategoryMapper;
import de.hs.da.hskleinanzeigen.mapper.CategoryMapperImpl;
import de.hs.da.hskleinanzeigen.payloads.requests.CategoryPostRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CategoryMapperTests {

    private final CategoryMapper categoryMapper = new CategoryMapperImpl();
    @Test
    public void testToCategory_with_correct_CategoryPostRequest() {
        CategoryPostRequest request = new CategoryPostRequest();
        request.setName("Test Category");

        Category result = categoryMapper.toCategory(request);

        assertEquals("Test Category", result.getName());
    }

    @Test
    public void testToCategory_with_null_CategoryPostRequest() {

        Category result = categoryMapper.toCategory(null);

        assertNull(result);
    }

    @Test
    public void toCategoryDTO_with_correct_Category() {
        Category request = new Category();
        request.setName("Category Name");

        CategoryDTO result = categoryMapper.toCategoryDTO(request);

        assertEquals("Category Name", result.getName());
    }

    @Test
    public void toCategoryDTO_with_null_Category() {

        CategoryDTO result = categoryMapper.toCategoryDTO(null);

        assertNull(result);
    }
}
