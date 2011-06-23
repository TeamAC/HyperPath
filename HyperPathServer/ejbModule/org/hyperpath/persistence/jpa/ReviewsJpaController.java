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
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class ReviewsJpaController implements Serializable {
  private static final long serialVersionUID = 913466467907993591L;
	private EntityManager em = null;

	public ReviewsJpaController(EntityManagerFactory emf) {
      this.em = emf.createEntityManager();
  }

	public ReviewsJpaController(EntityManager mockedEM) {
		this.em = mockedEM;
	}
    public void create(Reviews reviews) throws RollbackFailureException, Exception {
        try {
            Services servicesId = reviews.getServicesId();
            if (servicesId != null) {
                servicesId = em.getReference(servicesId.getClass(), servicesId.getId());
                reviews.setServicesId(servicesId);
            }
            Clients clientsId = reviews.getClientsId();
            if (clientsId != null) {
                clientsId = em.getReference(clientsId.getClass(), clientsId.getId());
                reviews.setClientsId(clientsId);
            }
            em.persist(reviews);
            if (servicesId != null) {
                servicesId.getReviewsList().add(reviews);
                servicesId = em.merge(servicesId);
            }
            if (clientsId != null) {
                clientsId.getReviewsList().add(reviews);
                clientsId = em.merge(clientsId);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reviews reviews) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            Reviews persistentReviews = em.find(Reviews.class, reviews.getId());
            Services servicesIdOld = persistentReviews.getServicesId();
            Services servicesIdNew = reviews.getServicesId();
            Clients clientsIdOld = persistentReviews.getClientsId();
            Clients clientsIdNew = reviews.getClientsId();
            if (servicesIdNew != null) {
                servicesIdNew = em.getReference(servicesIdNew.getClass(), servicesIdNew.getId());
                reviews.setServicesId(servicesIdNew);
            }
            if (clientsIdNew != null) {
                clientsIdNew = em.getReference(clientsIdNew.getClass(), clientsIdNew.getId());
                reviews.setClientsId(clientsIdNew);
            }
            reviews = em.merge(reviews);
            if (servicesIdOld != null && !servicesIdOld.equals(servicesIdNew)) {
                servicesIdOld.getReviewsList().remove(reviews);
                servicesIdOld = em.merge(servicesIdOld);
            }
            if (servicesIdNew != null && !servicesIdNew.equals(servicesIdOld)) {
                servicesIdNew.getReviewsList().add(reviews);
                servicesIdNew = em.merge(servicesIdNew);
            }
            if (clientsIdOld != null && !clientsIdOld.equals(clientsIdNew)) {
                clientsIdOld.getReviewsList().remove(reviews);
                clientsIdOld = em.merge(clientsIdOld);
            }
            if (clientsIdNew != null && !clientsIdNew.equals(clientsIdOld)) {
                clientsIdNew.getReviewsList().add(reviews);
                clientsIdNew = em.merge(clientsIdNew);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            Reviews reviews;
            try {
                reviews = em.getReference(Reviews.class, id);
                reviews.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.", enfe);
            }
            Services servicesId = reviews.getServicesId();
            if (servicesId != null) {
                servicesId.getReviewsList().remove(reviews);
                servicesId = em.merge(servicesId);
            }
            Clients clientsId = reviews.getClientsId();
            if (clientsId != null) {
                clientsId.getReviewsList().remove(reviews);
                clientsId = em.merge(clientsId);
            }
            em.remove(reviews);
        } catch (Exception ex) {
            throw ex;
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
      try {
        return em.find(Reviews.class, id);
      } finally {
        em.close();
      }
    }

    public int getReviewsCount() {
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
      try {
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<Reviews> criteriaQuery = criteriaBuilder.createQuery(Reviews.class);
//        Root<Reviews> reviewsRoot = criteriaQuery.from(Reviews.class);
//        Join<Reviews, Services> shrJoin = reviewsRoot.join()
//        criteriaQuery.where(criteriaBuilder.equal(shrJoin.get("services_id"), serviceID));
//        criteriaQuery.select(reviewsRoot);
//        Query query = em.createQuery(criteriaQuery);
//        return query.getResultList();
  return null;
      } finally {
        em.close();
      }
    }

    @SuppressWarnings("unchecked")
    public List<Reviews> findReviewsByClient(Clients client) {
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
