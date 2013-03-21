

package ru.cardio.web.json.utils;

import com.google.gson.Gson;
import ru.cardio.exceptions.CardioException;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioExceptionWrapper {
    
    
    private static class JsonException{
        private String error;

        public JsonException(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
        
    }
    
    public static String wrapException(Exception e){
        Gson gson = new Gson();
        return gson.toJson(new JsonException(e.getMessage()));
    }

}
