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
import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.jpa.EmailsJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "EmailsServices")
@Stateless()
public class EmailsServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  EmailsJpaController     controller;

  /**
   * List all mails
   */
  @WebMethod(operationName = "listAllEmails")
  public List<Emails> listAllEmails() throws Exception,
      RollbackFailureException, NonexistentEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    return controller.findEmailsEntities();
  }

  /**
   * Add new email address
   */
  @WebMethod(operationName = "addEmail")
  public void addEmail(@WebParam(name = "email") Emails email)
      throws Exception, PreexistingEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    controller.create(email);
  }

  /**
   * Update email address
   */
  @WebMethod(operationName = "updateEmail")
  public void updateEmail(@WebParam(name = "email") Emails email)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    controller.edit(email);
  }

  /**
   * Delete emacs address
   */
  @WebMethod(operationName = "deleteEmail")
  public void deleteEmail(@WebParam(name = "emailId") Integer emailId)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    controller.destroy(emailId);
  }

  /**
   * Find email address by exact address
   */
  @WebMethod(operationName = "findExactEmails")
  public List<Emails> findExactEmail(@WebParam(name = "email") String email)
      throws Exception, NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    return controller.findExactEmails(email);
  }

  /**
   * Find email address by address approximation
   */
  @WebMethod(operationName = "findAppoximateEmail")
  public List<Emails> findAppoximateEmail(@WebParam(name = "email") String email)
    throws Exception,
      NonexistentEntityException,
      RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    return controller.findApproximateEmails(email);
  }

  /**
   * Find total mails number
   */
  @WebMethod(operationName = "countEmails")
  public Integer countEmails() throws Exception, RollbackFailureException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new EmailsJpaController(utx, emf);
    return controller.getEmailsCount();
  }
}
