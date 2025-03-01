package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.dao.UserDao;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.security.UserDetailsImp;

import java.util.List;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserDao userDao;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        if(user.getId() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.update(id, user);
    }

    public void delete(long id){
        userDao.delete(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImp(user);
    }
}