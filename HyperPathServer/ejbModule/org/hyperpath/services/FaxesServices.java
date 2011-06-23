package org.hyperpath.services;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.jpa.FaxesJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "FaxesServices")
@Stateless()
public class FaxesServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  FaxesJpaController     controller;

  /**
   * List all faxes
   */
  @WebMethod(operationName = "listAllFaxes")
  public List<Faxes> listAllFaxes() throws Exception,
      RollbackFailureException, NonexistentEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new FaxesJpaController(emf);
    return controller.findFaxesEntities();
  }
//
//  /**
//   * Add new fax
//   */
//  @WebMethod(operationName = "addFax")
//  public void addFax(@WebParam(name = "fax") Faxes fax)
//      throws Exception, PreexistingEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    controller.create(fax);
//  }
//
//  /**
//   * Update fax number
//   */
//  @WebMethod(operationName = "updateFax")
//  public void updateFax(@WebParam(name = "fax") Faxes fax)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    controller.edit(fax);
//  }
//
//  /**
//   * Delete fax number
//   */
//  @WebMethod(operationName = "deleteFax")
//  public void deleteFax(@WebParam(name = "faxId") Integer faxId)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    controller.destroy(faxId);
//  }
//
//  /**
//   * Find fax by exact fax number
//   */
//  @WebMethod(operationName = "findExactFaxes")
//  public List<Faxes> findExactFax(@WebParam(name = "fax") String faxNumber)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    return controller.findExactFaxes(faxNumber);
//  }
//
//  /**
//   * Find fax by number approximation
//   */
//  @WebMethod(operationName = "findAppoximateFax")
//  public List<Faxes> findAppoximateFax(@WebParam(name = "fax") String faxNumber)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    return controller.findApproximateFaxes(faxNumber);
//  }
//
//  /**
//   * Find total fax number
//   */
//  @WebMethod(operationName = "countFaxes")
//  public Integer countFaxes() throws Exception, RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new FaxesJpaController(emf);
//    return controller.getFaxesCount();
//  }
}