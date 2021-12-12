package pl.ias.pas.hotelroom.pasrest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {

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

}
