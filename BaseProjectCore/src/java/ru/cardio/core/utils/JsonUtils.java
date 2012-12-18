package ru.cardio.core.utils;

import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.cardio.graphics.MyPoint;

/**
 *
 * @author rogvold
 */
public class JsonUtils {

    public static String getStringFromJson(String jsonString, String paramName) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(jsonString);
            return jsonObj.get(paramName).toString();
        } catch (Exception e) {
            return null;
        }

    }

    public static String getPlotData(List<MyPoint> points) {
        if ((points == null) || (points.isEmpty())) {
            return null;
        }
        String s = "[";
        for (int i = 0; i < points.size() - 1; i++) {
            MyPoint r = points.get(i);
            s = s + "[" + r.getX() + ", " + r.getY() + "], ";
        }
        MyPoint r = points.get(points.size() - 1);
        s = s + "[" + r.getX() + ", " + r.getY() + "]";
        s = s + "]";
        return s;
    }
}
