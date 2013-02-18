
package ru.cardio.core.entity;

import java.util.Date;

/**
 *
 * @author rogvold
 */
public class Rate {

    private Date startDate;
    private int duration;
    private Long sessionId;

    public Rate() {
        super();
    }

    public Rate(Date start, int duration) {
        this.startDate = start;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
