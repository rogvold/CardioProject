package ru.cardio.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rogvold
 */
public class UserUtils {

    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
