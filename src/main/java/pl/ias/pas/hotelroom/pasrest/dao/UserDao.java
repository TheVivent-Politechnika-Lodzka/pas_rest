package pl.ias.pas.hotelroom.pasrest.dao;

import jakarta.enterprise.context.ApplicationScoped;
import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class UserDao {

    private ArrayList<User> usersRepository = new ArrayList<>();

    synchronized public UUID addUser(User user) throws ApplicationDaoException {
        // sprawdzanie nie pustości loginu
        if ("".equals(user.getLogin())) {
            throw new ApplicationDaoException("500", "Login cannot be empty");
        }

        // sprawdzanie unikalności loginu i id
        UUID id = UUID.randomUUID();
        for (User currentUser : usersRepository) {
            if (currentUser.getLogin().equals(user.getLogin())) {
                throw new ApplicationDaoException("500", "User already exists");
            }
            if (currentUser.getId().equals(id)) {
                throw new ApplicationDaoException("500", "ID error, please try again");
            }
        }

        // wstawienie nowego użytkownika
        User newUser = new User(id, user.getLogin(), user.getPassword(), user.getName(), user.getSurname());
        usersRepository.add(newUser);
        return id;
    }

    public void updateUser(User user) throws ApplicationDaoException {
        if (!usersRepository.contains(user)) {
            throw new ApplicationDaoException("500", "User does not exist");
        }

        User oldUser = getUserById(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setPassword(user.getPassword());
        oldUser.setName(user.getName());
        oldUser.setSurname(user.getSurname());
    }

    synchronized public void removeUser(User user) throws ApplicationDaoException {
        if (!usersRepository.contains(user)) {
            throw new ApplicationDaoException("500", "User does not exist");
        }

        boolean removedUser = usersRepository.remove(user);

        if (!removedUser) {
            throw new ApplicationDaoException("500", "User could not be removed, please try again");
        }
    }

    synchronized public User getUserById(UUID id) throws ApplicationDaoException {
        for (User user : usersRepository) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new ApplicationDaoException("500", "User does not exist");
    }

    synchronized public User getUserByLogin(String login) throws ApplicationDaoException {
        for (User user : usersRepository) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        throw new ApplicationDaoException("500", "User does not exist");
    }

    synchronized public List<User> searchUsers(String login) {
        ArrayList<User> result = new ArrayList<>();
        String searchLogin = login.toLowerCase(Locale.ROOT);
        for (User user : usersRepository) {
            String currentLogin = user.getLogin().toLowerCase(Locale.ROOT);
            if (currentLogin.contains(searchLogin)) {
                result.add(user);
            }
        }
        return result;
    }

}
