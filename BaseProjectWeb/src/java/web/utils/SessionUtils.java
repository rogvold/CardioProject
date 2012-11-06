package web.utils;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.ejbLinkType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danon
 */
public class SessionUtils {

    public static boolean isSignedIn() {
        if (SessionListener.getSessionAttribute("userId", false) != null) {
            return true;
        }
        return false;
    }

    public static Long getUserId() {
        Long uId = ((Long) SessionListener.getSessionAttribute("userId", true));
        return uId;
    }

}
