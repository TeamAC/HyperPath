package org.hyperpath.persistence.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T04:42:21")
@StaticMetamodel(OpeningHours.class)
public class OpeningHours_ { 

    public static volatile SingularAttribute<OpeningHours, Integer> id;
    public static volatile SingularAttribute<OpeningHours, Date> closeTime;
    public static volatile SingularAttribute<OpeningHours, Integer> days;
    public static volatile SingularAttribute<OpeningHours, Date> openTime;
    public static volatile ListAttribute<OpeningHours, Services> servicesList;

}