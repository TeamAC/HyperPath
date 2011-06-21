package org.hyperpath.services;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.jpa.OpeningHoursJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "OpeningHoursServices")
@Stateless()
public class OpeningHoursServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  OpeningHoursJpaController controller;
  /**
   * Add new opening hour
   */
  @WebMethod(operationName = "addOpeningHours")
  public void addOpeningHours(@WebParam(name = "openingHours") OpeningHours openingHours)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new OpeningHoursJpaController(emf);
    controller.create(openingHours);
  }

  /**
   * Delete opening hour
   */
  @WebMethod(operationName = "deleteOpeningHours")
  public void deleteOpeningHours(@WebParam(name = "openingHoursId") Integer openingHoursId)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new OpeningHoursJpaController(emf);
    controller.destroy(openingHoursId);
  }

  /**
   * Update opening hour
   */
  @WebMethod(operationName = "updateOpeningHours")
  public void updateOpeningHours(@WebParam(name = "openingHoursId") Integer openingHoursId)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new OpeningHoursJpaController(emf);
    controller.destroy(openingHoursId);
  }

}
