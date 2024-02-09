package de.hs.da.hskleinanzeigen.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CategoryDTO {
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
