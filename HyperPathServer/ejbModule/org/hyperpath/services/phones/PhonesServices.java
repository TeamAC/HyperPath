package org.hyperpath.services.phones;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.jpa.PhonesJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "PhonesServices")
@Stateless()
public class PhonesServices {
  @Resource
  private UserTransaction utx;
  @PersistenceUnit
  EntityManagerFactory    emf;

  PhonesJpaController     controller;

  /**
   * Add new Phone
   */
  @WebMethod(operationName = "addPhones")
  public void addPhones(@WebParam(name = "phone") Phones phone)
      throws Exception, PreexistingEntityException,
      RollbackFailureException {
  	emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    controller.create(phone);
  }

  /**
   * Update an existing phone
   */
  @WebMethod(operationName = "updatePhones")
  public void updatePhones(@WebParam(name = "phone") Phones phone)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  	emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    controller.edit(phone);
  }

  /**
   * Delete a phone by id
   */
  @WebMethod(operationName = "deletePhones")
  public void deletePhones(@WebParam(name = "phoneId") Integer phoneId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  	emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    controller.destroy(phoneId);
  }

  /**
   * Find phone by exact phone number
   */
  @WebMethod(operationName = "findExactPhones")
  public List<Phones> findExactPhone(@WebParam(name = "phoneNumber") String phoneNumber)
    throws Exception, NonexistentEntityException,
    RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    return controller.findExactPhone(phoneNumber);
  }

  /**
   * Find phones by phone number approximation
   */
  @WebMethod(operationName = "findAppoximatePhones")
  public List<Phones> findAppoximatePhones(@WebParam(name = "phoneNumber") String phoneNumber)
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    return controller.findApproximatePhones(phoneNumber);
  }

  /**
   * Find total phone numbers
   */
  @WebMethod(operationName = "countPhones")
  public Integer countPhones() throws Exception, RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new PhonesJpaController(utx, emf);
    return controller.getPhonesCount();
  }
}
