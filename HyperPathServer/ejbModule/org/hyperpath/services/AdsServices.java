package org.hyperpath.services;

import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.AdsJpaController;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

@WebService(serviceName = "AdsServices")
@Stateless()
public class AdsServices {

  @PersistenceUnit
  EntityManagerFactory    emf;

  AdsJpaController controller;

  /**
   * find Ads by Id
   */
//  @WebMethod(operationName = "findAdsById")
//  public Ads findAds(@WebParam(name = "id") Integer id)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAds(id);
//  }

  /**
   * find Ads by advertiser
   */
//  @WebMethod(operationName = "findAdsByAdvertiser")
//  @RequestWrapper(className = "org.findAdsByAdvertiser")
//  @ResponseWrapper(className = "org.findAdsByAdvertiserResponse")
//  public List<Ads> findAds(@WebParam(name = "advertiser") Advertisers advertiser)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByAdvertiser(advertiser);
//  }

  /**
   * find Ads by service
   */
//  @WebMethod(operationName = "findAdsByService")
//  @RequestWrapper(className = "org.findAdsByService")
//  @ResponseWrapper(className = "org.findAdsByServiceResponse")
//  public List<Ads> findAds(@WebParam(name = "service") Services service)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByService(service);
//  }

  /**
   * Add new add
   */
  @WebMethod(operationName = "addAds")
  public void addAds(@WebParam(name = "ads") Ads ads) throws Exception,
      RollbackFailureException, PreexistingEntityException {
    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
    controller = new AdsJpaController(emf);
    controller.create(ads);
  }

  /**
   * Update ads
   */
//  @WebMethod(operationName = "updateAds")
//  public void updateAds(@WebParam(name = "ads") Ads ads) throws Exception,
//      RollbackFailureException, NonexistentEntityException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    controller.edit(ads);
//  }

  /**
   * Delete ads
   */
//  @WebMethod(operationName = "deleteAds")
//  public void deleteAds(@WebParam(name = "id") Integer id) throws Exception,
//      RollbackFailureException, NonexistentEntityException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    controller.destroy(id);
//  }

  /**
   * Find Ads by start date
   */
//  @WebMethod(operationName = "findAdsByStartDate")
//  public List<Ads> findAdsByStartDate(@WebParam(name = "startDate") Date startDate)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByStartDate(startDate);
//  }

  /**
   * Find Ads by end date
   */
//  @WebMethod(operationName = "findAdsByEndDate")
//  public List<Ads> findAdsByEndDate(@WebParam(name = "endDate") Date endDate)
//      throws Exception, NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByEndDate(endDate);
//  }

  /**
   * Find Ads between two dates
   */
//  @WebMethod(operationName = "findAdsInBetween")
//  public List<Ads> findAdsInBetween(
//                                    @WebParam(name = "startDate") Date startDate,
//                                    @WebParam(name = "endDate") Date endDate)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsInBetween(startDate, endDate);
//  }

  /**
   * List all Ads
   */
//  @WebMethod(operationName = "listAllAds")
//  public List<Ads> listAllAds()
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsEntities();
//  }

  /**
   * Find total categories number
   */
//  @WebMethod(operationName = "countAds")
//  public int countAds()
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.getAdsCount();
//  }

  /**
   * Find Ads by exact description
   */
//  @WebMethod(operationName = "findAdsByExactDescription")
//  public List<Ads> findAdsByExactDescription(@WebParam(name = "adsDescription") String adsDescription)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByExactDescription(adsDescription);
//  }

  /**
   * Find Ads by exact short description
   */
//  @WebMethod(operationName = "findAdsByExactShortDescription")
//  public List<Ads> findAdsByExactShortDescription(@WebParam(name = "adsDescription") String adsShortDescription)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByExactDescription(adsShortDescription);
//  }

  /**
   * Find Ads by approximate description
   */
//  @WebMethod(operationName = "findAdsByApproximateDescription")
//  public List<Ads> findAdsByApproximateDescription(@WebParam(name = "adsDescription") String adsDescription)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByApproximateDescription(adsDescription);
//  }

  /**
   * Find Ads by approximate short description
   */
//  @WebMethod(operationName = "findAdsByApproximateShortDescription")
//  public List<Ads> findAdsByApproximateShortDescription(@WebParam(name = "adsDescription") String adsShortDescription)
//    throws Exception,
//      NonexistentEntityException,
//      RollbackFailureException {
//    emf = Persistence.createEntityManagerFactory("HyperPathServerPU");
//    controller = new AdsJpaController(emf);
//    return controller.findAdsByApproximateShortDescription(adsShortDescription);
//  }

}
