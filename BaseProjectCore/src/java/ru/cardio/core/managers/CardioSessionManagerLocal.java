package ru.cardio.core.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.entity.Rate;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.exceptions.CardioException;
import ru.cardio.json.entity.SimpleRatesData;

/**
 *
 * @author rogvold
 */
@Local
public interface CardioSessionManagerLocal {

    public List<Rate> getRatesInCardioSession(Long sessionId, int amount);

    public List<Rate> getMyRatesInCardioSession(Long sessionId, int amount, Long reuquestOwnerId);

    public String getPlotDataOfRatesInMyCardioSession(Long sessionId, int amount, Long requestOwnerId);

    public List<String> getKubiosDataOfRatesInCardioSessionBySessionId(Long sessionId, int amount, Long requestOwnerId);

    public String getPlotDataOfCurrentSession(int amount, Long requestOwnerId);

    public CardioSession getCardioSessionById(Long sessionId);

    public List<Long> getUserCardioSessionsId(Long userId);

    public List<CardioSession> getUserCardioSessions(Long userId);

    public void addRatesCreatingNewSession(Long userId, List<Integer> ratesIdList, Date startDate) throws CardioException;

    public CardioSession getCurrentCardioSession(Long userId);

    public Long getCurrentCardioSessionId(Long userId);

    public void disableCurrentCardioSession(Long userId);

    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession) throws CardioException;

    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession, String password) throws CardioException;

    public void addRates(String email, List<Integer> ratesList, Date startDate, boolean createSession, String password) throws CardioException;
    
    public void addRates(SimpleRatesData srd) throws CardioException;

    public int getSessionRatesAmountById(Long sessionId);

    public Long getSessionDurationById(Long sessionId);

    public Date getSessionStartDateById(Long sessionId);

    public int getSessionStatusById(Long sessionId);

    public void updateSessionDescription(Long sessionId, String newDescription);

    public boolean deleteSession(Long sessionId);

    public boolean userHasActiveSession(Long userId);
    
    public Double getCurrentPulse(Long sessionId);
    
    public void syncRates(Long userId, List<Integer> ratesList, Date startDate) throws CardioException;
    
    public void syncRates(Long userId, List<Integer> ratesList, Date startDate, String password) throws CardioException;
    
    public void syncRates(String email, List<Integer> ratesList, Date startDate, String password) throws CardioException;
    
    public void syncRates(SimpleRatesData srd) throws CardioException;
}
