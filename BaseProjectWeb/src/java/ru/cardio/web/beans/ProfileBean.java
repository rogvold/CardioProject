package ru.cardio.web.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.jpa.entity.UserCard;
import ru.cardio.core.managers.UserCardManagerLocal;
import ru.cardio.core.managers.UserManagerLocal;
import web.utils.WebSession;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class ProfileBean implements Serializable {

    @ManagedProperty("#{webSession}")
    WebSession webSession;
    @EJB
    UserManagerLocal userMan;
    private Long myId;
    @EJB
    UserCardManagerLocal cardMan;
    
    private User profileUser;
    private Long profileUserId;
    private UserCard profileUserCard;
    
    @PostConstruct
    private void init() {
        myId = webSession.getUserId();
        initProfileUser();
    }

    private void initProfileUser() {
        try {
            profileUserId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            profileUser = userMan.getUserById(profileUserId);
            profileUserCard = cardMan.getCardByUserId(profileUserId);
        } catch (Exception e) {
            System.out.println("profileUserId=" + profileUserId);
            profileUserId = null;
            profileUser = null;
            profileUserCard = null;
        }
    }

    public void setWebSession(WebSession webSession) {
        this.webSession = webSession;
    }

    public Long getMyId() {
        return myId;
    }

    public User getProfileUser() {
        return profileUser;
    }

    public Long getProfileUserId() {
        return profileUserId;
    }

    public UserCard getProfileUserCard() {
        return profileUserCard;
    }

    
    
    
}
