/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author rogvold
 */
@Entity
public class User implements Serializable {
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
    @Column(name="current_session_id")
    private Long currentSessionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(Long currentSessionId) {
        this.currentSessionId = currentSessionId;
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
        return "ru.cardio.core.entity.User[ id=" + id + " ]";
    }
    
}
