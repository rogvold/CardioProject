package ru.cardio.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.jpa.entity.Subscription;
import ru.cardio.exceptions.SubscriptionException;

/**
 *
 * @author rogvold
 */
@Local
public interface SubscriptionManagerLocal {
    
    public boolean subscriptionExists(String email);
    
    public void saveSubscription(String email) throws SubscriptionException;
    
    public List<Subscription> getAllSubscriptions();
}
