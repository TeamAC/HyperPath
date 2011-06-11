/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.clients;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "ClientsServices")
@Stateless()
public class ClientsServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByAddress")
    public List<Clients> findClientByAddress(@WebParam(name = "address")
    Address address) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientPhone")
    public List<Clients> findClientPhone(@WebParam(name = "phone")
    String phone) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByFax")
    public List<Clients> findClientByFax(@WebParam(name = "fax")
    String fax) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByMail")
    public List<Clients> findClientByMail(@WebParam(name = "mail")
    String mail) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByName")
    public List<Clients> findClientByName(@WebParam(name = "clientName")
    String clientName) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
    
    
       /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByLastName")
    public List<Clients> findClientByLastName(@WebParam(name = "clientLastName")
    String clientLastName) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
    
       /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByLogin")
    public List<Clients> findClientByLogin(@WebParam(name = "clientLogin")
    String clientLogin) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
    
       /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByBirthDate")
    public List<Clients> findClientByBirthDate(@WebParam(name = "clientBirthDate")
    String clientBirthDate) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
    
       /**
     * Web service operation
     */
    @WebMethod(operationName = "findClientByAge")
    public List<Clients> findClientByAge(@WebParam(name = "clientAge")
    String clientAge) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
