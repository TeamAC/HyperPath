/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.emails;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author adel
 */
@WebService(serviceName = "EmailsServices")
@Stateless()
public class EmailsServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addEmail")
    public void addEmail(@WebParam(name = "email")
    Emails email) throws Exception, PreexistingEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateEmail")
    public void updateEmail(@WebParam(name = "email")
    Emails email) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "deleteEmail")
    public void deleteEmail(@WebParam(name = "emailId")
    Integer emailId) throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "findEmail")
    public List<Emails> findEmail(@WebParam(name = "email")
    String email) throws Exception, NonexistentEntityException, RollbackFailureException {
        //TODO write your implementation code here:
        return null;
    }
}
