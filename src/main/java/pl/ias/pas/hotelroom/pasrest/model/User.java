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
    private boolean isActive = true;
    private UserType userType;

//    public User() {
//        this.isActive = true;
//    }
//
    // nadawanie id to odpowiedzialność repozytorium
    // typ użytkownika to nadaje się potem (kto przy rejestracji od razu wybiera typ użytkownika? xD)
    public User(UUID id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.userType = UserType.CLIENT;
//        this.isActive = true;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(id, user.id) && Objects.equals(login, user.login);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, login);
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", login='" + login + '\'' +
//                ", password='" + password + '\'' +
//                ", name='" + name + '\'' +
//                ", surname='" + surname + '\'' +
//                ", isActive=" + isActive +
//                ", userType=" + userType +
//                '}';
//    }

//    public UUID getId() {
//        return id;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    public UserType getUserType() {
//        return userType;
//    }
//
//    public void setUserType(UserType userType) {
//        // TODO sprawdzenie czy użytkownik jest aktywny
//        this.userType = userType;
//    }
//
//    public String getLogin() {
//        return login;
//    }
//
//    public void setLogin(String login) {
//        // TODO sprawdzenie czy użytkownik jest aktywny
//        // TODO walidacja czy login jest poprawny
//        this.login = login;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        // TODO sprawdzenie czy użytkownik jest aktywny
//        // TODO walidacja czy hasło jest poprawne (len>0)
//        this.password = password;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        // TODO sprawdzenie czy użytkownik jest aktywny
//        // TODO walidacja czy imię jest poprawne (zaczyna się z wielkiej litery, jedno słowo itp)
//        this.name = name;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        // TODO sprawdzenie czy użytkownik jest aktywny
//        // TODO walidacja czy nazwisko jest poprawne (zaczyna się z wielkiej litery, jedno słowo itp)
//        this.surname = surname;
//    }
}
