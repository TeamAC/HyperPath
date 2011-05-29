package org.hyperpath.persistence.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-05-29T04:42:21")
@StaticMetamodel(Clients.class)
public class Clients_ { 

    public static volatile SingularAttribute<Clients, String> lastName;
    public static volatile SingularAttribute<Clients, String> name;
    public static volatile SingularAttribute<Clients, Date> birthdate;
    public static volatile SingularAttribute<Clients, String> gender;
    public static volatile SingularAttribute<Clients, Integer> Id;
    public static volatile SingularAttribute<Clients, String> login;
    public static volatile SingularAttribute<Clients, String> password;
    public static volatile ListAttribute<Clients, Services> servicesList;
    public static volatile SingularAttribute<Clients, Entities> entities;
    public static volatile ListAttribute<Clients, Reviews> reviewsList;

}