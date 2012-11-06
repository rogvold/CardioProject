package ru.cardio.core.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.Rate;

/**
 *
 * @author rogvold
 */
@Stateless
public class RateManager implements RateManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    @Override
    public Rate getRateByStartDate(Date startDate) {
        Query q = em.createQuery("select r from Rate r where r.start = :startDate");
        q.setParameter("startDate", startDate);
        try {
            return (Rate) q.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Rate> getLastRates(int number) {
        Query q = em.createQuery("select r from Rate r order by r.start desc ");
        List<Rate> list = q.getResultList();
        if (list.size() < number) {
            return list;
        } else {
            return list.subList(0, number);
        }
    }

    @Override
    public boolean rateExists(Date startDate) {
        Rate r = getRateByStartDate(startDate);
        if (r != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addRate(Date startDate, Integer duration) {
        Rate rate = new Rate(startDate, duration);
        if (!rateExists(startDate)) {
            em.persist(rate);
        }
    }

    @Override
    public void addRates(List<Rate> rates) {
        System.out.println("rates = " + rates);
        for (Rate r : rates) {
            addRate(r.getStart(), r.getDuration());
        }
    }

    @Override
    public String getPlotData(List<Rate> rates) {
        String s = "[";

        for (int i = 0; i < rates.size() - 1; i++) {
            Rate r = rates.get(i);
            s = s + "[" + r.getStart().getTime() + ", " + r.getDuration() + "], ";
        }
        Rate r = rates.get(rates.size() - 1);
        s = s + "[" + r.getStart().getTime() + ", " + r.getDuration() + "]";
        s = s + "]";
        return s;
    }
}
