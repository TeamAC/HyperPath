package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class GpslocationJpaController implements Serializable {
  private static final long serialVersionUID = -8056592577113286641L;

  public GpslocationJpaController(UserTransaction utx,
      EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(Gpslocation gpslocation)
      throws PreexistingEntityException, RollbackFailureException,
      Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      em.persist(gpslocation);
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      if (findGpslocation(gpslocation.getId()) != null) {
        throw new PreexistingEntityException("Gpslocation "
            + gpslocation + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(Gpslocation gpslocation)
      throws NonexistentEntityException, RollbackFailureException,
      Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      gpslocation = em.merge(gpslocation);
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
        Integer id = gpslocation.getId();
        if (findGpslocation(id) == null) {
          throw new NonexistentEntityException(
              "The gpslocation with id " + id
                  + " no longer exists.");
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
      Gpslocation gpslocation;
      try {
        gpslocation = em.getReference(Gpslocation.class, id);
        gpslocation.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The gpslocation with id "
            + id + " no longer exists.", enfe);
      }
      em.remove(gpslocation);
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

  public List<Gpslocation> findGpslocationEntities() {
    return findGpslocationEntities(true, -1, -1);
  }

  public List<Gpslocation> findGpslocationEntities(int maxResults,
                                                   int firstResult) {
    return findGpslocationEntities(false, maxResults, firstResult);
  }

  private List<Gpslocation> findGpslocationEntities(boolean all,
                                                    int maxResults,
                                                    int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(Gpslocation.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public Gpslocation findGpslocation(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Gpslocation.class, id);
    } finally {
      em.close();
    }
  }

  public int getGpslocationCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<Gpslocation> rt = cq.from(Gpslocation.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
