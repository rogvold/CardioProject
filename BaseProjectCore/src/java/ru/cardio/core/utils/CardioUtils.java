/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rogvold
 */
public class CardioUtils {
    
    public static List<Integer> getIntervalsFromString(String text, String delimiter){
        String ps = "([0-9]*"+delimiter+")*[0-9]+";
        Pattern patt = Pattern.compile(ps);
        Matcher matcher = patt.matcher(text);
        if (!matcher.matches()){
            System.out.println("incorrect data: " + text);
            return null;
        }
        List<Integer> list = new ArrayList();
        String[] array = text.split(delimiter);
        for (String s: array){
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
