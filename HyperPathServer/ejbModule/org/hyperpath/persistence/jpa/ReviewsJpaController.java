package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Clients;

import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

public class ReviewsJpaController implements Serializable {
  private static final long serialVersionUID = 4270693784161684160L;

  private EntityManager        em  = null;
  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public ReviewsJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  public ReviewsJpaController(EntityManager mockedEM) {
    em = mockedEM;
  }

  public EntityManager getEntityManager() {
    if (em != null)
      return em;
    return emf.createEntityManager();
  }

    public void create(Reviews reviews) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
            Clients clientsId = reviews.getClientsId();
            if (clientsId != null) {
                clientsId = em.getReference(clientsId.getClass(), clientsId.getId());
                reviews.setClientsId(clientsId);
            }
            Services servicesId = reviews.getServicesId();
            if (servicesId != null) {
                servicesId = em.getReference(servicesId.getClass(), servicesId.getId());
                reviews.setServicesId(servicesId);
            }
            em.persist(reviews);
            if (clientsId != null) {
                clientsId.getReviewsList().add(reviews);
                clientsId = em.merge(clientsId);
            }
            if (servicesId != null) {
                servicesId.getReviewsList().add(reviews);
                servicesId = em.merge(servicesId);
            }
            utx.commit();
        } catch (NotSupportedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SystemException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalStateException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (RollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicMixedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicRollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reviews reviews) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
            Reviews persistentReviews = em.find(Reviews.class, reviews.getId());
            Clients clientsIdOld = persistentReviews.getClientsId();
            Clients clientsIdNew = reviews.getClientsId();
            Services servicesIdOld = persistentReviews.getServicesId();
            Services servicesIdNew = reviews.getServicesId();
            if (clientsIdNew != null) {
                clientsIdNew = em.getReference(clientsIdNew.getClass(), clientsIdNew.getId());
                reviews.setClientsId(clientsIdNew);
            }
            if (servicesIdNew != null) {
                servicesIdNew = em.getReference(servicesIdNew.getClass(), servicesIdNew.getId());
                reviews.setServicesId(servicesIdNew);
            }
            reviews = em.merge(reviews);
            if (clientsIdOld != null && !clientsIdOld.equals(clientsIdNew)) {
                clientsIdOld.getReviewsList().remove(reviews);
                clientsIdOld = em.merge(clientsIdOld);
            }
            if (clientsIdNew != null && !clientsIdNew.equals(clientsIdOld)) {
                clientsIdNew.getReviewsList().add(reviews);
                clientsIdNew = em.merge(clientsIdNew);
            }
            if (servicesIdOld != null && !servicesIdOld.equals(servicesIdNew)) {
                servicesIdOld.getReviewsList().remove(reviews);
                servicesIdOld = em.merge(servicesIdOld);
            }
            if (servicesIdNew != null && !servicesIdNew.equals(servicesIdOld)) {
                servicesIdNew.getReviewsList().add(reviews);
                servicesIdNew = em.merge(servicesIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reviews.getId();
                if (findReviews(id) == null) {
                    throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
            Reviews reviews;
            try {
                reviews = em.getReference(Reviews.class, id);
                reviews.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.", enfe);
            }
            Clients clientsId = reviews.getClientsId();
            if (clientsId != null) {
                clientsId.getReviewsList().remove(reviews);
                clientsId = em.merge(clientsId);
            }
            Services servicesId = reviews.getServicesId();
            if (servicesId != null) {
                servicesId.getReviewsList().remove(reviews);
                servicesId = em.merge(servicesId);
            }
            em.remove(reviews);
            utx.commit();
        } catch (NotSupportedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SystemException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalStateException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (RollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicMixedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (HeuristicRollbackException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reviews> findReviewsEntities() {
        return findReviewsEntities(true, -1, -1);
    }

  public List<Reviews> findReviewsEntities(int maxResults, int firstResult) {
    return findReviewsEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
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

  public Reviews findReviews(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Reviews.class, id);
    } finally {
      em.close();
    }
  }

  public int getReviewsCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
      criteriaQuery.select(criteriaBuilder.count(reviewsRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

  public List<Reviews> findReviewsByService(int serviceID) {
    EntityManager em = getEntityManager();
    try {
//      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
//      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
//      Join<Reviews, Services> shrJoin = reviewsRoot.join()
//      criteriaQuery.where(criteriaBuilder.equal(shrJoin.get("services_id"), serviceID));
//      criteriaQuery.select(reviewsRoot);
//      Query query = em.createQuery(criteriaQuery);
//      return query.getResultList();
return null;
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Reviews> findReviewsByClient(Clients client) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
      Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
      criteriaQuery.select(reviewsRoot);
      Query query = em.createQuery(criteriaQuery);
      return query.getResultList();
    } finally {
      em.close();
    }
  }

}
