package ru.cardio.utils;

import java.util.List;
import ru.cardio.graphics.MyPoint;

/**
 *
 * @author rogvold
 */
public class JsonUtils {

    public static String getPlotData(List<MyPoint> points) {
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
