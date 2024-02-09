package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.CategoryDTO;
import de.hs.da.hskleinanzeigen.entities.Category;
import de.hs.da.hskleinanzeigen.payloads.requests.CategoryPostRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryPostRequest categoryPostRequest);
    CategoryDTO toCategoryDTO(Category category);
}
