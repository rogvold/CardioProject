package ru.cardio.core.managers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.UserCard;

/**
 *
 * @author rogvold
 */
@Stateless
public class UserCardManager implements UserCardManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    @Override
    public UserCard getCardByUserId(Long userId){
        try {
            System.out.println("getCardByUserId: userId = " + userId);
            Query q = em.createQuery("select c from UserCard c where c.userId = :userId").setParameter("userId", userId);
            return (UserCard) q.getSingleResult();
        } catch (NoResultException e) {
            UserCard card = new UserCard();
            card.setUserId(userId);
            return em.merge(card);
        }
    }

    @Override
    public UserCard updateUserCard(UserCard card, Long userId) {
        if (userId == null || card == null) {
            return null;
        }
        try {
            UserCard c = getCardByUserId(userId);
            c.setDescription(card.getDescription());
            c.setDiagnosis(card.getDiagnosis());
            c.setFirstName(card.getFirstName());
            c.setLastName(card.getLastName());
            c.setAboutMe(card.getAboutMe());
            c.setUserId(userId);
            return em.merge(c);
        } catch (Exception ex) {
            System.out.println("updateUserCard: exception = " + ex.toString());
            return null;
        }

    }
}
