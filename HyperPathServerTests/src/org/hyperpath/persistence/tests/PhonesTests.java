package org.hyperpath.persistence.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import junit.framework.TestCase;

import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.jpa.PhonesJpaController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PhonesTests extends TestCase  {
	private EntityManager mockedEM;
	private CriteriaBuilder mockedCB;
	private CriteriaQuery mockedCQ;
	private TypedQuery mockedQuery;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mockedEM    = mock(EntityManager.class);
		mockedCB    = mock(CriteriaBuilder.class);
		mockedCQ    = mock(CriteriaQuery.class);
		mockedQuery = mock(TypedQuery.class);

		when(mockedEM.getCriteriaBuilder()).thenReturn(mockedCB);
		when(mockedCB.createQuery(Long.class)).thenReturn(mockedCQ);
		when(mockedEM.createQuery(mockedCQ)).thenReturn(mockedQuery);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	public void testEdit() {
		fail("Not yet implemented");
	}

	@Test
	public void testDestroy() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPhonesEntities() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPhonesEntitiesIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPhones() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindExactPhone() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindApproximatePhones() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPhonesCount1() {
			when(mockedEM.getCriteriaBuilder()).thenReturn(mockedCB);
			when(mockedQuery.getSingleResult()).thenReturn((long) 10);
			PhonesJpaController controller = new PhonesJpaController(mockedEM);
			try{
				assertEquals(10 , controller.getPhonesCount());
			}catch(Exception e){
				fail("getPhonesCount !!");
			}
	}
	
	@Test
	public void testGetPhonesCount2() {
			when(mockedCQ.from(Phones.class)).thenAnswer(new Answer<Void>() {
		     public Void answer(InvocationOnMock invocation) throws Throwable {
		         throw new Exception("Testing method");
		}});
		try{
			PhonesJpaController controller = new PhonesJpaController(mockedEM);
			controller.getPhonesCount();
			fail("uncaught exception");
		} catch(Exception e){
	
		}
	}

}
