package ru.itmentor.spring.boot_security.demo.services;

import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role findById(long id);
}
