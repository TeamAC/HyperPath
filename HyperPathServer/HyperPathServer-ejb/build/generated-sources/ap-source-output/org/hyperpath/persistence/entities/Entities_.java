package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T04:42:21")
@StaticMetamodel(Entities.class)
public class Entities_ { 

    public static volatile SingularAttribute<Entities, Integer> id;
    public static volatile ListAttribute<Entities, Address> addressList;
    public static volatile ListAttribute<Entities, Emails> emailsList;
    public static volatile ListAttribute<Entities, Phones> phonesList;
    public static volatile ListAttribute<Entities, Faxes> faxesList;
    public static volatile ListAttribute<Entities, Clients> clientsList;
    public static volatile ListAttribute<Entities, Services> servicesList;
    public static volatile ListAttribute<Entities, Advertisers> advertisersList;

}