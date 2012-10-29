/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.managers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.entity.CardioSession;
import ru.cardio.core.entity.User;

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
    public User getUserByLogin(String login) {
        Query q = em.createQuery("select u from User u where u.email=:login").setParameter("login", login);
        try {
            return (User) q.getSingleResult();
        } catch (Exception e) {
            System.out.println("getUserByLogin: e = " + e.toString());
            return null;
        }
    }

    @Override
    public User registerNewUser(String email, String login, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User logInByEmail(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User logInByLogin(String login, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return (getUserByEmail(email) == null) ? false : true;
    }

    @Override
    public boolean userExistsByLogin(String login) {
        return (getUserByLogin(login) == null) ? false : true;
    }

    @Override
    public CardioSession getLastCardioSession(Long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
