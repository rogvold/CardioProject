package ru.cardio.web.beans;

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
import web.utils.SessionUtils;
import web.utils.WebSession;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class UserCardBean {

    @EJB
    UserCardManagerLocal cardMan;
    @EJB
    UserManagerLocal userMan;
    private static final String ID_PARAM = "id";
    private Long userId;
    private UserCard card;
    private User user;

    @PostConstruct
    private void init() throws Exception {
        userId = SessionUtils.getUserId();
        card = cardMan.getCardByUserId(userId);
        user = userMan.getUserById(userId);
    }

    public void updateCard() {
        cardMan.updateUserCard(card, userId);
        userMan.updateInfo(userId, card.getFirstName(), card.getLastName(), user.getDepartment(), null);
    }

    public UserCard getCard() {
        return card;
    }

    public void setCard(UserCard card) {
        this.card = card;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
