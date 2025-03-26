package microservices.SpringBootApp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import microservices.SpringBootApp.model.User;
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

    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Transactional
    public void update(long id, User new_user) {
        User user = findById(id);
        user.setFirstName(new_user.getFirstName());
        user.setLastName(new_user.getLastName());
        user.setEmail(new_user.getEmail());
        entityManager.merge(user);
    }

    @Transactional
    public void delete(long id) {
        User user = entityManager.find(User.class, id);
        entityManager.persist(user);
        entityManager.remove(user);
    }
}
