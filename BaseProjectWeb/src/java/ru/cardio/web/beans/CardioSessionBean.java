/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.managers.CardioSessionManagerLocal;
import web.utils.SessionUtils;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class CardioSessionBean implements Serializable {

    @EJB
    CardioSessionManagerLocal sm;
    private List<Long> userCardioSessions;
    private Long selectedCardioSessionId;

    @PostConstruct
    private void init() {
        this.userCardioSessions = sm.getUserCardioSessionsId(SessionUtils.getUserId());
        this.selectedCardioSessionId = sm.getCurrentCardioSessionId(SessionUtils.getUserId());
    }

    public void changeSelectedSession(Long selId) {
        this.selectedCardioSessionId = selId;
        System.out.println("selected session id = " + selectedCardioSessionId);
    }

    public Long getSelectedCardioSessionId() {
        return selectedCardioSessionId;
    }

    public void setSelectedCardioSessionId(Long selectedCardioSessionId) {
        this.selectedCardioSessionId = selectedCardioSessionId;
    }

    public List<Long> getUserCardioSessions() {
        return userCardioSessions;
    }

    public void setUserCardioSessions(List<Long> userCardioSessions) {
        this.userCardioSessions = userCardioSessions;
    }

    public int amountOfRatesById(Long sessionId) {
        return sm.getSessionRatesAmountById(sessionId);
    }

    public Long durationBySessionId(Long sessionId) {
        return sm.getSessionDurationById(sessionId);
    }

    public String prettyDurationBySessionId(Long sessionId){
        Long d = durationBySessionId(sessionId);
        Long min = d / (1000 * 60);
        Long millisec = d % (1000*60);
        String s = min  + " m. "+ millisec / 1000 +" s.";
        return s;
    }
    
    public Date startDateBySessionId(Long sessionId) {
        return sm.getSessionStartDateById(sessionId);
    }

    public int statusBySessionId(Long sessionId) {
        try {
            return sm.getSessionStatusById(sessionId);
        } catch (Exception e) {
            return 0;
        }
        
    }
}
