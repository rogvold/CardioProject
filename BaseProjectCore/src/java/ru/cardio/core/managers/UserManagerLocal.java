/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.managers;

import javax.ejb.Local;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.jpa.entity.User;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {
    
    
    public User getUserById(Long userId);
    public User getUserByEmail(String email);
    
    public User registerNewUser(String email,  String password, String firstName, String  lastName);
    
    public User logInByEmail(String email, String password);
    
    public boolean userExistsByEmail(String email);
    public boolean userExistsById(Long userId);
    
    public boolean checkAuData(Long userId, String password);
    public boolean checkAuthorisationData(String email, String password);

    public boolean checkEmailAndLogin(String email, String password) throws Exception;
    
    
    public CardioSession getLastCardioSession(Long userId);
    
}
