/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.services.tests;

import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.services.addresses.AddressesServices;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author adel
 */
public class AddressesServicesTest {
    
    public AddressesServicesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addAddress method, of class AddressesServices.
     */
    @Test
    public void testAddAddress() throws Exception {
        System.out.println("addAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        try{
            instance.addAddress(address);
            fail("Inserting null address ???");
        }catch(Exception e) {}
        
        try{
            address = new Address(-1, "street", "zip", "city", "department", "country");
            try{instance.addAddress(address);}
            catch(PreexistingEntityException e){}
            catch(RollbackFailureException   e){}
            catch(Exception e){fail("Failed to trigger PreexistingEntityException exception");}
        }catch(Exception e){
            fail("Failed to insert valid address");
        }
        instance.addAddress(address);
        container.close();
    }

    /**
     * Test of updateAddress method, of class AddressesServices.
     */
    @Test
    public void testUpdateAddress() throws Exception {
        System.out.println("updateAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        instance.updateAddress(address);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAddress method, of class AddressesServices.
     */
    @Test
    public void testDeleteAddress() throws Exception {
        System.out.println("deleteAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        instance.deleteAddress(address);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAddress method, of class AddressesServices.
     */
    @Test
    public void testFindAddress() throws Exception {
        System.out.println("findAddress");
        Categories category = null;
        String address = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        Address expResult = null;
        Address result = instance.findAddress(category, address);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAddressByLocation method, of class AddressesServices.
     */
    @Test
    public void testFindAddressByLocation() throws Exception {
        System.out.println("findAddressByLocation");
        Categories category = null;
        String location = "";
        int range = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        Address expResult = null;
        Address result = instance.findAddressByLocation(category, location, range);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findServiceByAddress method, of class AddressesServices.
     */
    @Test
    public void testFindServiceByAddress() throws Exception {
        System.out.println("findServiceByAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        List expResult = null;
        List result = instance.findServiceByAddress(address);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAdvertizerByAddress method, of class AddressesServices.
     */
    @Test
    public void testFindAdvertizerByAddress() throws Exception {
        System.out.println("findAdvertizerByAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        List expResult = null;
        List result = instance.findAdvertizerByAddress(address);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findClientByAddress method, of class AddressesServices.
     */
    @Test
    public void testFindClientByAddress() throws Exception {
        System.out.println("findClientByAddress");
        Address address = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AddressesServices instance = (AddressesServices)container.getContext().lookup("java:global/classes/AddressesServices");
        List expResult = null;
        List result = instance.findClientByAddress(address);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
