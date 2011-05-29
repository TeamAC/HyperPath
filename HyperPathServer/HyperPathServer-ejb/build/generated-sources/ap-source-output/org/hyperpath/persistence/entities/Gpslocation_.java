package org.hyperpath.persistence.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T17:36:51")
@StaticMetamodel(Gpslocation.class)
public class Gpslocation_ { 

    public static volatile SingularAttribute<Gpslocation, Integer> id;
    public static volatile SingularAttribute<Gpslocation, Date> time;
    public static volatile SingularAttribute<Gpslocation, String> altitude;
    public static volatile SingularAttribute<Gpslocation, String> longitude;
    public static volatile SingularAttribute<Gpslocation, String> latitude;
    public static volatile ListAttribute<Gpslocation, Services> servicesList;

}