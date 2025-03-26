package ru.itmentor.spring.boot_security.demo.dao;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void add(User user) {
        entityManager.persist(user);
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public User findByEmail(String email) {
        return entityManager
                .createQuery("Select u from User u " +
                        "join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Transactional
    public void update(long id, User new_user) {
        User user = findById(id);
        if(new_user.getFirstName() != null && !new_user.getFirstName().isEmpty())
            user.setFirstName(new_user.getFirstName());
        if(new_user.getLastName() != null && !new_user.getLastName().isEmpty())
            user.setLastName(new_user.getLastName());
        if(new_user.getEmail() != null && !new_user.getEmail().isEmpty())
            user.setEmail(new_user.getEmail());
        if (new_user.getPassword() != null)
            user.setPassword(new_user.getPassword());
        if(new_user.getRoles() != null)
            user.setRoles(new_user.getRoles());
        entityManager.merge(user);
    }

    @Transactional
    public void delete(long id) {
        User user = entityManager.find(User.class, id);
        entityManager.persist(user);
        entityManager.remove(user);
    }
}
