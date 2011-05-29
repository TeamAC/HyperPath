package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Entities;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T12:23:16")
@StaticMetamodel(Phones.class)
public class Phones_ { 

    public static volatile SingularAttribute<Phones, Integer> id;
    public static volatile ListAttribute<Phones, Entities> entitiesList;
    public static volatile SingularAttribute<Phones, String> number;

}