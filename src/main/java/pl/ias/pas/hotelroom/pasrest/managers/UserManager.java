package pl.ias.pas.hotelroom.pasrest.managers;


import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.exceptions.ResourceNotFoundException;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class UserManager {


    @Inject
    private UserDao userDao;

    public UserManager() {
    }

    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    public List<User> searchUsers(String login) {
        return userDao.customSearch(
                (user) -> user.getLogin().toLowerCase().contains(login.toLowerCase())
        );
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public List<User> getAllActiveUsers() {
        return userDao.customSearch((user) -> user.isActive());
    }

    public List<User> getAllArchivedUsers() {
        return userDao.customSearch((user) -> !user.isActive());
    }

    public User getUserById(UUID id, boolean includeArchived) {
        User user = userDao.getUserById(id);

        if (!includeArchived && !user.isActive()) {
            throw new ResourceNotFoundException("User does not exist");
        }

        return user;
    }

    public UUID addUser(User user) {
        return userDao.addUser(user);
    }

    public void deactivateUser(UUID id) {
        userDao.deactivateUser(id);
    }

    public void activateUser(UUID id) {
        userDao.activateUser(id);
    }

    public void updateUser(UUID userToUpdate, User update) {

        if (update.getLogin() != null) {
            update.validateLogin();
        }
        if (update.getPassword() != null) {
            update.validatePassword();
        }
        if (update.getName() != null) {
            update.validateName();
        }
        if (update.getSurname() != null) {
            update.validateSurname();
        }

        userDao.updateUser(userToUpdate, update);
    }

}
