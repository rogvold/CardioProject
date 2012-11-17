package ru.cardio.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import ru.cardio.core.jpa.entity.UserCard;
import ru.cardio.core.managers.UserCardManagerLocal;
import web.utils.SessionUtils;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class UserCardBean {
    
    @EJB
    UserCardManagerLocal cardMan;
    
    private static final String ID_PARAM = "id";
    
    
    private Long userId;
    private UserCard card ;
    
    @PostConstruct
    private void init() throws Exception{
        userId = SessionUtils.getUserId();
        card = cardMan.getCardByUserId(userId);
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
    
    public void updateCard(){
        cardMan.updateUserCard(card, userId);
    }
    
}
