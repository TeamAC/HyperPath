package org.hyperpath.services;

import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.ServicesJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "ServicesServices")
@Stateless()
public class ServicesServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  ServicesJpaController controller;

  /**
   * Add new service
   */
  @WebMethod(operationName = "addService")
  public void addService(@WebParam(name = "newService") Services newService)
    throws
    Exception,
    PreexistingEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    controller.create(newService);
  }

  /**
   * Update service
   */
  @WebMethod(operationName = "updateService")
  public void updateService(@WebParam(name = "service") Services service)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    controller.edit(service);
  }

  /**
   * Delete service
   */
  @WebMethod(operationName = "deleteService")
  public void deleteService(@WebParam(name = "serviceId") Integer serviceId)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    controller.destroy(serviceId);
  }

  /**
   * Find services by label
   */
  @WebMethod(operationName = "findServicesByLabel")
  public List<Services> findServicesByLabel(@WebParam(name = "serviceLabel") String serviceLabel)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByLabel(serviceLabel);
  }

  /**
   * Find services by category
   */
  @WebMethod(operationName = "findServicesByCategory")
  public List<Services> findServicesByCategory(@WebParam(name = "category") Categories category)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByCategory(category);
  }

  /**
   * Find services by gps location
   */
  @WebMethod(operationName = "findServicesByGpsLocation")
  public List<Services> findServicesByGpsLocation(
                                                  @WebParam(name = "gpsLocation") Gpslocation gpsLocation,
                                                  @WebParam(name = "range") int range)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByGpsLocation(gpsLocation, range);
  }

  /**
   * Find services by category
   */
  @WebMethod(operationName = "findServiceByRating")
  public List<Services> findServiceByRating(
                                            @WebParam(name = "rating") int rating,
                                            @WebParam(name = "category") Categories category)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServiceByRating(rating, category);
  }

  /**
   * Find services by user
   */
  @WebMethod(operationName = "findServicesByUser")
  public List<Services> findServicesByUser(@WebParam(name = "client") Clients client)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByUser(client);
  }

  /**
   * Find services by address
   */
  @WebMethod(operationName = "findServicesByAddress")
  public List<Services> findServicesByAddress(@WebParam(name = "address") Address address)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByAddress(address);
  }

  /**
   * Find services by phone
   */
  @WebMethod(operationName = "findServicesPhone")
  public List<Services> findServicesPhone(@WebParam(name = "phone") String phone)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesPhone(phone);
  }

  /**
   * Find services by fax
   */
  @WebMethod(operationName = "findServicesByFax")
  public List<Services> findServicesByFax(@WebParam(name = "fax") String fax)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByFax(fax);
  }

  /**
   * Find services by mail
   */
  @WebMethod(operationName = "findServicesByMail")
  public List<Services> findServicesByMail(@WebParam(name = "mail") String mail)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByFax(mail);
  }

  /**
   * Find services by opening hours
   */
  @WebMethod(operationName = "findServicesByOpeningTime")
  public List<Services> findServicesByOpeningTime(
                                                  @WebParam(name = "category") Categories category,
                                                  @WebParam(name = "startTime") Date startTime)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByOpeningTime(category, startTime);
  }

  /**
   * Find services by closing hours
   */
  @WebMethod(operationName = "findServicesByClosingTime")
  public List<Services> findServicesByClosingTime(
                                                  @WebParam(name = "category") Categories category,
                                                  @WebParam(name = "endTime") Date endTime)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByClosingTime(category, endTime);
  }

  /**
   * Find services by both opening and closing hours
   */
  @WebMethod(operationName = "findServicesByTimeRange")
  public List<Services> findServicesByTimeRange(
                                                @WebParam(name = "category") Categories category,
                                                @WebParam(name = "startTime") Date startTime,
                                                @WebParam(name = "endTime") Date endTime)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ServicesJpaController(emf);
    return controller.findServicesByTimeRange(category, startTime, endTime);
  }
}
