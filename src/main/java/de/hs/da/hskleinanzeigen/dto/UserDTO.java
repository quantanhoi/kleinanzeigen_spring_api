package de.hs.da.hskleinanzeigen.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserDTO {
    @EqualsAndHashCode.Include
    private int id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String location;
}
