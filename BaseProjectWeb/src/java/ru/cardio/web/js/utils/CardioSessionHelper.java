package ru.cardio.web.js.utils;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import ru.cardio.core.jpa.entity.CardioSession;

/**
 *
 * @author rogvold
 */
public class CardioSessionHelper {

    public String getTimelineJson(CardioSession cs) {
        SimpleSession ss = new SimpleSession(cs.getStartDate().getTime(), cs.getEndDate().getTime(), cs.getStartDate().toString(), cs.getId());
        Gson gson = new Gson();
        return gson.toJson(ss);
    }

    private List<SimpleSession> simpleSessionListFromSessionList(List<CardioSession> list){
        List<SimpleSession> l = new ArrayList();
        for (CardioSession cs: list){
            l.add(new SimpleSession(cs.getStartDate().getTime(), cs.getEndDate().getTime(), cs.getStartDate().toString(), cs.getId()));
        }
        return l;
    }
    
    public String getTimelineJsonFromSessionList(List<CardioSession> clist) {
        Gson gson = new Gson();
        return gson.toJson(simpleSessionListFromSessionList(clist));
    }
}
