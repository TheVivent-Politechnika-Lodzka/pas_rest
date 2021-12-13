package pl.ias.pas.hotelroom.pasrest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    private UUID id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private UserType userType;
    private boolean isActive = true;

    // nadawanie id to odpowiedzialność repozytorium
    // typ użytkownika to nadaje się potem (kto przy rejestracji od razu wybiera typ użytkownika? xD)
    public User(UUID id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.userType = UserType.CLIENT;
    }

    // TODO ogarnąć equals i hashCode (najlepiej żeby lombok się tym zajął, ale uwzględniał tylko uuid)

}
