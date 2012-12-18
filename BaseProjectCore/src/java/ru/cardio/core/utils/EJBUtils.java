package ru.cardio.core.utils;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Helps to load EJBs
 * 
 * @author danon
 */
public abstract class EJBUtils {
    
    public static <T> T resolve(String jndiName, Class<T> clazz) {
        Context context = null;
        T t = null;
        try {
            context = new InitialContext();
            t = (T)context.lookup(jndiName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return t;
    }
    
    public static Object resolve(String jndiName) {
        Context context = null;
        Object o = null;
        try {
            context = new InitialContext();
            o = context.lookup(jndiName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return o;
    }
    
}
