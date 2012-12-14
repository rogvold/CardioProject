package ru.cardio.core.jpa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2012-12-14T13:32:04")
@StaticMetamodel(Note.class)
public class Note_ { 

    public static volatile SingularAttribute<Note, Long> id;
    public static volatile SingularAttribute<Note, String> title;
    public static volatile SingularAttribute<Note, Long> duration;
    public static volatile SingularAttribute<Note, Long> sessionId;
    public static volatile SingularAttribute<Note, String> description;
    public static volatile SingularAttribute<Note, Long> startPoint;

}