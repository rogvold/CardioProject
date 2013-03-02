package ru.cardio.core.managers;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ru.cardio.core.entity.Rate;
import ru.cardio.core.jpa.entity.Constant;
import ru.cardio.graphics.MyPlot;
import ru.cardio.graphics.MyPoint;
import ru.cardio.indicators.*;

/**
 *
 * @author rogvold
 */
@Stateless
public class IndicatorsManager implements IndicatorsManagerLocal {

    
    
    @EJB
    CardioSessionManagerLocal cardMan;

    @EJB
    ConstantManagerLocal cMan;
    
    @Override
    public MyPlot getPlotOfParameters(Long sessionId, AbstractIndicatorsService a, String indicatorName, long msStep) throws Exception {
        if (sessionId == null) {
            return null;
        }
        List<Rate> rates = cardMan.getRatesInCardioSession(sessionId, -1);
        List<MyPoint> points = new ArrayList();
        String dur = cMan.getConstantValueByName(Constant.WINDOW_DURATION_NAME);
        if ( dur != null){
            a.setDuration(Integer.parseInt(dur) * 1000);
        }
        
        
//        System.out.println("getPlotOfParameters:  rates = " + rates);
        System.out.println("getPlotOfParameters: step = " + msStep + " duration = " + a.getDuration());
        
        
        int beginIndex = 0;
        int sum = 0;
        for (int i = 0; i < rates.size(); i++) {
            beginIndex = i;
            if (sum >= a.getDuration()) {
                break;
            }
            sum += rates.get(i).getDuration();
        }

        System.out.println("beginIndex = " + beginIndex);
        if (beginIndex == rates.size() - 1){
            System.out.println("not enough data...");
            return null;
        }
        
        
        int curr = beginIndex;
        int prev = 0;//not right
        List<Integer> list = new ArrayList();

        while (curr < rates.size()) {
            list.clear();
            for (int i = prev; i < curr; i++) {
                list.add(rates.get(i).getDuration());
            }
            a.setIntervals(list);
            System.out.println("calculating indicator " + indicatorName );

            points.add(new MyPoint(rates.get(curr).getStartDate().getTime(), a.parameter(indicatorName)));

            int t = 0;
            int curr2 = curr, prev2 = prev;
            for (int u = curr; u < rates.size(); u++) {
                if (t >= msStep) {
                    break;
                }
                t += rates.get(u).getDuration();
                curr2++;
                prev2++;
            }
            prev = prev2;
            curr = curr2;
        }

        MyPlot myPlot = new MyPlot(points, indicatorName);
        return myPlot;
    }

    private List<AbstractIndicatorsService> getAllAvailableIndicatorServices() {
        List<AbstractIndicatorsService> list = new ArrayList();

        list.add(new TimeIndicatorsService());
        list.add(new StatisticsIndicatorsService());
        list.add(new SpectrumIndicatorsService());
        list.add(new HRVIndicatorsService());

        return list;
    }

    @Override
    public List<MyPlot> getAllImplementedParametersPlots(Long sessionId, long msStep) throws Exception {
        List<AbstractIndicatorsService> servicesInstances = getAllAvailableIndicatorServices();
        List<MyPlot> list = new ArrayList();
        for (AbstractIndicatorsService ais : servicesInstances) {
            List<String> names = ais.allParametersNames();
            for (String s : names) {
                list.add(getPlotOfParameters(sessionId, ais, s, msStep));
            }
        }
        return list;
    }

    @Override
    public String getJsonOfAllImplementedParametersPlots(Long sessionId, long msStep) throws Exception {
        List<MyPlot> allPlots = getAllImplementedParametersPlots(sessionId, msStep);
        // TODO: implement

        return null;
    }
}
