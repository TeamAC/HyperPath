/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author chedi
 */
public class ClientsJpaController implements Serializable {

    public ClientsJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clients clients) throws RollbackFailureException, Exception {
        if (clients.getServicesList() == null) {
            clients.setServicesList(new ArrayList<Services>());
        }
        if (clients.getReviewsList() == null) {
            clients.setReviewsList(new ArrayList<Reviews>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Entities entitiesId = clients.getEntitiesId();
            if (entitiesId != null) {
                entitiesId = em.getReference(entitiesId.getClass(), entitiesId.getId());
                clients.setEntitiesId(entitiesId);
            }
            List<Services> attachedServicesList = new ArrayList<Services>();
            for (Services servicesListServicesToAttach : clients.getServicesList()) {
                servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
                attachedServicesList.add(servicesListServicesToAttach);
            }
            clients.setServicesList(attachedServicesList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : clients.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getId());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            clients.setReviewsList(attachedReviewsList);
            em.persist(clients);
            if (entitiesId != null) {
                entitiesId.getClientsList().add(clients);
                entitiesId = em.merge(entitiesId);
            }
            for (Services servicesListServices : clients.getServicesList()) {
                servicesListServices.getClientsList().add(clients);
                servicesListServices = em.merge(servicesListServices);
            }
            for (Reviews reviewsListReviews : clients.getReviewsList()) {
                Clients oldClientsIdOfReviewsListReviews = reviewsListReviews.getClientsId();
                reviewsListReviews.setClientsId(clients);
                reviewsListReviews = em.merge(reviewsListReviews);
                if (oldClientsIdOfReviewsListReviews != null) {
                    oldClientsIdOfReviewsListReviews.getReviewsList().remove(reviewsListReviews);
                    oldClientsIdOfReviewsListReviews = em.merge(oldClientsIdOfReviewsListReviews);
                }
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

    public void edit(Clients clients) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clients persistentClients = em.find(Clients.class, clients.getId());
            Entities entitiesIdOld = persistentClients.getEntitiesId();
            Entities entitiesIdNew = clients.getEntitiesId();
            List<Services> servicesListOld = persistentClients.getServicesList();
            List<Services> servicesListNew = clients.getServicesList();
            List<Reviews> reviewsListOld = persistentClients.getReviewsList();
            List<Reviews> reviewsListNew = clients.getReviewsList();
            List<String> illegalOrphanMessages = null;
            for (Reviews reviewsListOldReviews : reviewsListOld) {
                if (!reviewsListNew.contains(reviewsListOldReviews)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its clientsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entitiesIdNew != null) {
                entitiesIdNew = em.getReference(entitiesIdNew.getClass(), entitiesIdNew.getId());
                clients.setEntitiesId(entitiesIdNew);
            }
            List<Services> attachedServicesListNew = new ArrayList<Services>();
            for (Services servicesListNewServicesToAttach : servicesListNew) {
                servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
                attachedServicesListNew.add(servicesListNewServicesToAttach);
            }
            servicesListNew = attachedServicesListNew;
            clients.setServicesList(servicesListNew);
            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getId());
                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
            }
            reviewsListNew = attachedReviewsListNew;
            clients.setReviewsList(reviewsListNew);
            clients = em.merge(clients);
            if (entitiesIdOld != null && !entitiesIdOld.equals(entitiesIdNew)) {
                entitiesIdOld.getClientsList().remove(clients);
                entitiesIdOld = em.merge(entitiesIdOld);
            }
            if (entitiesIdNew != null && !entitiesIdNew.equals(entitiesIdOld)) {
                entitiesIdNew.getClientsList().add(clients);
                entitiesIdNew = em.merge(entitiesIdNew);
            }
            for (Services servicesListOldServices : servicesListOld) {
                if (!servicesListNew.contains(servicesListOldServices)) {
                    servicesListOldServices.getClientsList().remove(clients);
                    servicesListOldServices = em.merge(servicesListOldServices);
                }
            }
            for (Services servicesListNewServices : servicesListNew) {
                if (!servicesListOld.contains(servicesListNewServices)) {
                    servicesListNewServices.getClientsList().add(clients);
                    servicesListNewServices = em.merge(servicesListNewServices);
                }
            }
            for (Reviews reviewsListNewReviews : reviewsListNew) {
                if (!reviewsListOld.contains(reviewsListNewReviews)) {
                    Clients oldClientsIdOfReviewsListNewReviews = reviewsListNewReviews.getClientsId();
                    reviewsListNewReviews.setClientsId(clients);
                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
                    if (oldClientsIdOfReviewsListNewReviews != null && !oldClientsIdOfReviewsListNewReviews.equals(clients)) {
                        oldClientsIdOfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
                        oldClientsIdOfReviewsListNewReviews = em.merge(oldClientsIdOfReviewsListNewReviews);
                    }
                }
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
                Integer id = clients.getId();
                if (findClients(id) == null) {
                    throw new NonexistentEntityException("The clients with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clients clients;
            try {
                clients = em.getReference(Clients.class, id);
                clients.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clients with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reviews> reviewsListOrphanCheck = clients.getReviewsList();
            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clients (" + clients + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable clientsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entities entitiesId = clients.getEntitiesId();
            if (entitiesId != null) {
                entitiesId.getClientsList().remove(clients);
                entitiesId = em.merge(entitiesId);
            }
            List<Services> servicesList = clients.getServicesList();
            for (Services servicesListServices : servicesList) {
                servicesListServices.getClientsList().remove(clients);
                servicesListServices = em.merge(servicesListServices);
            }
            em.remove(clients);
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

    public List<Clients> findClientsEntities() {
        return findClientsEntities(true, -1, -1);
    }

    public List<Clients> findClientsEntities(int maxResults, int firstResult) {
        return findClientsEntities(false, maxResults, firstResult);
    }

    private List<Clients> findClientsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clients.class));
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

    public Clients findClients(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clients.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clients> rt = cq.from(Clients.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
