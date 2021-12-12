package pl.ias.pas.hotelroom.pasrest.dao;

import pl.ias.pas.hotelroom.pasrest.exceptions.ApplicationDaoException;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class UserDao {

    private List<User> usersRepository = Collections.synchronizedList(new ArrayList<>());
    private List<User> archiveRepository = Collections.synchronizedList(new ArrayList<>());

    public UUID addUser(User user) {
        usersRepository.add(user);
        return user.getId();
    }

    public void updateUser(User oldUser, User user) {
        if(user.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }
        if(user.getPassword() != null) {
            oldUser.setPassword(user.getPassword());
        }
        if(user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if(user.getSurname() != null) {
            oldUser.setSurname(user.getSurname());
        }
    }

    public void removeUser(User user) {
        archiveRepository.add(user);
        usersRepository.remove(user);
    }

    public User getUserById(UUID id) throws ApplicationDaoException {
        for (User user : usersRepository) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new ApplicationDaoException("500", "User does not exist");
    }

    public User getUserByLogin(String login) throws ApplicationDaoException {
        for (User user : usersRepository) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        throw new ApplicationDaoException("500", "User does not exist");
    }

    public List<User> searchUsers(String login) {
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

    public List<User> getActiveUsers() {
        return usersRepository;
    }

    public List<User> getArchiveUsers() {
        return archiveRepository;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(getActiveUsers());
        allUsers.addAll(getArchiveUsers());
        return allUsers;
    }

}
