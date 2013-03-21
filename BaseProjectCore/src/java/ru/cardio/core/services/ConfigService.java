package ru.cardio.core.services;

import java.io.FileInputStream;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author rogvold
 */
@Singleton
@Startup
@LocalBean
public class ConfigService {

    private static Map<String, Object> parameters;
    Properties prop;

    private void loadParameters(){
        prop = new Properties();
        parameters = new HashMap();
        try {
            prop.load(new FileInputStream("config/config.properties"));
            Set<Object> keySet = prop.keySet();
            System.out.println("loading parameters:");
            for (Object o : keySet) {
                System.out.println(">>> " + o.toString()+"="+prop.getProperty(o.toString(),null));
                parameters.put(o.toString(), prop.getProperty(o.toString(), null));
            }
        } catch (Exception e) {
            System.out.println("unable to load parameters: " + e.getMessage());
        }
    }

    @PostConstruct
    private void init(){
        loadParameters();
    }

    public Object getParameter(String paramName){
        return parameters.get(paramName);
    }

    public static Map<String, Object> getParameters() {
        return parameters;
    }
}
