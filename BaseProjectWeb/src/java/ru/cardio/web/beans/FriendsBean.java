package ru.cardio.web.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.managers.UserManagerLocal;
import web.utils.WebSession;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class FriendsBean implements Serializable {

    @ManagedProperty("#{webSession}")
    WebSession webSession;
    @EJB
    UserManagerLocal userMan;
    private Long userId;

    @PostConstruct
    private void init() {
        userId = webSession.getUserId();
    }

    public boolean hasNewFriends() {
        return userMan.hasNewFriends(userId);
    }
    
    public int getNewFriendsAmount(){
        return userMan.newFriendsAmount(userId);
    }

    public void setWebSession(WebSession webSession) {
        this.webSession = webSession;
    }
    
    public boolean areFriends(Long userAId, Long userBId){
        return userMan.areConnected(userAId, userBId);
    }
    
    
}
