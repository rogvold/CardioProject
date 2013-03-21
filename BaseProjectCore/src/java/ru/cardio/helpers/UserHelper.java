

package ru.cardio.helpers;

import com.google.gson.Gson;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.jpa.entity.UserCard;
import ru.cardio.exceptions.CardioException;
import ru.cardio.json.entity.SimpleUser;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class UserHelper {
    
    public static String getJsonFromSimpleUser(SimpleUser su){
        Gson gson = new Gson();
        String s = gson.toJson(su);
        System.out.println("getJsonFromSimpleUser: s = " + s);
        return s;
    }
    
    public static SimpleUser getSimpleUserFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, SimpleUser.class);
    }
    
    public static UserCard getUserCardFromSimpleUser(SimpleUser su) throws CardioException{
        if (su == null) throw new CardioException();
        UserCard uc = new UserCard(su.getFirstName(), su.getLastName(), su.getDescription(), su.getDiagnosis(), su.getAbout());
        return uc;
    }
    
    /**
     * specifing just FIRSNAME, LASTNAME, DESCRIPTION, DIAGNOSIS, ABOUT
     * @param json
     * @return
     * @throws CardioException 
     */
    public static UserCard getUserCardFromSimpleUserJson(String json) throws CardioException{
        SimpleUser su = getSimpleUserFromJson(json);
        return getUserCardFromSimpleUser(su);
    }
    
    public static User getUserFromSimpleUser(SimpleUser su) throws CardioException{
        if (su == null) throw new CardioException();
        User u = new User(su.getEmail(), su.getPassword(), su.getFirstName(), su.getLastName(), su.getDepartment(), su.getStatusMessage());
        return u;
    }
    
    
    /**
     * specifing just EMAIL, PASSWORD, FIRSNAME, LASTNAME, DEPARTMENT
     * @param json
     * @return
     * @throws CardioException 
     */
    public static User getUserFromSimpleUserJson(String json) throws CardioException{
        SimpleUser su = getSimpleUserFromJson(json);
        return getUserFromSimpleUser(su);
    }
    
    
}
