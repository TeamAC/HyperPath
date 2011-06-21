package org.hyperpath.services;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.jpa.ClientsJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "ClientsServices")
@Stateless()
public class ClientsServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  ClientsJpaController controller;

  /**
   * Find clients by address
   */
  @WebMethod(operationName = "findClientsByAddress")
  public List<Clients> findClientsByAddress(@WebParam(name = "address") Address address)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByAddress(address);
  }

  /**
   * Find clients by phones
   */
  @WebMethod(operationName = "findClientsByPhone")
  public List<Clients> findClientsByPhone(@WebParam(name = "phone") String phone)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByPhone(phone);
  }

  /**
   * Find clients by fax
   */
  @WebMethod(operationName = "findClientsByFax")
  public List<Clients> findClientsByFax(@WebParam(name = "fax") String fax)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByFax(fax);
  }

  /**
   * Find clients by mail
   */
  @WebMethod(operationName = "findClientsByMail")
  public List<Clients> findClientsByMail(@WebParam(name = "mail") String mail)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByMail(mail);
  }

  /**
   * find clients by name
   */
  @WebMethod(operationName = "findClientsByName")
  public List<Clients> findClientsByName(@WebParam(name = "clientName") String clientName)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByName(clientName);
  }

  /**
   * find clients by last name
   */
  @WebMethod(operationName = "findClientsByLastName")
  public List<Clients> findClientByLastName(@WebParam(name = "clientLastName") String clientLastName)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByLastName(clientLastName);
  }

  /**
   * Find clients by login
   */
  @WebMethod(operationName = "findClientsByLogin")
  public List<Clients> findClientsByLogin(@WebParam(name = "clientLogin") String clientLogin)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByLogin(clientLogin);
  }

  /**
   * Find clients by birth date
   */
  @WebMethod(operationName = "findClientsByBirthDate")
  public List<Clients> findClientsByBirthDate(@WebParam(name = "clientBirthDate") String clientBirthDate)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByBirthDate(clientBirthDate);
  }

  /**
   * Find clients by Age
   */
  @WebMethod(operationName = "findClientsByAge")
  public List<Clients> findClientsByAge(@WebParam(name = "clientAge") int clientAge)
    throws
    Exception,
    NonexistentEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new ClientsJpaController(emf);
    return controller.findClientsByAge(clientAge);
  }
}
