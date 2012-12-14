package ru.cardio.core.jpa.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2012-12-14T13:32:04")
@StaticMetamodel(CardioSession.class)
public class CardioSession_ { 

    public static volatile SingularAttribute<CardioSession, Long> id;
    public static volatile SingularAttribute<CardioSession, Date> startDate;
    public static volatile SingularAttribute<CardioSession, Integer> status;
    public static volatile SingularAttribute<CardioSession, String> description;
    public static volatile SingularAttribute<CardioSession, Long> userId;
    public static volatile SingularAttribute<CardioSession, String> rates;
    public static volatile SingularAttribute<CardioSession, Date> endDate;

}