package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T12:23:16")
@StaticMetamodel(Reviews.class)
public class Reviews_ { 

    public static volatile SingularAttribute<Reviews, Integer> id;
    public static volatile SingularAttribute<Reviews, String> description;
    public static volatile SingularAttribute<Reviews, Integer> rating;
    public static volatile ListAttribute<Reviews, Clients> clientsList;
    public static volatile ListAttribute<Reviews, Services> servicesList;

}