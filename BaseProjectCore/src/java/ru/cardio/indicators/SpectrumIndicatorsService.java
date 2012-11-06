/*
 * стр. 28, 33-34
 * 
 */
package ru.cardio.indicators;

import java.util.List;

/**
 *
 * @author rogvold, ???
 */
public class SpectrumIndicatorsService extends AbstractIndicatorsService {

    public SpectrumIndicatorsService(List<Integer> intervals) {
        super(intervals);
    }

    public double getHFPercents() {
        //TODO: implement =)
        return 20;
    }

    public double getLFPercents() {
        //TODO: implement =)
        return 30;
    }

    public double getVLFPercents() {
        //TODO: implement =)
        return 50;
    }

    public double getULFPercents() {
        //TODO: implement =)
        return 0;
    }

    public double getTP() {
        //TODO: implement =)
        return 0;
    }

    public double getIC() {
        //TODO: implement =)
        return 0;
    }
}
