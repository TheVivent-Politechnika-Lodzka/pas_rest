package pl.ias.pas.hotelroom.pasrest.managers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.exceptions.PermissionsException;
import pl.ias.pas.hotelroom.pasrest.model.User;
import pl.ias.pas.hotelroom.pasrest.model.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class UserManager {


    @Inject
    private UserDao userDao;
    private UserType accesLevel;

    public UserManager() {
        this.accesLevel = UserType.CLIENT;
    }

    public UserManager(UserType accesLevel) {
        this.accesLevel = accesLevel;
    }

    // TODO zastanowić się, czy rzucanie wyjątków przy wyszukiwaniu jest ok
    // TODO czy nie lepiej zmienić na zwracanie nulla, jeśli nie znajdzie użytkownika
    public User getUserByLogin(String login) throws ApplicationDaoException {
        return userDao.getUserByLogin(login);
    }

    public List<User> searchUsers(String login) throws ApplicationDaoException {
        return userDao.searchUsers(login);
    }

    public List<User> giveArchiveUsers() {
        return userDao.getArchiveUsers();
    }

    public List<User> giveAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(UUID id) throws ApplicationDaoException {
        return userDao.getUserById(id);
    }

    public UUID addUser(User user) throws ApplicationDaoException, PermissionsException {
//        if (accesLevel != UserType.USER_ADMIN) {
//            throw new PermissionsException("403", "You don't have permission to add user");
//        }
        // sprawdzanie nie pustości loginu
        if ("".equals(user.getLogin())) {
            throw new ApplicationDaoException("500", "Login cannot be empty");
        }

        // sprawdzanie unikalności loginu i id
        UUID id = UUID.randomUUID();
        for (User currentUser : giveAllUsers()) {
            if (currentUser.getLogin().equals(user.getLogin())) {
                throw new ApplicationDaoException("500", "User already exists");
            }
            if (currentUser.getId().equals(id)) {
                throw new ApplicationDaoException("500", "ID error, please try again");
            }
        }

        // stworzenie nowego użytkownika
        User newUser = new User(id, user.getLogin(), user.getPassword(), user.getName(), user.getSurname());

        return userDao.addUser(newUser);
    }

    public void removeUser(UUID id) throws ApplicationDaoException, PermissionsException {
//        if (accesLevel != UserType.USER_ADMIN) {
//            throw new PermissionsException("403", "You don't have permission to remove user");
//        }

        User user = userDao.getUserById(id);
        userDao.removeUser(user);
    }

    public void updateUser(User old, User user) throws ApplicationDaoException, PermissionsException {
//        if (accesLevel != UserType.USER_ADMIN) {
//            throw new PermissionsException("403", "You don't have permission to update user");
//        }
        userDao.updateUser(old, user);
    }

}
