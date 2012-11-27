package ru.cardio.core.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.entity.Rate;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class CardioSessionManager implements CardioSessionManagerLocal {

    public static final String DELIMETER = ";";
    public static final int SESSION_TIMEOUT = 15000;
    public static final boolean SESSION_TIMEOUT_MODE = false;
    public static final boolean SESSION_CREATION_FLAG_MODE = false;
    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;
    @EJB
    UserManagerLocal userMan;

    @Override
    public List<Rate> getRatesInCardioSession(Long sessionId, int amount) {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            return null;
        }
        List<Integer> list = CardioUtils.getIntervalsFromString(cs.getRates(), DELIMETER);
        List<Rate> rates = new ArrayList();

        Date d = new Date();
        d.setTime(cs.getStartDate().getTime());

        for (Integer i : list) {
            Rate currRate = new Rate(d, i);
            currRate.setSessionId(sessionId);
            rates.add(currRate);
            d = new Date();
            d.setTime(currRate.getDuration() + currRate.getStartDate().getTime());
        }

        System.out.println("getRatesInCardioSession: rates = " + rates);

        if (amount == -1) {
            return rates;
        }
        if (rates.size() < amount) {
            return rates;
        } else {
            return rates.subList(0, amount);
        }
    }

    @Override
    public CardioSession getCardioSessionById(Long sessionId) {
        System.out.println("getCardioSessionById: sessionID = " + sessionId);
        try {
            return (sessionId == null) ? null : em.find(CardioSession.class, sessionId); // sorry
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public List<Rate> getMyRatesInCardioSession(Long sessionId, int amount, Long reuquestOwnerId) {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            return null;
        }
        if (cs.getUserId().equals(reuquestOwnerId)) {
            System.out.println("getMyRatesInCardioSession: sessionId = " + sessionId + " ; requestOwnerId = " + reuquestOwnerId);
            return getRatesInCardioSession(sessionId, amount);
        } else {
            return null;
        }
    }

    private String getPlotData(List<Rate> rates) {
        String s = "[";
        for (int i = 0; i < rates.size() - 1; i++) {
            Rate r = rates.get(i);
            s = s + "[" + r.getStartDate().getTime() + ", " + r.getDuration() + "], ";
        }
        Rate r = rates.get(rates.size() - 1);
        s = s + "[" + r.getStartDate().getTime() + ", " + r.getDuration() + "]";
        s = s + "]";
        return s;
    }

    @Override
    public String getPlotDataOfRatesInMyCardioSession(Long sessionId, int amount, Long requestOwnerId) {
        List<Rate> list = getMyRatesInCardioSession(sessionId, amount, requestOwnerId);
        return (list == null) ? null : getPlotData(list);
    }

    @Override
    public List<Long> getUserCardioSessionsId(Long userId) {
        Query q = em.createQuery("select c from CardioSession c where c.userId=:id order by c.startDate desc").setParameter("id", userId);
        List<CardioSession> ls = q.getResultList();
        List<Long> list = new ArrayList();
        for (CardioSession cs : ls) {
            list.add(cs.getId());
        }
        return list;
    }

    @Override
    public void addRatesCreatingNewSession(Long userId, List<Integer> ratesIdList, Date startDate) {
        CardioSession cs = new CardioSession();
        cs.setStartDate(startDate);
        String s = "";
        long time = startDate.getTime();
        if (ratesIdList != null) {
            for (int i = 0; i < ratesIdList.size() - 1; i++) {
                time += ratesIdList.get(i);
                s += ratesIdList.get(i) + ";";
            }
            s += ratesIdList.get(ratesIdList.size() - 1);
            time += ratesIdList.get(ratesIdList.size() - 1);
        }

        Date end = new Date();
        end.setTime(time);
        cs.setEndDate(end);
        cs.setRates(s);
        cs.setUserId(userId);
        cs.setStatus(CardioSession.STATUS_CURRENT);
        em.persist(cs);
    }

    @Override
    public CardioSession getCurrentCardioSession(Long userId) {
        if (userId == null) {
            return null;
        }
        try {
            Query q = em.createQuery("select c from CardioSession c where c.status=:currStat and c.userId = :uId").setParameter("currStat", CardioSession.STATUS_CURRENT).setParameter("uId", userId);
            return (CardioSession) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("getCurrentCardioSession: exc = " + e);
            return null;
        }
    }

    @Override
    public Long getCurrentCardioSessionId(Long userId) {
        try {
            return (userId == null) ? null : getCurrentCardioSession(userId).getId();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void disableCurrentCardioSession(Long userId) {
        CardioSession cs = getCurrentCardioSession(userId);
        if (cs == null) {
            return;
        }
        cs.setStatus(CardioSession.STATUS_OLD);
        em.merge(cs);
    }

    @Override
    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession) {
        if ((ratesList == null) || (ratesList.isEmpty())) {
            return;
        }

        CardioSession currentCs = getCurrentCardioSession(userId);

        if (currentCs == null) {
            addRatesCreatingNewSession(userId, ratesList, startDate);
            return;
        }

        if (currentCs.getEndDate().getTime() > startDate.getTime()) {
            System.out.println("Current endDate > startDate. Please check your clock.");
            return;
        }

        boolean shouldCreateNewSession = createSession;

        if (SESSION_TIMEOUT_MODE && startDate.getTime() - currentCs.getEndDate().getTime() > SESSION_TIMEOUT) {
            shouldCreateNewSession = true;
        }


        if (createSession) {
            currentCs.setStatus(CardioSession.STATUS_OLD);
            em.merge(currentCs);
            addRatesCreatingNewSession(userId, ratesList, startDate);
        } else { // adding rates and updating endTime

            long time = currentCs.getEndDate().getTime();
            String s = "";
            for (int i = 0; i < ratesList.size() - 1; i++) {
                time += ratesList.get(i);
                s += ratesList.get(i) + ";";
            }

            time += ratesList.get(ratesList.size() - 1);
            s += ratesList.get(ratesList.size() - 1);

            Date newEnd = new Date();
            newEnd.setTime(time);
            currentCs.setEndDate(newEnd);
            currentCs.setRates(currentCs.getRates() + ";" + s);
            em.merge(currentCs);
        }
    }

    @Override
    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession, String password) {
        if (userMan.checkAuData(userId, password) == false) {
            System.out.println("Authorisation failed");
            return;
        }
        addRates(userId, ratesList, startDate, createSession);
    }

    @Override
    public String getPlotDataOfCurrentSession(int amount, Long requestOwnerId) {
        CardioSession cs = getCurrentCardioSession(requestOwnerId);
        if (cs == null) {
            return null;
        }
        return getPlotDataOfRatesInMyCardioSession(cs.getId(), amount, requestOwnerId);
    }

    @Override
    public int getSessionRatesAmountById(Long sessionId) {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            return 0;
        }
        return cs.getRates().split(CardioSessionManager.DELIMETER).length;
    }

    @Override
    public Long getSessionDurationById(Long sessionId) {
        CardioSession cs = getCardioSessionById(sessionId);
        return (cs == null) ? null : cs.getEndDate().getTime() - cs.getStartDate().getTime();
    }

    @Override
    public Date getSessionStartDateById(Long sessionId) {
        CardioSession cs = getCardioSessionById(sessionId);
        return (cs == null) ? null : cs.getStartDate();
    }

    @Override
    public int getSessionStatusById(Long sessionId) {
        CardioSession cs = getCardioSessionById(sessionId);
        return (cs == null) ? -1 : cs.getStatus();
    }

    @Override
    public void addRates(String email, List<Integer> ratesList, Date startDate, boolean createSession, String password) {
        User u = userMan.getUserByEmail(email);
        addRates(u.getId(), ratesList, startDate, createSession, password);
    }

    @Override
    public void updateSessionDescription(Long sessionId, String newDescription) {
        try {
            CardioSession cs = em.find(CardioSession.class, sessionId);
            cs.setDescription(newDescription);
            em.merge(cs);
        } catch (Exception e) {
            System.out.println("exception occured: exc = " + e.toString());
        }
    }

    @Override
    public List<CardioSession> getUserCardioSessions(Long userId) {
        try {
            Query q = em.createQuery("select c from CardioSession c where c.userId=:id order by c.startDate desc").setParameter("id", userId);
            return q.getResultList();
        } catch (Exception e) {
            System.out.println("getUserCardioSessions(usrId  = " + userId + ") : exc = " + e.toString());
            return null;
        }

    }

    @Override
    public boolean deleteSession(Long sessionId) {
        try {
            CardioSession cs = getCardioSessionById(sessionId);
            em.remove(cs);
            return true;
        } catch (Exception e) {
            System.out.println("deleteSession( sessionId = " + sessionId + "): exc =  " + e.toString());
            return false;
        }
    }

    @Override
    public boolean userHasActiveSession(Long userId) {
        Long l = getCurrentCardioSessionId(userId);
        return l == null ? false : true;
    }
}
