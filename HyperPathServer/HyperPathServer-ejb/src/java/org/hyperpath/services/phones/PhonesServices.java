/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.phones;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "PhonesServices")
@Stateless()
public class PhonesServices {
    @Resource
    private UserTransaction utx;
    @PersistenceUnit
    EntityManagerFactory emf;
    
        /**
     * Web service operation
     */
    @WebMethod(operationName = "addPhones")
    public void addPhones(@WebParam(name = "phone")
    Phones phone) throws Exception, PreexistingEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updatePhones")
    public void updatePhones(@WebParam(name = "phone")
    Phones phone) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deletePhones")
    public void deletePhones(@WebParam(name = "phoneId")
    Integer phoneId) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findPhones")
    public List<Phones> findPhones(@WebParam(name = "phone")
    String phone) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
