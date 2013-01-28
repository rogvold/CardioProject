package ru.cardio.web.beans;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.managers.UserManagerLocal;
import web.utils.WebSession;

/**
 *
 * @author rogvold
 */
@ViewScoped
@ManagedBean
public class TrainersViewBean implements Serializable {

    @ManagedProperty("#{webSession}")
    WebSession webSession;
    @EJB
    UserManagerLocal userMan;
    private Long userId;
    private List<User> myTrainers;
    private List<User> newTrainers;
    private List<User> notMyTrainers;

    @PostConstruct
    private void init() {
        userId = webSession.getUserId();
        initLists();
    }

    private void initLists() {
        myTrainers = userMan.getFriends(userId);
        newTrainers = userMan.getNewFriends(userId);
        notMyTrainers = userMan.getUsersExceptFor(User.TRAINER, myTrainers, newTrainers);
    }

    public Integer traineesAmount(Long trainerId) {
        return userMan.getFriendsAmount(trainerId);
    }

    public void deleteTrainer(Long trainerId) throws Exception {
        System.out.println("deleteTrainer: userId=" + userId);
        userMan.deleteFriend(userId, trainerId);
        initLists();
    }

    public void rejectTrainer(Long trainerId) throws Exception {
        userMan.rejectFriendRequest(userId, trainerId, "Пользователь id=" + userId + " не хочет быть вашим пациентом.");
    }

    public void confirmTrainer(Long trainerId) throws Exception {
        userMan.confirmFriendRequest(userId, trainerId);
    }

//    public void addTrainer(Long trainerId) throws Exception {
//        userMan.addTrainer(userId, trainerId);
//        initLists();
//    }
    public boolean canBid(Long trainerId) {
        return userMan.checkIfCanBid(userId, trainerId);
    }

    public void bidTrainerFriendRequest(Long trainerId) throws Exception {
        userMan.bidFriendRequest(userId, trainerId, "Пользователь id=" + userId + " хочет быть вашим пациентом");
    }

    public boolean canBidTrainer(Long trainerId) {
        return userMan.checkIfCanBid(userId, trainerId);
    }

    public List<User> getMyTrainers() {
        return myTrainers;
    }

    public void setMyTrainers(List<User> myTrainers) {
        this.myTrainers = myTrainers;
    }

    public List<User> getNotMyTrainers() {
        return notMyTrainers;
    }

    public void setNotMyTrainers(List<User> notMyTrainers) {
        this.notMyTrainers = notMyTrainers;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<User> getNewTrainers() {
        return newTrainers;
    }

    public void setNewTrainers(List<User> newTrainers) {
        this.newTrainers = newTrainers;
    }

    public void setWebSession(WebSession webSession) {
        this.webSession = webSession;
    }
}
