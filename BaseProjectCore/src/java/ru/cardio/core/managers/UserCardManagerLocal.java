
package ru.cardio.core.managers;

import javax.ejb.Local;
import ru.cardio.core.jpa.entity.UserCard;

/**
 *
 * @author rogvold
 */
@Local
public interface UserCardManagerLocal {
    
        public UserCard getCardByUserId(Long userId);
        
        public UserCard updateUserCard(UserCard card , Long userId);
}
