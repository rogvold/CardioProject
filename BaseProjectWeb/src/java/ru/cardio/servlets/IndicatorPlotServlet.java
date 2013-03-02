/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.cardio.core.jpa.entity.Constant;
import ru.cardio.core.managers.ConstantManagerLocal;
import ru.cardio.core.managers.IndicatorsManagerLocal;
import ru.cardio.graphics.MyPlot;
import ru.cardio.indicators.HRVIndicatorsService;

/**
 *
 * @author rogvold
 */
public class IndicatorPlotServlet extends HttpServlet {

    private static final int ACCURACY_INTERVAL = 20;
    @EJB
    IndicatorsManagerLocal indMan;
    @EJB
    ConstantManagerLocal cMan;
    private long accuracyInterval = ACCURACY_INTERVAL;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String sessionIdString = request.getParameter("sessionId");
            String paramName = request.getParameter("param");
            System.out.println("sessionId = " + sessionIdString + " ; param = " + paramName);
            out.print(plotHRVJsonString(paramName, Long.parseLong(sessionIdString)));
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
            Logger.getLogger(IndicatorPlotServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(IndicatorPlotServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    public String plotHRVJsonString(String paramName, Long sessionId) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new HRVIndicatorsService(), paramName, getAccuracyInterval() * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    private long getAccuracyInterval() {
        String step = cMan.getConstantValueByName(Constant.STEP_DURATION_NAME);
        if (step != null) {
            accuracyInterval = Long.parseLong(step);
        }

        return accuracyInterval;
    }
}
