package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.hyperpath.persistence.entities.Emails;
import org.hyperpath.persistence.entities.Entities;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class EmailsJpaController implements Serializable {
  private static final long    serialVersionUID = 3395709032680641939L;

  /*
   * This is used only in test mode for mocking the entity manager
   */
  private EntityManager        em               = null;
  private UserTransaction      utx              = null;
  private EntityManagerFactory emf              = null;

  public EmailsJpaController(EntityManager mockedEM) {
    em = mockedEM;
  }

  public EmailsJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  public EntityManager getEntityManager() {
    if (em != null)
      return em;
    return emf.createEntityManager();
  }

  public void create(Emails emails) throws RollbackFailureException,
      Exception {
    if (emails.getEntitiesList() == null) {
      emails.setEntitiesList(new ArrayList<Entities>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      List<Entities> attachedEntitiesList = new ArrayList<Entities>();
      for (Entities entitiesListEntitiesToAttach : emails
          .getEntitiesList()) {
        entitiesListEntitiesToAttach = em.getReference(
            entitiesListEntitiesToAttach.getClass(),
            entitiesListEntitiesToAttach.getId());
        attachedEntitiesList.add(entitiesListEntitiesToAttach);
      }
      emails.setEntitiesList(attachedEntitiesList);
      em.persist(emails);
      for (Entities entitiesListEntities : emails.getEntitiesList()) {
        entitiesListEntities.getEmailsList().add(emails);
        entitiesListEntities = em.merge(entitiesListEntities);
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Emails emails) throws NonexistentEntityException,
      RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Emails persistentEmails = em.find(Emails.class, emails.getId());
      List<Entities> entitiesListOld = persistentEmails.getEntitiesList();
      List<Entities> entitiesListNew = emails.getEntitiesList();
      List<Entities> attachedEntitiesListNew = new ArrayList<Entities>();
      for (Entities entitiesListNewEntitiesToAttach : entitiesListNew) {
        entitiesListNewEntitiesToAttach = em.getReference(
            entitiesListNewEntitiesToAttach.getClass(),
            entitiesListNewEntitiesToAttach.getId());
        attachedEntitiesListNew.add(entitiesListNewEntitiesToAttach);
      }
      entitiesListNew = attachedEntitiesListNew;
      emails.setEntitiesList(entitiesListNew);
      emails = em.merge(emails);
      for (Entities entitiesListOldEntities : entitiesListOld) {
        if (!entitiesListNew.contains(entitiesListOldEntities)) {
          entitiesListOldEntities.getEmailsList().remove(emails);
          entitiesListOldEntities = em.merge(entitiesListOldEntities);
        }
      }
      for (Entities entitiesListNewEntities : entitiesListNew) {
        if (!entitiesListOld.contains(entitiesListNewEntities)) {
          entitiesListNewEntities.getEmailsList().add(emails);
          entitiesListNewEntities = em.merge(entitiesListNewEntities);
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = emails.getId();
        if (findEmails(id) == null) {
          throw new NonexistentEntityException("The emails with id "
              + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws NonexistentEntityException,
      RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      Emails emails;
      try {
        emails = em.getReference(Emails.class, id);
        emails.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The emails with id " + id
            + " no longer exists.", enfe);
      }
      List<Entities> entitiesList = emails.getEntitiesList();
      for (Entities entitiesListEntities : entitiesList) {
        entitiesListEntities.getEmailsList().remove(emails);
        entitiesListEntities = em.merge(entitiesListEntities);
      }
      em.remove(emails);
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<Emails> findEmailsEntities() {
    return findEmailsEntities(true, -1, -1);
  }

  public List<Emails> findEmailsEntities(int maxResults, int firstResult) {
    return findEmailsEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Emails> findEmailsEntities(boolean all, int maxResults,
                                          int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Emails> criteriaQuery = criteriaBuilder.createQuery(Emails.class);
      Query query = em.createQuery(criteriaQuery);
      if (!all) {
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);
      }
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Emails> findExactEmails(String emailAddress) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Emails> criteriaQuery = criteriaBuilder.createQuery(Emails.class);
      Root<Emails> emailRoot = criteriaQuery.from(Emails.class);
      criteriaQuery.select(emailRoot).where( criteriaBuilder.equal(emailRoot.get("address"),emailAddress));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Emails> findApproximateEmails(String emailAddress) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Emails> criteriaQuery = criteriaBuilder.createQuery(Emails.class);
      Root<Emails> emailRoot = criteriaQuery.from(Emails.class);
      criteriaQuery.select(emailRoot).where(criteriaBuilder.like(emailRoot.<String> get("address"),'%' + emailAddress + '%'));
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

  public Emails findEmails(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Emails.class, id);
    } finally {
      em.close();
    }
  }

  public int getEmailsCount() throws Exception {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Emails> emailRoot = criteriaQuery.from(Emails.class);
      criteriaQuery.select(criteriaBuilder.count(emailRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
