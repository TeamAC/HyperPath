package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Entities;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T17:36:51")
@StaticMetamodel(Advertisers.class)
public class Advertisers_ { 

    public static volatile ListAttribute<Advertisers, Ads> adsList;
    public static volatile SingularAttribute<Advertisers, String> description;
    public static volatile SingularAttribute<Advertisers, String> name;
    public static volatile SingularAttribute<Advertisers, Integer> Id;
    public static volatile SingularAttribute<Advertisers, Entities> entities;

}