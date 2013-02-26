package ru.cardio.core.managers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.Constant;
import ru.cardio.indicators.AbstractIndicatorsService;

/**
 *
 * @author rogvold
 */
@Stateless
public class ConstantManager implements ConstantManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    private Constant constantByName(String name) {
        Query q = em.createQuery("select c from Constant c where c.name = :name").setParameter("name", name);
        try {
            return (Constant) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private void initDefaultConstants() {
        if (constantByName(Constant.STEP_DURATION_NAME) == null) {
            createConstant(Constant.STEP_DURATION_NAME, Integer.toString(20));
        }

        if (constantByName(Constant.WINDOW_DURATION_NAME) == null) {
            createConstant(Constant.WINDOW_DURATION_NAME, Integer.toString(AbstractIndicatorsService.DEFAULT_DURATION));
        }
    }

    @Override
    public String getConstantValueByName(String name) {
        initDefaultConstants();// very bad style(((
        //TODO(Rogvold): implement initialization in singleton
        Constant c = constantByName(name);
        if (c == null) {
            return null;
        } else {
            return c.getValue();
        }
    }

    @Override
    public void createConstant(String name, String value) {
        if (constantByName(name) != null) {
            return;
        }
        Constant c = new Constant();
        c.setName(name);
        c.setValue(value);
        em.merge(c);
    }

    @Override
    public void updateConstant(String name, String newValue) {
        Constant c = constantByName(name);
        if (c == null) {
            createConstant(name, newValue);
        } else {
            c.setValue(newValue);
            em.merge(c);
        }
    }
}
