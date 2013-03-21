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
import ru.cardio.exceptions.CardioException;
import ru.cardio.filters.BaevskyFilter;
import ru.cardio.json.entity.SimpleRatesData;

/**
 *
 * @author rogvold
 */
@Stateless
public class CardioSessionManager implements CardioSessionManagerLocal {
    
    public static final String DELIMETER = ";";
    public static final int SESSION_TIMEOUT = 120000;
    public static final boolean SESSION_TIMEOUT_MODE = true;
    public static final boolean SESSION_CREATION_FLAG_MODE = false;
    public static final boolean BAEVSKY_FILTER_ENABLED = true;
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
        if (BAEVSKY_FILTER_ENABLED) {
            list = BaevskyFilter.getInstance().filterRates(list);
        }
        
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


        if (amount == -1) {
            return rates;
        }
        if (rates.size() < amount) {
            return rates;
        } else {
            return rates.subList(0, amount);
        }
    }
    
    private Double getPulse(Long sessionId) {
        List<Rate> rates = getRatesInCardioSession(sessionId, -1);
        Double p = 0.0;
        for (int i = rates.size() - 3; i < rates.size(); i++) {
            p += rates.get(i).getDuration();
        }
        p = (180000.0 / p);
        return Math.floor(p);
    }
    
    @Override
    public CardioSession getCardioSessionById(Long sessionId) {
//        System.out.println("getCardioSessionById: sessionID = " + sessionId);
        try {
            return (sessionId == null) ? null : em.find(CardioSession.class, sessionId); // sorry
        } catch (Exception e) {
            return null;
        }
        
    }
    
    @Override
    public List<Rate> getMyRatesInCardioSession(Long sessionId, int amount, Long requestOwnerId) {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            return null;
        }
        //TODO: erase || requestOwnerId == null
        if (cs.getUserId().equals(requestOwnerId) || requestOwnerId == null) {
//            System.out.println("getMyRatesInCardioSession: sessionId = " + sessionId + " ; requestOwnerId = " + requestOwnerId);
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
    
    private String getKubiosDataOfCardioSession(List<Rate> list) {
        String s = "";
        for (Rate r : list) {
            s += r.getDuration() + "\n";
        }
        return s;
    }
    
    private List<String> getKubiosDataOfCardioSessionStringList(List<Rate> list) {
        List<String> l = new ArrayList();
        for (Rate r : list) {
            l.add(new Integer(r.getDuration()).toString());
        }
        return l;
    }
    
    @Override
    public List<String> getKubiosDataOfRatesInCardioSessionBySessionId(Long sessionId, int amount, Long requestOwnerId) {
        List<Rate> list = getMyRatesInCardioSession(sessionId, amount, requestOwnerId);
        return (list == null) ? null : getKubiosDataOfCardioSessionStringList(list);
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
    
    private void checkIfsessionWithThisStartDateExists(Date start) throws CardioException {
        String jpql = "select c from CardioSession c where c.startDate = :start";
        Query q = em.createQuery(jpql).setParameter("start", start);
        List<CardioSession> list = q.getResultList();
        if (list == null) {
            return;
        }
        if (list.size() > 0) {
            throw new CardioException("session with startDate = " + start + " already exists in the system");
        }
    }
    
    @Override
    public void addRatesCreatingNewSession(Long userId, List<Integer> ratesIdList, Date startDate) throws CardioException {
        checkIfsessionWithThisStartDateExists(startDate);
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
//            System.out.println("getCurrentCardioSession: exc = " + e);
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
    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession) throws CardioException {
        if ((ratesList == null) || (ratesList.isEmpty())) {
            return;
        }
        
        CardioSession currentCs = getCurrentCardioSession(userId);
        User user = userMan.getUserById(userId);
        user.setLastDataRecievedDate(new Date());
        em.merge(user);
        
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
    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession, String password) throws CardioException {
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
    public void addRates(String email, List<Integer> ratesList, Date startDate, boolean createSession, String password) throws CardioException {
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
    
    @Override
    public Double getCurrentPulse(Long sessionId) {
        return getPulse(sessionId);
    }
    
    @Override
    public void addRates(SimpleRatesData srd) throws CardioException {
        System.out.println("addRates: srd: \n" + srd.toString());
        try {
            addRates(srd.getEmail(), srd.getRates(), srd.getStart(), (srd.getCreate() == 1) ? true : false, srd.getPassword());
        } catch (Exception e) {
            throw new CardioException(e.getMessage());
        }
    }
    
    private Date calculateEndDate(Date startDate, List<Integer> ratesList){
        long l = startDate.getTime();
        for (Integer i : ratesList){
            l+=i;
        }
        return new Date(l);
    }    
    
    private void checkNewIncomingSession(Long userId, List<Integer> ratesList, Date startDate)throws CardioException{
        Date endDate = calculateEndDate(startDate, ratesList);
        Query q  = em.createQuery("select cs FROM CardioSession cs  WHERE cs.startDate = :start").setParameter("start", startDate);
        List<CardioSession> slist = q.getResultList();
        if (q!=null){
            throw new CardioException("The session with startDate = " + startDate+" has been already saved");
        }
    }
    
    @Override
    //dummy implementation
    public void syncRates(Long userId, List<Integer> ratesList, Date startDate) throws CardioException {
        if ((ratesList == null) || (ratesList.isEmpty())) {
            return;
        }
        checkNewIncomingSession(userId, ratesList, startDate);
        addRatesCreatingNewSession(userId, ratesList, startDate);
    }
    
    @Override
    public void syncRates(Long userId, List<Integer> ratesList, Date startDate, String password) throws CardioException {
        if (userMan.checkAuData(userId, password) == false) {
            throw new CardioException("syncRates: authorisation failed");
        }
        syncRates(userId, ratesList, startDate);
    }
    
    
    
    @Override
    public void syncRates(String email, List<Integer> ratesList, Date startDate, String password) throws CardioException {
        if (!userMan.checkEmailAndPassword(email, password)){
            throw new CardioException("syncRates: authorisation failed");
        }
        syncRates(userMan.getUserByEmail(email).getId(), ratesList, startDate);
    }

    @Override
    public void syncRates(SimpleRatesData srd) throws CardioException {
        syncRates(srd.getEmail(), srd.getRates(), srd.getStart(), srd.getPassword());
    }
    
}
