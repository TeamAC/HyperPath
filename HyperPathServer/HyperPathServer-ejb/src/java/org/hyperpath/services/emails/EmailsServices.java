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

@WebService(serviceName = "EmailsServices")
@Stateless()
public class EmailsServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;

    /**
     * Add new email address
     */
    @WebMethod(operationName = "addEmail")
    public void addEmail(@WebParam(name = "email")Emails email) 
    throws Exception, PreexistingEntityException, RollbackFailureException {
    }

    /**
     * Update email address
     */
    @WebMethod(operationName = "updateEmail")
    public void updateEmail(@WebParam(name = "email")Emails email) 
    throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Delete emacs address
     */
    @WebMethod(operationName = "deleteEmail")
    public void deleteEmail(@WebParam(name = "emailId")Integer emailId)
    throws Exception, NonexistentEntityException, RollbackFailureException {
    }

    /**
     * Find email address by aproximation
     */
    @WebMethod(operationName = "findEmail")
    public List<Emails> findEmail(@WebParam(name = "email")
    String email) throws Exception, NonexistentEntityException, RollbackFailureException {
        return null;
    }
}
