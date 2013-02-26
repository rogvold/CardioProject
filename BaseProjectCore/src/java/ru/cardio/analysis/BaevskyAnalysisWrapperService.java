package ru.cardio.analysis;

import java.util.List;
import ru.cardio.indicators.AbstractIndicatorsService;

/**
 *
 * @author rogvold
 */
public class BaevskyAnalysisWrapperService extends AbstractIndicatorsService {

    public BaevskyAnalysisWrapperService(List<Integer> intervals) {
        super(intervals);
    }

    public BaevskyAnalysisWrapperService() {
    }
    
    public Integer getP(){
        CharacteristicsScore cs = BaevskyAnalysis.getInstance().getCharacteristics(intervals);
        return cs.getScore();
    }
    
}
