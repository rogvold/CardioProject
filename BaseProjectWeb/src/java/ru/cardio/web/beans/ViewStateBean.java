package ru.cardio.web.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import ru.cardio.core.jpa.entity.Constant;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.managers.CardioSessionManagerLocal;
import ru.cardio.core.managers.ConstantManagerLocal;
import ru.cardio.core.managers.IndicatorsManagerLocal;
import ru.cardio.core.managers.UserManagerLocal;
import ru.cardio.graphics.MyPlot;
import ru.cardio.indicators.HRVIndicatorsService;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class ViewStateBean {

    private static final String FIELD_ID = "id";
    private static final int ACCURACY_INTERVAL = 20;
    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    IndicatorsManagerLocal indMan;
    @EJB
    UserManagerLocal userMan;
    @EJB
    ConstantManagerLocal cMan;
    
    List<User> usersList = null;
    private long accuracyInterval = ACCURACY_INTERVAL;

    private void initUsersList() {
        String susers = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("users");
        if (susers == null || susers.isEmpty()) {
            return;
        }
        StringTokenizer st = new StringTokenizer(susers, "_");
        usersList = new ArrayList();
        while (st.hasMoreTokens()) {
            usersList.add(userMan.getUserById(Long.parseLong(st.nextToken())));
        }
    }

    @PostConstruct
    private void init() {
        initUsersList();
    }

    private long getAccuracyInterval(){
        String step = cMan.getConstantValueByName(Constant.STEP_DURATION_NAME);
        if (step!=null){
            accuracyInterval = Long.parseLong(step);
        }
        
        return accuracyInterval;
    }
    
    public String plotHRVJsonString(String paramName, Long userId) throws Exception {
        Long sessionId = userMan.getLastCardioSessionId(userId);

        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new HRVIndicatorsService(), paramName, getAccuracyInterval() * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }
}
