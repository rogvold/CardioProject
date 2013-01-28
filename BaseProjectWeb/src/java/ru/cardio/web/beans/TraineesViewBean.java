package ru.cardio.web.beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.managers.UserManagerLocal;
import web.utils.WebSession;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class TraineesViewBean {

    @ManagedProperty("#{webSession}")
    WebSession webSession;
    @EJB
    UserManagerLocal userMan;
    private Long userId;
    private List<User> myTrainees;
    private List<User> newTrainees;
    private List<User> notMyTrainees;

    @PostConstruct
    private void init() {
        userId = webSession.getUserId();
        initLists();
    }

    private void initLists() {
        myTrainees = userMan.getFriends(userId);
        newTrainees = userMan.getNewFriends(userId);
        notMyTrainees = userMan.getUsersExceptFor(User.USER, myTrainees, newTrainees);
    }

    public void rejectTrainee(Long traineeId) throws Exception {
        userMan.rejectFriendRequest( userId, traineeId, "Пользователь id=" + userId + " не хочет быть вашим доктором.");
    }
    
    public void confirmTrainee(Long traineeId) throws Exception{
        userMan.confirmFriendRequest(userId, traineeId);
    }

    public boolean canBid(Long traineeId) {
        return userMan.checkIfCanBid(userId, traineeId);
    }

    public void bidTraineeFriendRequest(Long traineeId) throws Exception {
        userMan.bidFriendRequest(userId, traineeId, "Пользователь id=" + userId + " хочет быть вашим пациентом");
    }

    public void deleteTrainee(Long traineeId) throws Exception {
        System.out.println("deleteTrainee: userId=" + userId);
        userMan.deleteFriend(userId, traineeId);
        initLists();
    }

    public List<User> getMyTrainees() {
        return myTrainees;
    }


    public List<User> getNewTrainees() {
        return newTrainees;
    }


    public List<User> getNotMyTrainees() {
        return notMyTrainees;
    }


    public Long getUserId() {
        return userId;
    }


    public void setWebSession(WebSession webSession) {
        this.webSession = webSession;
    }
    
    
}
