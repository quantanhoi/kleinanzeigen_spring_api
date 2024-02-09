package de.hs.da.hskleinanzeigen.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserPostRequest {

    @NotNull
    @Email(message = "Ungültige E-Mail-Adresse")
    @EqualsAndHashCode.Include
    private String email;

    @NotNull
    @Size(min = 6, message = "Das Passwort muss mindestens 6 Zeichen lang sein")
    private String password;

    @NotNull
    @Size(max = 255, message = "Der Vorname darf maximal 255 Zeichen lang sein")
    private String firstName;

    @NotNull
    @Size(max = 255, message = "Der Nachname darf maximal 255 Zeichen lang sein")
    private String lastName;
    private String phone;
    private String location;

}
