package ru.cardio.web.test;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.json.simple.parser.ParseException;
import ru.cardio.core.managers.RateManagerLocal;
import ru.cardio.entity.Data;
import ru.cardio.utils.XmlUtils;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class TestBean {

    @EJB 
    RateManagerLocal rm;
    
    private String text;
    
    private String notif;

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void testXML() {
        System.out.println("xd = " + XmlUtils.dataFromXML(text));
    }

    public void testJson() {
        System.out.println("jd = " + XmlUtils.dataFromJson(text));
    }
    
//    public void testDB(){
//        Data d = XmlUtils.dataFromJson(text);
//        System.out.println("d.getRates() = " + d.getRates());
//        rm.addRates(d.getRates());
//    }
//    
//    public void testSimpleJson() throws ParseException, java.text.ParseException{
//        Data d = XmlUtils.simpleDataFromJson(text);
//        System.out.println("data = " + d);
//    }
//    
//    public void ololo(){
//        System.out.println("ololo");
//        notif="ann";
//    }
    
}
