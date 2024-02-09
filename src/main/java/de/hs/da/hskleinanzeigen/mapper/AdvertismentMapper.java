package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.AdvertismentDTO;
import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.payloads.requests.AdvertisementPostRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class})
public interface AdvertismentMapper {
    Advertisement toAdvertisement(AdvertisementPostRequest advertisementPostRequest);

    AdvertismentDTO toAdvertismentDTO(Advertisement advertisement);
}
