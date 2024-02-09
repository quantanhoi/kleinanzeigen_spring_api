package de.hs.da.hskleinanzeigen.payloads.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class NotepadPutRequest {
    @EqualsAndHashCode.Include
    private  int advertisementId;
    private  String note;

}
