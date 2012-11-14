package ru.cardio.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rogvold
 */
public class CardioUtils {
    
    public static List<Integer> getIntervalsFromString(String text, String delimiter){
        List<Integer> list = new ArrayList();
        String[] array = text.split(delimiter);
        for (String s: array){
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
