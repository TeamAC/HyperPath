/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.categories;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;
import org.jboss.weld.logging.Category;

/**
 *
 * @author adel
 */
@WebService(serviceName = "CategoriesServices")
@Stateless()
public class CategoriesServices {
    @Resource
    private UserTransaction utx;

    @PersistenceUnit
    EntityManagerFactory emf;
    
    @WebMethod(operationName = "addCategory")
    public void addService(@WebParam(name = "category")
    Category service) throws Exception, PreexistingEntityException, RollbackFailureException {
    }
}
