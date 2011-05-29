package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Entities;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T12:23:16")
@StaticMetamodel(Emails.class)
public class Emails_ { 

    public static volatile SingularAttribute<Emails, Integer> id;
    public static volatile SingularAttribute<Emails, String> address;
    public static volatile ListAttribute<Emails, Entities> entitiesList;

}