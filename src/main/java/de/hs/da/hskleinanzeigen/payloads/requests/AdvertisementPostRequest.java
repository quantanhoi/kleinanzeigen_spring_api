package de.hs.da.hskleinanzeigen.payloads.requests;


import de.hs.da.hskleinanzeigen.entities.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class AdvertisementPostRequest {
    private Type type;
    private int categoryId;
    private String title;
    @EqualsAndHashCode.Include
    private String description;
    private int price;
    private String location;
    private int userId;

}
