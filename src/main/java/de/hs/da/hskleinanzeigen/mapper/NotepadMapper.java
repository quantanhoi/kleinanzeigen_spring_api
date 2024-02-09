package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.NotepadDTO;
import de.hs.da.hskleinanzeigen.entities.Notepad;
import de.hs.da.hskleinanzeigen.payloads.requests.NotepadPutRequest;
import de.hs.da.hskleinanzeigen.payloads.responds.NotepadPutRespond;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AdvertismentMapper.class)
public interface NotepadMapper {
    Notepad toNodepad(NotepadPutRequest notepadPutRequest);
    NotepadDTO toNotepadDTO(Notepad notepad);

    NotepadPutRespond toNotepadPutRespond(Notepad notepad);

}
