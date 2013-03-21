package ru.cardio.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.Subscription;
import ru.cardio.exceptions.SubscriptionException;

/**
 *
 * @author rogvold
 */
@Stateless
public class SubscriptionManager implements SubscriptionManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    @Override
    public void saveSubscription(String email) throws SubscriptionException {
        if (subscriptionExists(email)){
            throw new SubscriptionException("You have already subscribed on our news");
        }
        Subscription s = new Subscription();
        s.setEmail(email);
        em.persist(s);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Subscription> getAllSubscriptions() {
        return em.createQuery("select s from Subscription s order by s.id").getResultList();
    }

    @Override
    public boolean subscriptionExists(String email) {
        Query q = em.createQuery("select s from Subscription s where s.email = :email").setParameter("email", email);
        List<Subscription> l = q.getResultList();
        System.out.println(" List<Subscription> l = " + l);
        return ((l !=null)&&(!l.isEmpty()));
    }
}
