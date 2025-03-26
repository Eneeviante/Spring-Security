package ru.itmentor.spring.boot_security.demo.dao;

import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface RoleDao {
    List<Role> getAll();
    Role findById(long id);
}
