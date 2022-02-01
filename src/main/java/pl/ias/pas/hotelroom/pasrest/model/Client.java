package pl.ias.pas.hotelroom.pasrest.model;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class Client extends User<Client> {

    public Client(UUID id, String login, String password, String name, String surname) {
        super(id, login, password, name, surname);
    }

    public Client(Client user) {
        super(user);
    }

    @Override
    public Client copy() {
        return new Client(this);
    }

    @Override
    public String getPermissionLevel() {
        return "CLIENT";
    }
}
