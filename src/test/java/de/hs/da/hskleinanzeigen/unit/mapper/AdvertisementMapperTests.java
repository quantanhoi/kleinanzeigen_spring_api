package de.hs.da.hskleinanzeigen.unit.mapper;

import de.hs.da.hskleinanzeigen.dto.AdvertismentDTO;
import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.mapper.AdvertismentMapper;
import de.hs.da.hskleinanzeigen.mapper.AdvertismentMapperImpl;
import de.hs.da.hskleinanzeigen.payloads.requests.AdvertisementPostRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdvertisementMapperTests {

    private final AdvertismentMapper advertisementMapper = new AdvertismentMapperImpl();

    @Test
    public void testToAdvertisement_with_correct_AdvertisementPostRequest() {
        AdvertisementPostRequest request = new AdvertisementPostRequest();
        request.setTitle("Test Advertisement");

        Advertisement result = advertisementMapper.toAdvertisement(request);

        assertEquals("Test Advertisement", result.getTitle());
    }

    @Test
    public void testToAdvertisement_with_null_AdvertisementPostRequest() {

        Advertisement result = advertisementMapper.toAdvertisement(null);

        assertNull(result);
    }

    @Test
    public void testToAdvertisementDTO_with_correct_Advertisement() {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("Test Advertisement");

        AdvertismentMapper advertisementMapper_mock = mock(AdvertismentMapper.class);

        AdvertismentDTO expectAdvertisementDTO = new AdvertismentDTO();
        expectAdvertisementDTO.setTitle("Test Advertisement");

        when(advertisementMapper_mock.toAdvertismentDTO(advertisement))
                .thenReturn(expectAdvertisementDTO);

        AdvertismentDTO result = advertisementMapper_mock.toAdvertismentDTO(advertisement);

        assertEquals("Test Advertisement", result.getTitle());
    }

    @Test
    public void testToAdvertisementDTO_with_null_Advertisement() {

        AdvertismentDTO result = advertisementMapper.toAdvertismentDTO(null);

        assertNull(result);
    }
}
