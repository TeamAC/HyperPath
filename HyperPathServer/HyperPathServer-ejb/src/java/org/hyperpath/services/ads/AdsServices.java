/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.ads;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author adel
 */
@WebService(serviceName = "AdsServices")
@Stateless()
public class AdsServices {

    /** This is a sample web service operation */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
}
