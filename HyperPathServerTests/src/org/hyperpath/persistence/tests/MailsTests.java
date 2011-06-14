package org.hyperpath.persistence.tests;

import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import junit.framework.TestCase;

import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.jpa.EmailsJpaController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MailsTests extends TestCase {
  private EntityManager mockedEM;
  private CriteriaBuilder mockedCB;
  private CriteriaQuery<Long> mockedCQ;
  private TypedQuery<Long> mockedQuery;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @SuppressWarnings({ "unchecked"})
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
  public void testFindEmailsEntities() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindEmailsEntitiesIntInt() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindExactEmails() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindApproximateEmails() {
    fail("Not yet implemented");
  }

  @Test
  public void testFindEmails() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetEmailsCount1() {
    when(mockedCQ.from(Emails.class)).thenReturn(null);
    when(mockedCB.count(null)).thenReturn(null);
    when(mockedCQ.select(null)).thenReturn(null);
    when(mockedQuery.getSingleResult()).thenReturn((long) 10);
    try{
      EmailsJpaController controller = new EmailsJpaController(mockedEM);
      assertEquals(10, controller.getEmailsCount());
    } catch(Exception e){
      fail("caught exception");
    }
  }

  @Test
  public void testGetEmailsCount2() {
    when(mockedCQ.from(Emails.class)).thenAnswer(new Answer<Void>() {
         public Void answer(InvocationOnMock invocation) throws Throwable {
             throw new Exception("Testing methode");
    }});
    try{
      EmailsJpaController controller = new EmailsJpaController(mockedEM);
      controller.getEmailsCount();
      fail("uncaught exception");
    } catch(Exception e){

    }
  }

}
