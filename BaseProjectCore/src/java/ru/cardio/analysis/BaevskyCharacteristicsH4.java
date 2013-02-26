package ru.cardio.analysis;

import java.util.List;
import ru.cardio.indicators.HRVIndicatorsService;
import ru.cardio.indicators.StatisticsIndicatorsService;

/**
 *
 * @author rogvold
 */
public class BaevskyCharacteristicsH4 extends Characteristics {

    public BaevskyCharacteristicsH4(List<Integer> rates) {
        super(rates);
    }

    @Override
    public String getName() {
        return "stability of regulation";
    }

    @Override
    public CharacteristicsScore getResult() {
        HRVIndicatorsService hrv = new HRVIndicatorsService(rates);
        StatisticsIndicatorsService sis = new StatisticsIndicatorsService(rates);
        double m = sis.getRRNN();
        double sigma = sis.getSDNN();
        double v = 100.0 * sigma / m;

        if (v <= 3) {
            return new CharacteristicsScore(2, " dysregulation");
        }

        if (v >= 6) {
            return new CharacteristicsScore(-2, " dysregulation");
        }

        return new CharacteristicsScore(0, "Stable dysregulation");
    }
}
