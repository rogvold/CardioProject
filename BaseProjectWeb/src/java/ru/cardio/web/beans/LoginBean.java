package ru.cardio.web.beans;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.managers.UserManagerLocal;
import ru.cardio.core.utils.UserUtils;
import web.utils.SessionListener;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

    @EJB
    UserManagerLocal userMan;
    private transient HttpSession session = null;
//    for login
    public static final String FLAG_LOGGED_IN = "loggedIn"; // redirect to index
    public static final String FLAG_INCORRECT_PASSWORD = "incorrectPassword";
    public static final String FLAG_EMPTY_PASSWORD = "emptyPassword";
    public static final String FLAG_USER_DOES_NOT_EXIST = "userDoesNotExist";
//    for registration
    public static final String FLAG_INVALID_EMAIL = "invalidEmail";
    public static final String FLAG_USER_EXISTS = "userExists";
    public static final String FLAG_CONFIRMATION_PASSOWRD_IS_WRONG = "incorrectConfirmation";
    public static final String FLAG_REGISTRATION_ERROR = "registrationError";
    //----------------------
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String confirmPassword;
    private String flag;
    private String notificationText;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    private boolean checkEmailAndPasword() {
        boolean b = true;
        if (UserUtils.isValidEmail(email) == false) {
            this.flag = FLAG_INVALID_EMAIL;
            addNotificationTextByFlag(this.flag);
            b = false;
        }
        if (password.isEmpty()) {
            this.flag = FLAG_EMPTY_PASSWORD;
            addNotificationTextByFlag(this.flag);
            b = false;
        }
        return b;
    }

    private boolean checkLoginData() {
        boolean b = true;
        b = checkEmailAndPasword();
        if (b == true) {
            if (!userMan.userExistsByEmail(email)) {
                b = false;
                this.flag = FLAG_USER_DOES_NOT_EXIST;
                addNotificationTextByFlag(flag);
            }
        }

        return b;
    }

    private boolean checkRegistrationData() {
        boolean b = true;
        b = checkEmailAndPasword();
        if (!password.equals(confirmPassword)) {
            this.flag = FLAG_CONFIRMATION_PASSOWRD_IS_WRONG;
            addNotificationTextByFlag(flag);
            b = false;
        }

        if (b == true) {
            if (userMan.userExistsByEmail(email)) {
                b = false;
                this.flag = FLAG_USER_EXISTS;
                addNotificationTextByFlag(flag);
            }
        }
        return b;
    }

    private void addNotificationTextByFlag(String f) {

        if (f.equals(FLAG_CONFIRMATION_PASSOWRD_IS_WRONG)) {
            notificationText += "Пароль подтверждения не совпадает с паролем." + "\n";
        }
        if (f.equals(FLAG_USER_DOES_NOT_EXIST)) {
            notificationText += "Пользователь с указанным email не зарегистрирован в системе." + "\n";
        }
        if (f.equals(FLAG_INVALID_EMAIL)) {
            notificationText += "Не корректный email." + "\n";
        }
        if (f.equals(FLAG_EMPTY_PASSWORD)) {
            notificationText += "Вы не ввели пароль." + "\n";
        }
        if (f.equals(FLAG_REGISTRATION_ERROR)) {
            notificationText += "Ошибка регистрации... Попробуйте еще раз." + "\n";
        }
    }

    public void register() {
        this.notificationText = "";
        if (!checkRegistrationData()) {
            return;
        }
        User u = userMan.registerNewUser(email, password, firstName, lastName);
        if (u == null) {
            this.flag = FLAG_REGISTRATION_ERROR;
            addNotificationTextByFlag(flag);
            return;
        }
        login();
    }

    public void login() {
        this.notificationText = "";
        if (!checkLoginData()) {
            return;
        }
        User u = userMan.logInByEmail(email, password);
        if (u == null) {
            this.flag = FLAG_INCORRECT_PASSWORD;
            addNotificationTextByFlag(flag);
            return;
        }
        this.flag = FLAG_LOGGED_IN;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(true);
        SessionListener.setSessionAttribute(session, "userId", u.getId());
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }
}
