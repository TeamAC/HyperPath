/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.advertisers;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Advertisers;
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
    
}
