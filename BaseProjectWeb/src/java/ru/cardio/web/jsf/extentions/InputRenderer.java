package ru.cardio.web.jsf.extentions;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.html_basic.TextRenderer;
import java.io.IOException;
import javax.faces.context.ResponseWriterWrapper;

/**
 *
 * @author rogvold
 */
public class InputRenderer extends TextRenderer {
@Override
    protected void getEndTextToRender(FacesContext context,
            UIComponent component, String currentValue) throws IOException {
        final ResponseWriter originalResponseWriter = context.getResponseWriter();
        context.setResponseWriter(new ResponseWriterWrapper() {

            @Override
            public ResponseWriter getWrapped() {
                return originalResponseWriter;
            }   

            @Override
            public void startElement(String name, UIComponent component)
                    throws IOException {
                super.startElement(name, component);
                if(name!=null && name.equals("input")){
                    String attribute = (String)component.getAttributes().get("placeholder");
                    if ((attribute!=null) && (!attribute.isEmpty())) {
                        super.writeAttribute("placeholder", attribute, "placeholder");
                    } 
                }
            }   
        });
        super.getEndTextToRender(context, component, currentValue);
        context.setResponseWriter(originalResponseWriter); // Restore original writer.
    }

}
