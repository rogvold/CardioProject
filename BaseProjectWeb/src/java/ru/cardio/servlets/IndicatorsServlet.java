
package ru.cardio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import ru.cardio.core.managers.IndicatorsManagerLocal;
import ru.cardio.indicators.HRVIndicatorsService;
import ru.cardio.indicators.SpectrumIndicatorsService;
import ru.cardio.indicators.StatisticsIndicatorsService;
import ru.cardio.indicators.TimeIndicatorsService;

/**
 *
 * @author rogvold
 */
@WebServlet(name = "indicators", urlPatterns = {"/indicators"})
public class IndicatorsServlet extends HttpServlet {

    @EJB
    IndicatorsManagerLocal indMan;
    
    private final static String INTERVALS_FIELD = "intervals";
    private List<Integer> intervals;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String getJsonIndicators() throws Exception {
        JSONObject obj = new JSONObject();

        TimeIndicatorsService tis = new TimeIndicatorsService(intervals);
        obj.put("PNN50", tis.getPNN50());

        StatisticsIndicatorsService stis = new StatisticsIndicatorsService(intervals);
        obj.put("RRNN", stis.getRRNN());
        obj.put("SDNN", stis.getSDNN());

        SpectrumIndicatorsService spis = new SpectrumIndicatorsService(intervals);
        obj.put("TP", spis.getTP());
        obj.put("IC", spis.getIC());
        obj.put("HFPercents", spis.getHFPercents());
        obj.put("LFPercents", spis.getLFPercents());
        obj.put("VLFPercents", spis.getVLFPercents());
        obj.put("ULFPercents", spis.getULFPercents());

        HRVIndicatorsService hrvis = new HRVIndicatorsService(intervals);
        obj.put("AMoPercents", hrvis.getAMoPercents());
        obj.put("IN", hrvis.getIN());
        obj.put("BP", hrvis.getBP());

//        System.out.println("testing myplot ejb ; : " + indMan.getPlotOfParameters(251L, stis, "RRNN", 30000).getJsonString() );
        
        
        return obj.toJSONString();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            String s = request.getParameter(INTERVALS_FIELD);
            String[] mas = s.split(",");
//            out.println("mas = " + mas);
            intervals = new ArrayList();
            for (String s1 : mas) {
                intervals.add(Integer.parseInt(s1));
            }
            out.println(getJsonIndicators());


        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(IndicatorsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(IndicatorsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
