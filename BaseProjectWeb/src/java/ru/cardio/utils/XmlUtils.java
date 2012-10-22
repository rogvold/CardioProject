package ru.cardio.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.cardio.converters.DateConverter;
import ru.cardio.core.entity.Rate;
import ru.cardio.entity.Data;

/**
 *
 * @author rogvold
 */
public class XmlUtils {

    public static Data dataFromXML(String xml) {
        XStream xs = new XStream();
        xs.registerConverter(new DateConverter());
        xs.alias("start", Date.class);
        xs.alias("id", String.class);
        xs.alias("rate", Rate.class);
        xs.alias("root", Data.class);
        Data xd = (Data) xs.fromXML(xml);
        xd.updateRatesWithCalculateRatesStartDate();
        return xd;
    }

    public static Data dataFromJson(String json) {
        XStream xs = new XStream(new JettisonMappedXmlDriver());
        xs.registerConverter(new DateConverter());
        xs.alias("start", Date.class);
        xs.alias("id", String.class);
        xs.alias("rate", Rate.class);
        xs.alias("root", Data.class);
        Data xd = (Data) xs.fromXML(json);
        xd.updateRatesWithCalculateRatesStartDate();
        return xd;
    }

    public static Data simpleDataFromJson(String json) throws ParseException, java.text.ParseException {
        System.out.println("simpleDataFromJson: json = " + json);
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray ja = (JSONArray) jsonObj.get("rates");
        Object[] arr = ja.toArray();
        Date start = new Date();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        start = formatter.parse(jsonObj.get("start").toString());

        List<Rate> nrates = new ArrayList();
        
        Date d = new Date();
        d.setTime(start.getTime());
        for (Object o : arr) {
            Rate currRate = new Rate(d, Integer.parseInt(o.toString()));
            nrates.add(currRate);
            d = new Date();
            d.setTime(currRate.getDuration() + currRate.getStart().getTime());
        }
        Data dat = new Data();
        dat.setStart(start);
        dat.setRates(nrates);
        dat.setId(jsonObj.get("id").toString());
        return dat;
    }
}
