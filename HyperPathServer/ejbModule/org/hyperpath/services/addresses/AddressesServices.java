package org.hyperpath.services.addresses;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.AddressJpaController;
import org.hyperpath.persistence.jpa.EmailsJpaController;
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

  /**
   * list all address
   */
  @WebMethod(operationName = "listAllAddress")
  public List<Address> listAllAddress() throws Exception,
      RollbackFailureException, NonexistentEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    AddressJpaController controller = new AddressJpaController(utx, emf);
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
    try {
      if (controller.findAddress(address) == null) {
        controller.create(address);
      } else {
        throw new PreexistingEntityException(
            "Address already present in the database");
      }
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Update existing address
   */
  @WebMethod(operationName = "updateAddress")
  public void updateAddress(@WebParam(name = "address") Address address)
      throws Exception, RollbackFailureException,
      PreexistingEntityException {
    AddressJpaController controller = new AddressJpaController(utx, emf);
    try {
      if (address.getId() != null) {
        controller.edit(address);
      } else {
        throw new Exception(
            "To update address the id field must be specified");
      }
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Delete an existing address
   */
  @WebMethod(operationName = "deleteAddress")
  public void deleteAddress(@WebParam(name = "address") Address address)
      throws Exception, RollbackFailureException,
      NonexistentEntityException {
    AddressJpaController controller = new AddressJpaController(utx, emf);
    try {
      Integer id;
      if ((id = address.getId()) != null
          || controller.findAddress(address) != null) {
        controller.destroy(id);
      } else {
        throw new Exception(
            "To delete address the id field must be specified or the address must match one in the database");
      }
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Find address by textual approximation
   */
  @WebMethod(operationName = "findAddress")
  public Address findAddress(
                             @WebParam(name = "category") Categories category,
                             @WebParam(name = "address") String address)
    throws Exception,
      NonexistentEntityException {
    AddressJpaController a_controller = new AddressJpaController(utx, emf);
    try {
      if (category.getId() != null) {
        EntityManager em = emf.createEntityManager();
        em.createNamedQuery(address);
      }
    } catch (Exception e) {
      throw e;
    }
    return null;
  }

  /**
   * Find address by location and range
   */
  @WebMethod(operationName = "findAddressByLocation")
  @RequestWrapper(className = "org.findAddressByLocation")
  @ResponseWrapper(className = "org.findAddressByLocationResponse")
  public Address findAddressByLocation(
                                       @WebParam(name = "category") Categories category,
                                       @WebParam(name = "location") String location,
                                       @WebParam(name = "range") int range)
      throws NonexistentEntityException {
    // TODO write your implementation code here:
    return null;
  }

  /**
   * Find services based on their address
   */
  @WebMethod(operationName = "findServiceByAddress")
  public List<org.hyperpath.persistence.entities.Services> findServiceByAddress(
                                                                                @WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {
    try {
      if (address.getId() == null)
        throw new Exception(
            "Address id must be initialiazed before using search");
      EntityManager em = emf.createEntityManager();
      Query qr = em.createNamedQuery("Address.findServiceByAddress");
      qr.setParameter("addressId", address.getId());
      List<Services> rs = qr.getResultList();
      if (rs.isEmpty())
        throw new NonexistentEntityException(
            "There is no service in this address");
      return rs;
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Find advertisers base on their address
   */
  @WebMethod(operationName = "findAdvertizerByAddress")
  public List<org.hyperpath.persistence.entities.Advertisers> findAdvertizerByAddress(
                                                                                      @WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {

    try {
      if (address.getId() == null)
        throw new Exception(
            "Address id must be initialiazed before using search");
      EntityManager em = emf.createEntityManager();
      Query qr = em.createNamedQuery("Address.findAdvertiserByAddress");
      qr.setParameter("addressId", address.getId());
      List<Advertisers> rs = qr.getResultList();
      if (rs.isEmpty())
        throw new NonexistentEntityException(
            "There is no advertizers in this address");
      return rs;
    } catch (Exception e) {
      throw e;
    }

  }

  /**
   * Find clients based on their address
   */
  @WebMethod(operationName = "findClientByAddress")
  public List<Clients> findClientByAddress(
                                           @WebParam(name = "address") Address address)
    throws Exception,
      NonexistentEntityException {
    try {
      if (address.getId() == null) {
        throw new Exception(
            "Address id must be initialiazed before using search");
      }
      EntityManager em = emf.createEntityManager();
      Query qr = em.createNamedQuery("Address.findClientByAddress");
      qr.setParameter("clientId", address.getId());
      List<Clients> result = qr.getResultList();
      if (result.isEmpty()) {
        throw new NonexistentEntityException(
            "There is no client living at this address");
      }
      return result;
    } catch (Exception e) {
      throw e;
    }
  }
}
