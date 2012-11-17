package web.utils;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import ru.cardio.core.managers.UserManagerLocal;

/**
 *
 * @author Danon and Sabir
 */
@ManagedBean
@SessionScoped
public class WebSession implements Serializable {

    private static final String LOGIN_REDIRECT_URL = "/BaseProjectWeb/faces/login.xhtml";
    private transient HttpSession session = null;
    private boolean registered;
    @EJB
    UserManagerLocal userMan;

    public boolean isSignedIn() {
        boolean b = SessionUtils.isSignedIn();
        System.out.println("isSignedId = " + b);
        return b;
    }

    @PostConstruct
    private void init() {
        registered = userMan.userExistsById(getUserId());
    }

    public void loginRedirect() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (!isSignedIn()) {
            ExternalContext ext = fc.getExternalContext();
            
            ext.redirect(LOGIN_REDIRECT_URL);
        }
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Long getUserId() {
        return SessionUtils.getUserId();
    }

    public void resetSession() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession mysession = (HttpSession) fc.getExternalContext().getSession(true);
        mysession.invalidate();
        fc.getExternalContext().getSession(true);
    }
}
