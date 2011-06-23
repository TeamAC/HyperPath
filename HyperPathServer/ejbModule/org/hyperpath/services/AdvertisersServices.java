package org.hyperpath.services;

import java.util.List;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.AdvertisersJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "AdvertisersServices")
@Stateless()
public class AdvertisersServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  AdvertisersJpaController controller;

  /**
   * Add new advertiser
   */
  @WebMethod(operationName = "addAdvertizer")
  public void addAdvertizer(@WebParam(name = "newAdvertizer") Advertisers newAdvertizer)
    throws
    Exception,
    PreexistingEntityException,
    RollbackFailureException
  {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new AdvertisersJpaController(emf);
    controller.create(newAdvertizer);
  }
//
//  /**
//   * Find advertiser by address
//   */
//  @WebMethod(operationName = "findAdvertisersByAddress")
//  public List<Services> findAdvertisersByAddress(@WebParam(name = "address") Address address)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    return controller.findAdvertisersByAddress(address);
//  }
//
//  /**
//   * Find advertiser by phone
//   */
//  @WebMethod(operationName = "findAdvertisersByPhone")
//  public List<Services> findAdvertiserByPhone(@WebParam(name = "phone") String phone)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    return controller.findAdvertisersByPhone(phone);
//  }
//
//  /**
//   * Find advertiser by fax
//   */
//  @WebMethod(operationName = "findAdvertisersByFax")
//  public List<Services> findAdvertisersByFax(@WebParam(name = "fax") String fax)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    return controller.findAdvertisersByFax(fax);
//  }
//
//  /**
//   * Find advertiser by mail
//   */
//  @WebMethod(operationName = "findAdvertisersByMail")
//  public List<Services> findAdvertisersByMail(@WebParam(name = "mail") String mail)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    return controller.findAdvertisersByMail(mail);
//  }
//
//  /**
//   * Update advertiser
//   */
//  @WebMethod(operationName = "updateAdvertiser")
//  public void updateAdvertiser(@WebParam(name = "advertiser") Advertisers advertiser)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    controller.edit(advertiser);
//  }
//
//  /**
//   * Delete advertiser
//   */
//  @WebMethod(operationName = "deleteAdvertiser")
//  public void deleteAdvertiser(@WebParam(name = "advertiserId") Integer advertiserId)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    controller.destroy(advertiserId);
//  }
//
//  /**
//   * Find advertiser by Ad
//   */
//  @WebMethod(operationName = "findAdvertiserByAd")
//  public Advertisers findAdvertiserByAd(@WebParam(name = "ad") Advertisers ad)
//    throws
//    Exception,
//    NonexistentEntityException,
//    RollbackFailureException
//  {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdvertisersJpaController(emf);
//    return controller.findAdvertiserByAd(ad);
//  }
}
