package ru.cardio.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.jpa.entity.User;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {
    
    
    public User getUserById(Long userId);
    public User getUserByEmail(String email);
    
    public User registerNewUser(String email,  String password, String firstName, String  lastName, int group);
    
    public User logInByEmail(String email, String password);
    
    public boolean userExistsByEmail(String email);
    public boolean userExistsById(Long userId);
    
    public boolean checkAuData(Long userId, String password);
    public boolean checkAuthorisationData(String email, String password);

    public boolean checkEmailAndLogin(String email, String password) throws Exception;
    
    public int getUserGroupById(Long userId);
    
    public void updateInfo(Long userId, String fName, String lName, String dep, String newStatusMessage);
    
    public boolean userSensorIsWorking(Long userId);
    
//    public List<User> getTrainersOfUser(Long traineeId);
//    public List<User> getTraineesOfser(Long trainerId);
//    
//    public List<User> getTrainersExceptFor(List<User> etrainers);
//    
//    public void addTrainer(Long traineeId, Long trainerId) throws Exception;
//    public void addTrainee(Long trainerId, Long traineeId) throws Exception;
//    public void deleteTrainee(Long trainerId, Long traineeId) throws Exception;
//    public void deleteTrainer(Long traineeId, Long trainerId) throws Exception;
    
    public void deleteFriend(Long userAId, Long userBId);
    
    public void bidFriendRequest(Long myId, Long friendId, String message)  throws Exception;
    public void confirmFriendRequest(Long myId, Long friendId) throws Exception;
    public void rejectFriendRequest(Long myId, Long friendId, String rejectionMessage) throws Exception;
    
    public void rejectFriendRequest(Long myId, Long friendId)  throws Exception ;
    public int getUsersFriendshipStatus(Long userA, Long userB);
    
    public List<User> getNewFriends(Long myId);
    
    public int newFriendsAmount(Long userId);
    public boolean hasNewFriends(Long userId);
    
    public boolean checkIfCanBid(Long myId, Long friendId);
    public List<User> getFriends(Long userId);
    
    public Integer getFriendsAmount(Long userId);
    
    public boolean areConnected(Long userAId, Long userBId);
    public List<User> getAllUsersByRole(int userGroup);
    
    public List<User> getUsersExceptFor(int role, Object... etrainers);
    
    public CardioSession getLastCardioSession(Long userId);
    
}
