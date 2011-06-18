package org.hyperpath.services;

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
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.ReviewsJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "ReviewsServices")
@Stateless()
public class ReviewsServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  ReviewsJpaController controller;

  /**
   * Add new review
   */
  @WebMethod(operationName = "addReview")
  public void addReview(@WebParam(name = "review") Reviews review)
    throws
    Exception,
    PreexistingEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ReviewsJpaController(utx, emf);
    controller.create(review);
  }

  /**
   * Update review
   */
  @WebMethod(operationName = "updateReview")
  public void updateReview(@WebParam(name = "review") Reviews review)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ReviewsJpaController(utx, emf);
    controller.edit(review);
  }

  /**
   * Delete review
   */
  @WebMethod(operationName = "deleteReview")
  public void deleteReview(@WebParam(name = "reviewId") Integer reviewId)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ReviewsJpaController(utx, emf);
    controller.destroy(reviewId);
  }

  /**
   * Find reviews by service
   */
  @WebMethod(operationName = "findReviewsByService")
  public List<Reviews> findReviewsByService(@WebParam(name = "service") Services service)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ReviewsJpaController(utx, emf);
    return controller.findReviewsByService(service);
  }

  /**
   * Find reviews by client
   */
  @WebMethod(operationName = "findReviewsByClient")
  public List<Reviews> findReviewsByClient(@WebParam(name = "client") Clients client)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ReviewsJpaController(utx, emf);
    return controller.findReviewsByClient(client);
  }

}
