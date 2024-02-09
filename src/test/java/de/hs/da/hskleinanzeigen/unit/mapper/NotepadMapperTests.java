package de.hs.da.hskleinanzeigen.unit.mapper;
import de.hs.da.hskleinanzeigen.dto.NotepadDTO;
import de.hs.da.hskleinanzeigen.entities.Notepad;
import de.hs.da.hskleinanzeigen.mapper.NotepadMapper;
import de.hs.da.hskleinanzeigen.mapper.NotepadMapperImpl;
import de.hs.da.hskleinanzeigen.payloads.requests.NotepadPutRequest;
import de.hs.da.hskleinanzeigen.payloads.responds.NotepadPutRespond;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotepadMapperTests {

    private final NotepadMapper notepadMapper = new NotepadMapperImpl();

    @Test
    public void testToNotepad_with_correct_NotepadPutRequest() {
        NotepadPutRequest notepadPutRequest = new NotepadPutRequest();
        notepadPutRequest.setNote("Note of Notepad");

        Notepad result = notepadMapper.toNodepad(notepadPutRequest);

        assertEquals("Note of Notepad", result.getNote());
    }

    @Test
    public void testToNotepad_with_null_NotepadPutRequest() {

        Notepad result = notepadMapper.toNodepad(null);

        assertNull(result);
    }

    @Test
    public void testToNotepadDTO_with_correct_Notepad() {
        Notepad notepad = new Notepad();
        notepad.setNote("Note of Notepad");

        NotepadMapper notepadMapper_mock = mock(NotepadMapper.class);

        NotepadDTO expectNotepadDTO = new NotepadDTO();
        expectNotepadDTO.setNote("Note of Notepad");

        when(notepadMapper_mock.toNotepadDTO(notepad))
                .thenReturn(expectNotepadDTO);

        NotepadDTO result = notepadMapper_mock.toNotepadDTO(notepad);

        assertEquals("Note of Notepad", result.getNote());
    }

    @Test
    public void testToNotepadDTO_with_null_Notepad() {
        NotepadDTO result = notepadMapper.toNotepadDTO(null);

        assertNull(result);
    }

    @Test
    public void testToNotepadPutRespond_with_correct_Notepad() {
        Notepad notepad = new Notepad();
        notepad.setId(123);

        NotepadPutRespond result = notepadMapper.toNotepadPutRespond(notepad);

        assertEquals(123, result.getId());
    }

    @Test
    public void testToNotepadPutRespond_with_null_id_Notepad() {
        Notepad notepad = new Notepad();
        notepad.setId(null);

        NotepadPutRespond result = notepadMapper.toNotepadPutRespond(notepad);

        assertEquals(new NotepadPutRespond(), result);
    }

    @Test
    public void testToNotepadPutRespond_with_null_Notepad() {
        NotepadPutRespond result = notepadMapper.toNotepadPutRespond(null);

        assertNull(result);
    }
}
