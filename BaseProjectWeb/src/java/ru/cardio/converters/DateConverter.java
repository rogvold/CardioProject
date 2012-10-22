package ru.cardio.converters;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author rogvold
 */
public class DateConverter implements Converter {

    private SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public boolean canConvert(Class clazz) {
        // This converter is only for Calendar fields.
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        Date date = (Date) value;
//        Date date = calendar.getTime();
        writer.setValue(formatter.format(date));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Date date  = new Date();
        try {
            date = formatter.parse(reader.getValue());
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage(), e);
        }
        return date;
    }
}