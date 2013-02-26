package ru.cardio.core.managers;

import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface ConstantManagerLocal {
    
    public String getConstantValueByName(String name);
    public void createConstant(String name, String value);
    public void updateConstant(String name, String newValue);
}
