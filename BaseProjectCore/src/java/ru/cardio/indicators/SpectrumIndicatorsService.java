/*
 * стр. 28, 33-34
 * 
 */
package ru.cardio.indicators;

import java.util.List;
import ru.cardio.indicators.utils.Lomb;
import ru.cardio.indicators.utils.Periodogram;
import ru.cardio.indicators.utils.Square;

/**
 *
 * @author rogvold, Alexanter Taraymovich, Kirill Y. Tsibriy
 */
public class SpectrumIndicatorsService extends AbstractIndicatorsService {

    public SpectrumIndicatorsService() {
    }

    public SpectrumIndicatorsService(List<Integer> intervals) {
        super(intervals);
    }

    public SpectrumIndicatorsService(String id, List<Integer> intervals) {
        super(id, intervals);
    }

    public double getHFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.15, 0.4).Calculate();
    }

    public double getLFPercents() {
        List<Periodogram> periodogram = this.training.evaluate(new Lomb());
        return new Square(periodogram, 0.04, 0.15).Calculate();
    }

    public double getVLFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0033, 0.04).Calculate();
    }

    public double getULFPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0, 0.0033).Calculate();
    }

    public double getSdPercents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.1, 0.5).Calculate();
    }

    public double getS1Percents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0333, 0.1).Calculate();
    }

    public double getS2Percents() {
        List<Periodogram> periodogram = training.evaluate(new Lomb());
        return new Square(periodogram, 0.0167, 0.0333).Calculate();
    }

    public double getTP() {
        //TODO: implement =)
        return 0;
    }

    public double getIC() {
        return (getHFPercents() + getLFPercents()) / getVLFPercents();
    }
}
