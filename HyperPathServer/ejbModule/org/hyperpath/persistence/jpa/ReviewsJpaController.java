/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author chedi
 */
public class ReviewsJpaController implements Serializable {

    public ReviewsJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reviews reviews) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reviews reviews) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
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

    private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reviews.class));
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
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reviews> rt = cq.from(Reviews.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
