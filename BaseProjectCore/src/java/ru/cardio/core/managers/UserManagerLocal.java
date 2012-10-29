/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.managers;

import javax.ejb.Local;
import ru.cardio.core.entity.CardioSession;
import ru.cardio.core.entity.User;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {
    
    public User getUserById(Long userId);
    public User getUserByEmail(String email);
    public User getUserByLogin(String login);
    
    public User registerNewUser(String email, String login, String password);
    public User logInByEmail(String email, String password);
    public User logInByLogin(String login,String password);
    
    public boolean userExistsByEmail(String email);
    public boolean userExistsByLogin(String login);
    
    public CardioSession getLastCardioSession(Long userId);
    
}
