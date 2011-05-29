/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.ads;

import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "AdsServices")
@Stateless()
public class AdsServices {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsById")
    public List<Ads> findAds(@WebParam(name = "id")
    Integer id) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsByAdvertiser")
    @RequestWrapper(className = "org.findAdsByAdvertiser")
    @ResponseWrapper(className = "org.findAdsByAdvertiserResponse")
    public List<Ads> findAds(@WebParam(name = "advertiser")
    Advertisers advertiser) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsByService")
    @RequestWrapper(className = "org.findAdsByService")
    @ResponseWrapper(className = "org.findAdsByServiceResponse")
    public List<Ads> findAds(@WebParam(name = "service")
    Services service) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addAds")
    public Void addAds(@WebParam(name = "ads")
    Ads ads) throws Exception, RollbackFailureException, PreexistingEntityException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateAds")
    public void updateAds(@WebParam(name = "ads")
    Ads ads) throws Exception, RollbackFailureException, NonexistentEntityException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteAds")
    public void deleteAds(@WebParam(name = "id")
    Integer id) throws Exception, RollbackFailureException, NonexistentEntityException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsByStartDate")
    public List<Ads> findAdsByStartDate(@WebParam(name = "startDate")
    Date startDate) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsByEndDate")
    public List<Ads> findAdsByEndDate(@WebParam(name = "endDate")
    Date endDate) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findAdsInBetween")
    public List<Ads> findAdsInBetween(@WebParam(name = "startDate") Date startDate, @WebParam(name = "endDate")
    Date endDate) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
