package ru.cardio.graphics;

import java.util.List;
import org.json.simple.JSONObject;
import ru.cardio.core.utils.JsonUtils;


/**
 *
 * @author rogvold
 */
public class MyPlot {

    private List<MyPoint> points;
    private String name;

    public MyPlot(List<MyPoint> points, String name) {
        this.points = points;
        this.name = name;
    }

    public List<MyPoint> getPoints() {
        return points;
    }

    public void setPoints(List<MyPoint> points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonString() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("data", JsonUtils.getPlotData(points));

        return obj.toJSONString();
    }
}
