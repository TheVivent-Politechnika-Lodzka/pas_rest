package pl.ias.pas.hotelroom.pasrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
public class CredentialsData {

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;


}
