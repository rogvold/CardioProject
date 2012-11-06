/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.entity.Rate;
import ru.cardio.core.jpa.entity.CardioSession;

/**
 *
 * @author rogvold
 */
@Local
public interface CardioSessionManagerLocal {

    public List<Rate> getRatesInCardioSession(Long sessionId, int amount);

    public List<Rate> getMyRatesInCardioSession(Long sessionId, int amount, Long reuquestOwnerId);

    public String getPlotDataOfRatesInMyCardioSession(Long sessionId, int amount, Long requestOwnerId);

    public String getPlotDataOfCurrentSession(int amount, Long requestOwnerId);

    public CardioSession getCardioSessionById(Long sessionId);

    public List<Long> getUserCardioSessionsId(Long userId);

    public void addRatesCreatingNewSession(Long userId, List<Integer> ratesIdList, Date startDate);

    public CardioSession getCurrentCardioSession(Long userId);
    public Long getCurrentCardioSessionId(Long userId);

    public void disableCurrentCardioSession(Long userId);

    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession);

    public void addRates(Long userId, List<Integer> ratesList, Date startDate, boolean createSession, String password);
    
    public int getSessionRatesAmountById(Long sessionId);
    public Long getSessionDurationById(Long sessionId);
    public Date getSessionStartDateById(Long sessionId);
    public int getSessionStatusById(Long sessionId);
}
