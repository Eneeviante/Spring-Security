package microservices.SpringBootApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import microservices.SpringBootApp.dao.UserDao;
import microservices.SpringBootApp.model.User;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    public User findById(Long id){
        return userDao.findById(id);
    }

    public void update(long id, User user){
        userDao.update(id, user);
    }

    public void delete(long id){
        userDao.delete(id);
    }
}