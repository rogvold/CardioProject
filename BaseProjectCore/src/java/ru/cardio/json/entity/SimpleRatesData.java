package ru.cardio.json.entity;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import ru.cardio.exceptions.CardioException;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SimpleRatesData {

    public static final String DATE_MASK = "yyyy-MM-dd HH:mm:ss.SSS";
    private static Gson gson;
    private String deviceId;
    private String email;
    private String password;
    private List<Integer> rates;
    private Date start;
    private int create;


    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        gson = builder.create();
    }

    public SimpleRatesData(String deviceId, String email, String password, List<Integer> rates, Date start, int create) {
        this.deviceId = deviceId;
        this.email = email;
        this.password = password;
        this.rates = rates;
        this.start = start;
        this.create = create;
    }

    public SimpleRatesData() {
        super();
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static SimpleRatesData fromJson(String json) throws CardioException {
        try {
            SimpleRatesData fj = gson.fromJson(json, SimpleRatesData.class);
            System.out.println("from json: json = " + json + "; fj = " + fj.toString());
            return fj;
        } catch (Exception e) {
            System.out.println("fromJson: e = " + e.getMessage());
            throw new CardioException("Cannot parse json = " + json + " e = " + e.getMessage());
        }
    }

    public int getCreate() {
        return create;
    }

    public void setCreate(int create) {
        this.create = create;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return " \n{\n  email = " + email + "\n"
                + " password = " + password + "\n"
                + " rates = " + rates + "\n"
                + " create = " + create + "\n"
                + " start = " + start + "\n}";
    }
}
