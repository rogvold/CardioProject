package ru.cardio.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.managers.CardioSessionManagerLocal;
import ru.cardio.web.js.utils.CardioSessionHelper;
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
    private List<Long> userCardioSessionsIds;
    private Long selectedCardioSessionId;
    private List<CardioSession> userCardioSessions;
    private Long deletionSessionId;

    private CardioSessionHelper cardioHelper = new CardioSessionHelper();
    
    @PostConstruct
    private void init() {
        this.userCardioSessionsIds = sm.getUserCardioSessionsId(SessionUtils.getUserId());
        this.userCardioSessions = sm.getUserCardioSessions(SessionUtils.getUserId());
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

    public List<Long> getUserCardioSessionsIds() {
        return userCardioSessionsIds;
    }

//    public List<CardioSession> 
    public void setUserCardioSessionsIds(List<Long> userCardioSessionsIds) {
        this.userCardioSessionsIds = userCardioSessionsIds;
    }

    public List<CardioSession> getUserCardioSessions() {
        return userCardioSessions;
    }

    public void setUserCardioSessions(List<CardioSession> userCardioSessions) {
        this.userCardioSessions = userCardioSessions;
    }

    public Long getDeletionSessionId() {
        return deletionSessionId;
    }

    public void setDeletionSessionId(Long deletionSessionId) {
        this.deletionSessionId = deletionSessionId;
    }

    public int amountOfRatesById(Long sessionId) {
        return sm.getSessionRatesAmountById(sessionId);
    }

    public Long durationBySessionId(Long sessionId) {
        return sm.getSessionDurationById(sessionId);
    }

    public String prettyDurationBySessionId(Long sessionId) {
        Long d = durationBySessionId(sessionId);
        if (d == null) {
            return "";
        }
        Long min = d / (1000 * 60);
        Long millisec = d % (1000 * 60);
        String s = min + " m. " + millisec / 1000 + " s.";
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
    
    public String timelineJsonBySessionId(Long sessionId){
       return cardioHelper.getTimelineJson(sm.getCardioSessionById(sessionId));
    }
    
    public String getAllSessionsTimelineJson(){
        return cardioHelper.getTimelineJsonFromSessionList(userCardioSessions);
    }

    public void selectSessionForDeleting(Long sessionId) {
        this.deletionSessionId = sessionId;
    }

    public void updateDescription(Long sessionId, String newDescription) {
        System.out.println("updateDescription() occured : sessionId = " + sessionId + ", descr = " + newDescription);
        sm.updateSessionDescription(sessionId, newDescription);
    }

    public void deleteCardioSession(Long sessionId) {
        System.out.println("simulation deleting. session id = " + sessionId);
        int del = 0;
        for (int i = 0; i < this.userCardioSessions.size(); i++) {
            if (this.userCardioSessions.get(i).getId().equals(sessionId)) {
                del = i;
                break;
            }
        }

        if (sm.deleteSession(sessionId)) {
            this.userCardioSessions.remove(del);
        }
    }
    
    public boolean hasActiveSession(Long userId){
        return sm.userHasActiveSession(userId);
    }
    
}
