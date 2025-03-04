package ru.itmentor.spring.boot_security.demo.formatters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itmentor.spring.boot_security.demo.dao.RoleDao;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.services.RoleService;

@Component
public class RoleConverter implements Converter<String, Role> {

    private final RoleService roleService;

    @Autowired
    public RoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role convert(String id) {
        return roleService.findById(Long.valueOf(id));
    }
}
