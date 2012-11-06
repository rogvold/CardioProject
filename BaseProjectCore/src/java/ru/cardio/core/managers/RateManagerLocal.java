/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.core.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.jpa.entity.Rate;

/**
 *
 * @author rogvold
 */
@Local
public interface RateManagerLocal {
    public Rate getRateByStartDate(Date startDate);
    public List<Rate> getLastRates(int number);
    public boolean rateExists(Date startDate);
    public void addRate(Date startDate, Integer duration);
    public void addRates(List<Rate> rates);
    public String getPlotData(List<Rate> rates);
}
