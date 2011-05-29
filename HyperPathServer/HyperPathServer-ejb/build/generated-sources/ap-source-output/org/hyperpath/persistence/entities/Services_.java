package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.entities.Reviews;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T17:36:51")
@StaticMetamodel(Services.class)
public class Services_ { 

    public static volatile SingularAttribute<Services, Gpslocation> gpslocation;
    public static volatile ListAttribute<Services, Ads> adsList;
    public static volatile SingularAttribute<Services, OpeningHours> openingHours;
    public static volatile SingularAttribute<Services, String> description;
    public static volatile SingularAttribute<Services, Categories> categories;
    public static volatile SingularAttribute<Services, Integer> Id;
    public static volatile SingularAttribute<Services, String> label;
    public static volatile SingularAttribute<Services, Integer> rating;
    public static volatile ListAttribute<Services, Clients> clientsList;
    public static volatile SingularAttribute<Services, String> usersReview;
    public static volatile SingularAttribute<Services, Entities> entities;
    public static volatile ListAttribute<Services, Reviews> reviewsList;

}