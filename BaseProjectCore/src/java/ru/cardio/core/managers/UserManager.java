package ru.cardio.core.managers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.utils.UserUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class UserManager implements UserManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    @Override
    public User getUserById(Long userId) {
        User user;
        if (userId == null) {
            return null;
        }
        user = em.find(User.class, userId);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        Query q = em.createQuery("select u from User u where u.email=:email").setParameter("email", email);
        try {
            return (User) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("getUserByEmail: e = " + e.toString());
            return null;
        }
    }

    @Override
    public User registerNewUser(String email, String password, String firstName, String lastName) {
        if (!UserUtils.isValidEmail(email) || userExistsByEmail(email)) {
            return null;
        }
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        return em.merge(u);
    }

    @Override
    public User logInByEmail(String email, String password) {
        User u = getUserByEmail(email);
        if (u.getPassword().equals(password)) {
            return u;
        } else {
            return null;
        }
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return (getUserByEmail(email) == null) ? false : true;
    }

    @Override
    public boolean userExistsById(Long userId) {
        return (getUserById(userId) == null) ? false : true;
    }

    @Override
    public CardioSession getLastCardioSession(Long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean checkAuData(Long userId, String password) {
        System.out.println("checkAuData: userId = " + userId + " / password = " + password);

        User u = em.find(User.class, userId);
        try {
            return u.getPassword().equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkAuthorisationData(String email, String password) {
        User u = getUserByEmail(email);
        try {
            return u.getPassword().equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkEmailAndLogin(String email, String password) throws Exception {
        User u = getUserByEmail(email);
        if (u == null) throw new Exception("user with given email not found");
        if (u.getPassword().equals(password)){
            return true;
        }else {
            return false;
        }
    }
}
