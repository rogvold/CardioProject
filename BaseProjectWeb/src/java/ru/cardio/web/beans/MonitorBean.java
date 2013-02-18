package ru.cardio.web.beans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.managers.UserManagerLocal;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class MonitorBean {
    
    
    @EJB
    UserManagerLocal userMan;
    
    
    
    public boolean activeSensor(Long userId){
        return userMan.userSensorIsWorking(userId);
    }
    
}
