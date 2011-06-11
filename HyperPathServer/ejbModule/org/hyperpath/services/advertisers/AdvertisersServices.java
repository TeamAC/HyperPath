/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.advertisers;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "AdvertisersServices")
@Stateless()
public class AdvertisersServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "addAdvertizer")
    public void addService(@WebParam(name = "advertizer")
    Advertisers service) throws Exception, PreexistingEntityException, RollbackFailureException {
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByAddress")
    public List<Services> findServicesByAddress(@WebParam(name = "address")
    Address address) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesPhone")
    public List<Services> findServicesPhone(@WebParam(name = "phone")
    String phone) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByFax")
    public List<Services> findServicesByFax(@WebParam(name = "fax")
    String fax) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByMail")
    public List<Services> findServicesByMail(@WebParam(name = "mail")
    String mail) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateAdvertiser")
    public void updateAdvertiser(@WebParam(name = "advertiser")
    Advertisers advertiser) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteAdvertiser")
    public void deleteAdvertiser(@WebParam(name = "advertiserId")
    Integer advertiserId) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdvertiserByAd")
    public Advertisers findAdvertiserByAd(@WebParam(name = "ad")
    Advertisers ad) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
