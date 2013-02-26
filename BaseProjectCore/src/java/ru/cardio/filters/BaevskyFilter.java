package ru.cardio.filters;

import java.util.ArrayList;
import java.util.List;
import ru.cardio.indicators.StatisticsIndicatorsService;

/**
 *
 * @author rogvold
 */
public class BaevskyFilter implements FilterInterfaces {

    private static BaevskyFilter instance;
    private static final double BOTTOM_BORDER = 0.8;
    private static final double TOP_BORDER = 1.2;
    private static final double BOTTOM_DOUBLING = 1.7;

    public static FilterInterfaces getInstance() {
        if (instance == null) {
            instance = new BaevskyFilter();
        }
        return instance;
    }

    private Integer getAverage(List<Integer> rates) {
        Integer sum = 0;
        for (int i = 0; i < rates.size(); i++) {
            sum += rates.get(i);
        }
        return sum / rates.size();
    }

    private List<Integer> detectPVCs(List<Integer> rates) {
        List<Integer> list = new ArrayList();
        for (int i = 0; i < rates.size() - 2; i++) {
            if ((rates.get(i + 1) < BOTTOM_BORDER * rates.get(i)) && (rates.get(i + 2) > TOP_BORDER * rates.get(i))) {
                list.add(i);
            }
        }
        return list;
    }

    private List<Integer> processPVCs(List<Integer> rates) {
        List<Integer> indexes = detectPVCs(rates);
        for (int i = 0; i < indexes.size(); i++) {
            Integer newValue = (rates.get(indexes.get(i) + 1) + rates.get(indexes.get(i) + 2)) / 2;
            rates.set(indexes.get(i) + 1, newValue);
            rates.set(indexes.get(i) + 2, newValue);
        }
        return rates;
    }

    private List<Integer> processDoublings(List<Integer> rates) {
        List<Integer> list = new ArrayList();
        rates.add(0, getAverage(rates)); // do not forget the first element!

        for (int i = 1; i < rates.size() - 1; i++) {
            if ((rates.get(i) > rates.get(i - 1) * BOTTOM_DOUBLING) && (rates.get(i) > rates.get(i + 1) * BOTTOM_DOUBLING)) {
                list.add(rates.get(i) / 2);
                list.add(rates.get(i) / 2);
            } else {
                list.add(rates.get(i));
            }
        }
        //check the last one 
        if (rates.get(rates.size() - 1) > BOTTOM_DOUBLING * list.get(list.size() - 1)) {
            list.add(rates.get(rates.size() - 1) / 2);
            list.add(rates.get(rates.size() - 1) / 2);
        } else {
            list.add(rates.get(rates.size() - 1));
        }
        return list;
    }

    private boolean goodRatio(Integer a, Integer b) {
        if ((b < a * BOTTOM_BORDER) || (b > a * TOP_BORDER)) {
            return false;
        }
        return true;
    }

    private boolean statisticsChecking(Integer point, double m, double sigma) {
        if (Math.abs(m - point) < 4 * sigma) {
            return true;
        }
        return false;
    }

    private List<Integer> processBlowouts(List<Integer> rates) {
        List<Integer> list = new ArrayList();
        StatisticsIndicatorsService sis = new StatisticsIndicatorsService();
        sis.setIntervals(rates);
        double m = sis.getRRNN();
        double sigma = sis.getSDNN();
        
//        System.out.println("m = " + " ; sigma = " + sigma);
        
        rates.add(0, getAverage(rates)); // do not forget the first element!

        for (int i = 1; i < rates.size() - 1; i++) {
            if (!statisticsChecking(rates.get(i), m, sigma)) {
                continue;
            }
            if (!((!(goodRatio(rates.get(i - 1), rates.get(i)))) && (goodRatio(rates.get(i - 1), rates.get(i + 1))))) {
                list.add(rates.get(i));
            }
        }
        //check the last one 
        if (!((rates.get(rates.size() - 1) > TOP_BORDER * list.get(list.size() - 1)) || (rates.get(rates.size() - 1) < BOTTOM_BORDER * list.get(list.size() - 1)))) {
            list.add(rates.get(rates.size() - 1));
        }
        return list;
    }

    @Override
    public List<Integer> filterRates(List<Integer> rates) {
        rates = processPVCs(rates);
        rates = processDoublings(rates);
        rates = processBlowouts(rates);
        return rates;
    }
}
