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
import ru.cardio.core.jpa.entity.Rate;
import ru.cardio.entity.Data;

/**
 *
 * @author rogvold
 */
public class XmlUtils {

    public static final String JSON_FIELD_RATES = "rates";
    public static final String JSON_FIELD_START = "start";
    public static final String JSON_FIELD_ID = "id";
    public static final String JSON_FIELD_EMAIL = "email";
    public static final String JSON_CREATE_SESSION = "create";
    public static final String JSON_FIELD_PASSWORD = "password";
    public static final String DATE_MASK = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String JSON_CREATE_SESSION_VALUE_TRUE = "1";
    public static final String JSON_CREATE_SESSION_VALUE_FALSE = "0";

    public static Data dataFromXML(String xml) {
        XStream xs = new XStream();
        xs.registerConverter(new DateConverter());
        xs.alias("start", Date.class);
        xs.alias("id", String.class);
        xs.alias("rate", Rate.class);
        xs.alias("root", Data.class);
        Data xd = (Data) xs.fromXML(xml);
//        xd.updateRatesWithCalculateRatesStartDate();
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
//        xd.updateRatesWithCalculateRatesStartDate();
        return xd;
    }

    public static Data simpleDataFromJson(String json) throws ParseException, java.text.ParseException {
        System.out.println("simpleDataFromJson: json = " + json);
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray ja = (JSONArray) jsonObj.get(XmlUtils.JSON_FIELD_RATES);
        Object[] arr = ja.toArray();
        Date start = new Date();

        DateFormat formatter = new SimpleDateFormat(XmlUtils.DATE_MASK);
        start = formatter.parse(jsonObj.get(XmlUtils.JSON_FIELD_START).toString());

        List<Integer> nrates = new ArrayList();
        for (Object o : arr) {
            nrates.add(Integer.parseInt(o.toString()));
        }
        Data dat = new Data();
        dat.setStart(start);
        dat.setRates(nrates);
        try {
            dat.setId(jsonObj.get(XmlUtils.JSON_FIELD_ID).toString());
        } catch (Exception e) {
        }
//        dat.setId(jsonObj.get(XmlUtils.JSON_FIELD_ID).toString());
        dat.setEmail(jsonObj.get(XmlUtils.JSON_FIELD_EMAIL).toString());
        dat.setPassword(jsonObj.get(XmlUtils.JSON_FIELD_PASSWORD).toString());

        String createSession = jsonObj.get(XmlUtils.JSON_CREATE_SESSION).toString();
        dat.setShouldCreateSession(createSession.equals(XmlUtils.JSON_CREATE_SESSION_VALUE_TRUE) ? true : false);

        return dat;
    }
}
