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
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.AddressJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "AddressesServices")
@Stateless()
public class AddressesServices {
  @Resource
  private UserTransaction utx;

  @PersistenceUnit
  EntityManagerFactory    emf;

  AddressJpaController controller;

  /**
   * list all address
   */
  @WebMethod(operationName = "listAllAddress")
  public List<Address> listAllAddress() throws Exception,
      RollbackFailureException, NonexistentEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new AddressJpaController(utx, emf);
    return controller.findAddressEntities();
  }

  /**
   * Add new address
   */
  @WebMethod(operationName = "addAddress")
  public void addAddress(@WebParam(name = "address") Address address)
      throws Exception, RollbackFailureException,
      PreexistingEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    AddressJpaController controller = new AddressJpaController(utx, emf);
    controller.create(address);
  }

  /**
   * Update existing address
   */
  @WebMethod(operationName = "updateAddress")
  public void updateAddress(@WebParam(name = "address") Address address)
      throws Exception, RollbackFailureException,
      PreexistingEntityException {
    controller = new AddressJpaController(utx, emf);
    controller.edit(address);
  }

  /**
   * Delete an existing address
   */
  @WebMethod(operationName = "deleteAddress")
  public void deleteAddress(@WebParam(name = "address") Address address)
      throws Exception, RollbackFailureException,
      NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    controller.destroy(address.getId());
  }

  /**
   * Find address by textual approximation
   */
  @WebMethod(operationName = "findApproximateAddressesByCategory")
  public List<Address> findApproximateAddressesByCategory(
                             @WebParam(name = "category") Categories category,
                             @WebParam(name = "address") String address)
    throws Exception,
      NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    return controller.findApproximateAddressesByCategory(category, address);
  }

  /**
   * Find address by location and range
   */
  @WebMethod(operationName = "findAddressByLocation")
  @RequestWrapper(className = "org.findAddressByLocation")
  @ResponseWrapper(className = "org.findAddressByLocationResponse")
  public List<Address> findAddressByLocation(
                                       @WebParam(name = "category") Categories category,
                                       @WebParam(name = "location") String location,
                                       @WebParam(name = "range") int range)
      throws NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    return controller.findAddressByRange(category, location, range);
  }

  /**
   * Find services based on their address
   */
  @WebMethod(operationName = "findServicesByAddress")
  public List<Services> findServicesByAddress(@WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    return controller.findServicesByAddress(address);
  }

  /**
   * Find advertisers base on their address
   */
  @WebMethod(operationName = "findAdvertizersByAddress")
  public List<Advertisers> findAdvertizersByAddress(@WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    return controller.findAdvertizersByAddress(address);
  }

  /**
   * Find clients based on their address
   */
  @WebMethod(operationName = "findClientsByAddress")
  public List<Clients> findClientsByAddress(@WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {
    controller = new AddressJpaController(utx, emf);
    return controller.findClientsByAddress(address);
  }
}
