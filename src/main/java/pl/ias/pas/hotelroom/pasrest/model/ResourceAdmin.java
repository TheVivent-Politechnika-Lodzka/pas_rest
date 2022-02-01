package pl.ias.pas.hotelroom.pasrest.model;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class ResourceAdmin extends User<ResourceAdmin> {

    public ResourceAdmin(UUID id, String login, String password, String name, String surname) {
        super(id, login, password, name, surname);
    }

    public ResourceAdmin(ResourceAdmin user) {
        super(user);
    }

    @Override
    public ResourceAdmin copy() {
        return new ResourceAdmin(this);
    }

    @Override
    public String getPermissionLevel() {
        return "RESOURCE_ADMIN";
    }

}
