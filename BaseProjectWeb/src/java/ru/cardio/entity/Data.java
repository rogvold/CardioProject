package ru.cardio.entity;

import java.util.Date;
import java.util.List;

/**
 *
 * @author rogvold
 */
public class Data {

    private List<Integer> rates;
    private Date start;
    private String id;
    private String password;
    private String email;
    private boolean shouldCreateSession;

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

    public boolean isShouldCreateSession() {
        return shouldCreateSession;
    }

    public void setShouldCreateSession(boolean shouldCreateSession) {
        this.shouldCreateSession = shouldCreateSession;
    }

    
    
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.replace(" ", "");
    }

    @Override
    public String toString() {
        return "Data : \n"
                + "id = " + id + ";\n"
                + "start = " + start + ";\n"
                + "rates = " + rates + "\n";
    }

//    public void updateRatesWithCalculateRatesStartDate() {
//        Date d = new Date();
//        d.setTime(start.getTime());
//        List nrates = new ArrayList();
//        for (Rate r : rates) {
//            Rate currRate = new Rate(d, r.getDuration());
//            nrates.add(currRate);
//            d = new Date();
//            d.setTime(currRate.getDuration() + currRate.getStart().getTime());
//        }
//        this.rates = nrates;
//    }
}
