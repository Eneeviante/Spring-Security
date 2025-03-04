package ru.itmentor.spring.boot_security.demo.services;

import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    void add(User user);
    User findById(Long id);
    User findByEmail(String email);
    List<User> getAll();
    void update(long id, User user);
    void delete(long id);
}
