package ru.cardio.web.js.utils;

import java.util.Date;

/**
 *
 * @author rogvold
 */
public class SimpleSession {
    
    private Long start;
    private Long end;
    private String content;

    private Long id;

    public SimpleSession(Long start, Long end, String content, Long id) {
        this.start = start;
        this.end = end;
        this.content = content;
        this.id = id;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }


    
}
