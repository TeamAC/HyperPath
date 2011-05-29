/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.faxes;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "FaxesServices")
@Stateless()
public class FaxesServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "addFaxes")
    public void addFaxes(@WebParam(name = "faxe")
    Faxes faxe) throws Exception, PreexistingEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateFaxes")
    public void updateFaxes(@WebParam(name = "faxe")
    Faxes faxe) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteFaxes")
    public void deleteFaxes(@WebParam(name = "faxeId")
    Integer faxeId) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findFaxes")
    public List<Faxes> findFaxes(@WebParam(name = "faxe")
    String faxe) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
