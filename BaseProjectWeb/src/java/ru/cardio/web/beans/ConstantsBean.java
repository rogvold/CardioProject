package ru.cardio.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.cardio.core.managers.ConstantManagerLocal;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class ConstantsBean {
    
    private String value; 
    private String step;
    private String window;
            
    @EJB
    ConstantManagerLocal cMan;
    
    @PostConstruct
    private void init(){
        window = cMan.getConstantValueByName("window_duration");
        step = cMan.getConstantValueByName("step_duration");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }
    
    public void update(){
        System.out.println("updating");
        cMan.updateConstant("step_duration", step);
        cMan.updateConstant("window_duration", window);
    }
    
}
