/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.advertisers;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Gpslocation;
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

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addService")
    public void addService(@WebParam(name = "service")
    Services service) throws Exception, PreexistingEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateService")
    public void updateService(@WebParam(name = "service")
    Services service) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteService")
    public void deleteService(@WebParam(name = "serviceId")
    Integer serviceId) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServiceByLabel")
    public List<Services> findService(@WebParam(name = "serviceLabel")
    String serviceLabel) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByCategory")
    public List<Services> findServicesByCategory(@WebParam(name = "category")
    Categories category) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByGpsLocation")
    public List<Services> findServicesByGpsLocation(@WebParam(name = "gpsLocation")
    Gpslocation gpsLocation, @WebParam(name = "range")
    int range) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServiceByRating")
    public List<Services> findServiceByRating(@WebParam(name = "rating")
    int rating, @WebParam(name = "category")
    Categories category) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findServicesByUser")
    public List<Services> findServicesByUser(@WebParam(name = "client")
    Clients client) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
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

    
}
