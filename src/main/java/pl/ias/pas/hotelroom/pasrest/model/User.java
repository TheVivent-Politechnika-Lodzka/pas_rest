package pl.ias.pas.hotelroom.pasrest.model;

import java.util.UUID;

public class User {

    private UUID id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private boolean isActive;
    private UserType userType;

    public User() {
    }

    // nadawanie id to odpowiedzialność managera
    public User(UUID id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.userType = UserType.CLIENT;
    }

    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        // TODO sprawdzenie czy użytkownik jest aktywny
        this.userType = userType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        // TODO sprawdzenie czy użytkownik jest aktywny
        // TODO walidacja czy login jest poprawny
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // TODO sprawdzenie czy użytkownik jest aktywny
        // TODO walidacja czy hasło jest poprawne (len>0)
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // TODO sprawdzenie czy użytkownik jest aktywny
        // TODO walidacja czy imię jest poprawne (zaczyna się z wielkiej litery, jedno słowo itp)
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        // TODO sprawdzenie czy użytkownik jest aktywny
        // TODO walidacja czy nazwisko jest poprawne (zaczyna się z wielkiej litery, jedno słowo itp)
        this.surname = surname;
    }
}
