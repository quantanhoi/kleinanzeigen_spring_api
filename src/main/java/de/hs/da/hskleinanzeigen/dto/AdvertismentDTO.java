package de.hs.da.hskleinanzeigen.dto;

import de.hs.da.hskleinanzeigen.entities.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class AdvertismentDTO {
    @EqualsAndHashCode.Include
    private int id;
    private Type type;

    private CategoryDTO category;

    private String title;
    private String description;
    private int price;
    private String location;

    private UserDTO user;

}
