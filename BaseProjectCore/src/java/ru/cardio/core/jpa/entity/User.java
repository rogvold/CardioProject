package ru.cardio.core.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author rogvold
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "getBidsToMe", query = "select u from User u, FriendRequest fr where (u.id = fr.fromId) and (fr.toId = :userId ) and (fr.status = :status)"),
    @NamedQuery(name = "getBidsToMeAmount", query = "select count(u) from User u, FriendRequest fr where (u.id = fr.fromId) and (fr.toId = :userId ) and (fr.status = :status)"),
    @NamedQuery(name = "getFriends", query = "select u from User u, FriendRequest fr where ( ((u.id = fr.toId) and (fr.fromId = :userId )) or ((u.id = fr.fromId) and (fr.toId = :userId ))) and (fr.status = 2)"),
    @NamedQuery(name = "getFriendsAmount", query = "select count(u) from User u, FriendRequest fr where ( ((u.id = fr.toId) and (fr.fromId = :userId )) or ((u.id = fr.fromId) and (fr.toId = :userId ))) and (fr.status = 2)"),
    @NamedQuery(name = "getMyBids", query = "select u from User u, FriendRequest fr where (u.id = fr.toId) and (fr.fromId = :userId ) and (fr.status = :status)")
})
public class User implements Serializable {

    public static final int USER = 0;
    public static final int TRAINER = 1;
    public static final int ADMIN = 2;
    public static final int FRIENDSHIP_UNFAMILIAR = -1;
    public static final int FRIENDSHIP_REJECTED_BY_FIRST = 1;
    public static final int FRIENDSHIP_REJECTED_BY_SECOND = 2;
    public static final int FRIENDSHIP_CONFIRMED = 5;
    public static final int FRIENDSHIP_FIRST_REQUESTED_SECOND = 3;
    public static final int FRIENDSHIP_SECOND_REQUESTED_FIRST = 4;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String login;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private int userGroup;
    @Column
    private int status;
    @Column
    private String statusMessage;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date statusMessageChangingDate;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastDataRecievedDate;
    @Column
    private String department;

    public User() {
        super();
    }

    
    
    public User(String email, String password, String firstName, String lastName, String department, String statusMessage) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.statusMessage = statusMessage;
    }

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(int userGroup) {
        this.userGroup = userGroup;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        this.statusMessageChangingDate = new Date();
    }

    public Date getStatusMessageChangingDate() {
        return statusMessageChangingDate;
    }

    public void setStatusMessageChangingDate(Date statusMessageChangingDate) {
        this.statusMessageChangingDate = statusMessageChangingDate;
    }

    public Date getLastDataRecievedDate() {
        return lastDataRecievedDate;
    }

    public void setLastDataRecievedDate(Date lastDataRecievedDate) {
        this.lastDataRecievedDate = lastDataRecievedDate;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.cardio.core.entity.User[ id=" + id + " ; email = " + email + " ]";
    }
}
