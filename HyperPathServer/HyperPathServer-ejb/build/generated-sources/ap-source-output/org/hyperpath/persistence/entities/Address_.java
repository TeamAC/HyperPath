package org.hyperpath.persistence.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Entities;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T04:42:21")
@StaticMetamodel(Address.class)
public class Address_ { 

    public static volatile SingularAttribute<Address, Integer> id;
    public static volatile SingularAttribute<Address, String> zip;
    public static volatile ListAttribute<Address, Entities> entitiesList;
    public static volatile SingularAttribute<Address, String> department;
    public static volatile SingularAttribute<Address, String> street;
    public static volatile SingularAttribute<Address, String> ext;
    public static volatile SingularAttribute<Address, String> city;
    public static volatile SingularAttribute<Address, String> country;

}