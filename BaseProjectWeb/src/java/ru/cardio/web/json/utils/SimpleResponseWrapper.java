package ru.cardio.web.json.utils;

import com.google.gson.Gson;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SimpleResponseWrapper {

    private static class SimpleResponse {

        private String response;

        public SimpleResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public static String getJsonResponse(String respValue) {
        System.out.println("getJsonResponse: respValue = " + respValue);
        return (new Gson()).toJson(new SimpleResponse(respValue));
    }
}
