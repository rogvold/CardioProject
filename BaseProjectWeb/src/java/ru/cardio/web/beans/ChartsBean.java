package ru.cardio.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import ru.cardio.analysis.BaevskyAnalysisWrapperService;
import ru.cardio.core.jpa.entity.CardioSession;
import ru.cardio.core.managers.CardioSessionManagerLocal;
import ru.cardio.core.managers.IndicatorsManagerLocal;
import ru.cardio.graphics.MyPlot;
import ru.cardio.indicators.HRVIndicatorsService;
import ru.cardio.indicators.SpectrumIndicatorsService;
import ru.cardio.indicators.StatisticsIndicatorsService;
import ru.cardio.indicators.TimeIndicatorsService;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class ChartsBean {

    private static final String FIELD_ID = "id";
    private static final int ACCURACY_INTERVAL = 20;
    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    IndicatorsManagerLocal indMan;
    private Long sessionId;
    private CardioSession cardioSession;

    @PostConstruct
    private void init() {
        String s = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(FIELD_ID);
        System.out.println("init(): try to get id: s = " + s);
        try {
            this.sessionId = Long.parseLong(s);
            cardioSession = cardMan.getCardioSessionById(sessionId);
        } catch (Exception e) {
            System.out.println("init(): exception  e = " + e.toString());

        }
    }

    public String plotHRVJsonString(String paramName) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new HRVIndicatorsService(), paramName, ACCURACY_INTERVAL * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    public String plotTISJsonString(String paramName) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new TimeIndicatorsService(), paramName, ACCURACY_INTERVAL * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    public String plotStisJsonString(String paramName) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new StatisticsIndicatorsService(), paramName, ACCURACY_INTERVAL * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    public String plotBaevskyJsonString(String paramName) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new BaevskyAnalysisWrapperService(), paramName, ACCURACY_INTERVAL * 1000);
        return plot == null ? "" : plot.getJsonString();
    }

    public String plotSpectralJsonString(String paramName) throws Exception {
        System.out.println("paramName = " + paramName);
        MyPlot plot = indMan.getPlotOfParameters(sessionId, new SpectrumIndicatorsService(), paramName, ACCURACY_INTERVAL * 1000);
        return plot == null ? "" : plot.getJsonString();
    }
}
