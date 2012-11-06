/**
 * Во всех классах получения показателей есть общие элементы - они вынесены в абстрактный класс.
 * 
 * В конструктор мы передаём массив интервалов. Их количество может быть совершенно разным. 
 * Нужно анализировать только первые N из них, которые суммарно составляют не больше duration миллисекунд(обычно рассматриваются 5-минутные образцы).
 * Если интервалов не набирается на duration миллисекунд, то анализируются они все. 
 */
package ru.cardio.indicators;

import java.util.List;

/**
 *
 * @author rogvold
 */
public abstract class AbstractIndicatorsService {

    public static final int DEFAULT_DURATION = 5 * 60 * 1000;
    
    protected List<Integer> intervals;
    protected int duration = DEFAULT_DURATION;

    public AbstractIndicatorsService(List<Integer> intervals) {
        this.intervals = intervals;
    }

    public void setIntervals(List<Integer> list) {
        this.intervals = list;
    }

    public void setDuration(int newDuration) {
        this.duration = newDuration;
    }

}
