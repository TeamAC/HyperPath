/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.openinghours;

import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 * 
 * @author adel
 */
@WebService(serviceName = "OpeningHoursServices")
@Stateless()
public class OpeningHoursServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  /**
   * Web service operation
   */
  @WebMethod(operationName = "addOpeningHours")
  public void addOpeningHours(
                              @WebParam(name = "openingHours") OpeningHours openingHours)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "deleteOpeningHours")
  public void deleteOpeningHours(
                                 @WebParam(name = "openingHoursId") Integer openingHoursId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "updateOpeningHours")
  public void updateOpeningHours(
                                 @WebParam(name = "openingHoursId") Integer openingHoursId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  }

}
