package org.hyperpath.persistence.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T17:36:51")
@StaticMetamodel(Ads.class)
public class Ads_ { 

    public static volatile SingularAttribute<Ads, Date> startDate;
    public static volatile SingularAttribute<Ads, Advertisers> advertisers;
    public static volatile SingularAttribute<Ads, Services> services;
    public static volatile SingularAttribute<Ads, String> shortDescription;
    public static volatile SingularAttribute<Ads, String> description;
    public static volatile SingularAttribute<Ads, Integer> Id;
    public static volatile SingularAttribute<Ads, Date> endDate;

}