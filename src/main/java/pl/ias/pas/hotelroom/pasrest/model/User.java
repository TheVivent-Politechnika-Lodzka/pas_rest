package pl.ias.pas.hotelroom.pasrest.model;


import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ias.pas.hotelroom.pasrest.exceptions.ValidationException;
import pl.ias.pas.hotelroom.pasrest.security.JwtSignable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

//@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
abstract public class User<T extends User<T>> implements JwtSignable {

    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "Login must be 3-20 characters long and contain only letters and numbers")
    private String login;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "Password must be 3-20 characters long and contain only letters and numbers")
    private String password;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "First name must be 3-20 characters long and contain only letters")
    private String name;

    @Getter
    @Setter
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "Last name must be 3-20 characters long and contain only letters")
    private String surname;

    @Getter
    @Setter
    private boolean isActive = true;

    @Override
    @JsonIgnore
    public String getPayload() {
        return id.toString();
    }

    public User(UUID id, String login, String password, String name, String surname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.isActive = user.isActive();
    }

    abstract public T copy();

    abstract public String getPermissionLevel();

    public void validateLogin() throws ValidationException {
        if (login.length() < 3 || login.length() > 20)
            throw new ValidationException("Login must be between 3 and 20 characters");
    }

    public void validatePassword() throws ValidationException {
        if (password.length() < 8 || password.length() > 20)
            throw new ValidationException("Password must be between 8 and 20 characters");
    }

    public void validateName() throws ValidationException {
        if (name.length() < 1)
            throw new ValidationException("Name must be minimum 1 character");
    }

    public void validateSurname() throws ValidationException {
        if (surname.length() < 1)
            throw new ValidationException("Surname must be minimum 1 character");
    }


    public void validate() throws ValidationException {
        validateLogin();
        validatePassword();
        validateName();
        validateSurname();
    }

}
