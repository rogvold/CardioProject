package ru.cardio.core.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author rogvold
 */
@Entity
public class Constant implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String WINDOW_DURATION_NAME = "window_duration";
    public static final String STEP_DURATION_NAME = "step_duration";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    
    @Column
    private String name;
    
    @Column
    private String value;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(object instanceof Constant)) {
            return false;
        }
        Constant other = (Constant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.cardio.core.jpa.entity.Constant[ id=" + id + " ]";
    }
    
}
