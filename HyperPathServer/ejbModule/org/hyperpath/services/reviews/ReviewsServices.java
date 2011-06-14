/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.reviews;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 * 
 * @author adel
 */
@WebService(serviceName = "ReviewsServices")
@Stateless()
public class ReviewsServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  /**
   * Web service operation
   */
  @WebMethod(operationName = "addReview")
  public void addReview(@WebParam(name = "review") Reviews review)
      throws Exception, PreexistingEntityException,
      RollbackFailureException {
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "updateReview")
  public void updateReview(@WebParam(name = "review") Reviews review)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "deleteReview")
  public void deleteReview(@WebParam(name = "reviewId") Integer reviewId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "findReviewByService")
  public List<Reviews> findReviewByService(
                                           @WebParam(name = "service") Services service)
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    // TODO write your implementation code here:
    return null;
  }

  /**
   * Web service operation
   */
  @WebMethod(operationName = "findReviewByClient")
  public List<Reviews> findReviewByClient(
                                          @WebParam(name = "client") Clients client)
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    // TODO write your implementation code here:
    return null;
  }

}
