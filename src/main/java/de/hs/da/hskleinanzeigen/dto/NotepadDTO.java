package de.hs.da.hskleinanzeigen.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class NotepadDTO {
    @EqualsAndHashCode.Include
    private int id;
    private String note;
    private AdvertismentDTO advertisement;
}
