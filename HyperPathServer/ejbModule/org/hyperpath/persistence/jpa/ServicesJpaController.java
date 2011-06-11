package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Categories;
import org.hyperpath.persistence.entities.Clients;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Reviews;
import org.hyperpath.persistence.entities.Ads;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class ServicesJpaController implements Serializable {

    public ServicesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Services services) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (services.getClientsList() == null) {
            services.setClientsList(new ArrayList<Clients>());
        }
        if (services.getReviewsList() == null) {
            services.setReviewsList(new ArrayList<Reviews>());
        }
        if (services.getAdsList() == null) {
            services.setAdsList(new ArrayList<Ads>());
        }
        
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            OpeningHours openingHours = services.getOpeningHours();
            if (openingHours != null) {
                openingHours = em.getReference(openingHours.getClass(), openingHours.getId());
                services.setOpeningHours(openingHours);
            }
            Entities entities = services.getEntities();
            if (entities != null) {
                entities = em.getReference(entities.getClass(), entities.getId());
                services.setEntities(entities);
            }
            Categories categories = services.getCategories();
            if (categories != null) {
                categories = em.getReference(categories.getClass(), categories.getId());
                services.setCategories(categories);
            }
            List<Clients> attachedClientsList = new ArrayList<Clients>();
            for (Clients clientsListClientsToAttach : services.getClientsList()) {
                clientsListClientsToAttach = em.getReference(clientsListClientsToAttach.getClass(), clientsListClientsToAttach.getClientsPK());
                attachedClientsList.add(clientsListClientsToAttach);
            }
            services.setClientsList(attachedClientsList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : services.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getId());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            services.setReviewsList(attachedReviewsList);
            List<Ads> attachedAdsList = new ArrayList<Ads>();
            for (Ads adsListAdsToAttach : services.getAdsList()) {
                adsListAdsToAttach = em.getReference(adsListAdsToAttach.getClass(), adsListAdsToAttach.getAdsPK());
                attachedAdsList.add(adsListAdsToAttach);
            }
            services.setAdsList(attachedAdsList);
            em.persist(services);
            if (openingHours != null) {
                openingHours.getServicesList().add(services);
                openingHours = em.merge(openingHours);
            }
            if (entities != null) {
                entities.getServicesList().add(services);
                entities = em.merge(entities);
            }
            if (categories != null) {
                categories.getServicesList().add(services);
                categories = em.merge(categories);
            }
            for (Clients clientsListClients : services.getClientsList()) {
                clientsListClients.getServicesList().add(services);
                clientsListClients = em.merge(clientsListClients);
            }
            for (Reviews reviewsListReviews : services.getReviewsList()) {
                reviewsListReviews.getServicesList().add(services);
                reviewsListReviews = em.merge(reviewsListReviews);
            }
            for (Ads adsListAds : services.getAdsList()) {
                Services oldServicesOfAdsListAds = adsListAds.getServices();
                adsListAds.setServices(services);
                adsListAds = em.merge(adsListAds);
                if (oldServicesOfAdsListAds != null) {
                    oldServicesOfAdsListAds.getAdsList().remove(adsListAds);
                    oldServicesOfAdsListAds = em.merge(oldServicesOfAdsListAds);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findServices(services.getServicesPK()) != null) {
                throw new PreexistingEntityException("Services " + services + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Services services) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Services persistentServices = em.find(Services.class, services.getServicesPK());
            OpeningHours openingHoursOld = persistentServices.getOpeningHours();
            OpeningHours openingHoursNew = services.getOpeningHours();
            Entities entitiesOld = persistentServices.getEntities();
            Entities entitiesNew = services.getEntities();
            Categories categoriesOld = persistentServices.getCategories();
            Categories categoriesNew = services.getCategories();
            List<Clients> clientsListOld = persistentServices.getClientsList();
            List<Clients> clientsListNew = services.getClientsList();
            List<Reviews> reviewsListOld = persistentServices.getReviewsList();
            List<Reviews> reviewsListNew = services.getReviewsList();
            List<Ads> adsListOld = persistentServices.getAdsList();
            List<Ads> adsListNew = services.getAdsList();
            List<String> illegalOrphanMessages = null;
            for (Ads adsListOldAds : adsListOld) {
                if (!adsListNew.contains(adsListOldAds)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ads " + adsListOldAds + " since its services field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (openingHoursNew != null) {
                openingHoursNew = em.getReference(openingHoursNew.getClass(), openingHoursNew.getId());
                services.setOpeningHours(openingHoursNew);
            }
            if (entitiesNew != null) {
                entitiesNew = em.getReference(entitiesNew.getClass(), entitiesNew.getId());
                services.setEntities(entitiesNew);
            }
            if (categoriesNew != null) {
                categoriesNew = em.getReference(categoriesNew.getClass(), categoriesNew.getId());
                services.setCategories(categoriesNew);
            }
            List<Clients> attachedClientsListNew = new ArrayList<Clients>();
            for (Clients clientsListNewClientsToAttach : clientsListNew) {
                clientsListNewClientsToAttach = em.getReference(clientsListNewClientsToAttach.getClass(), clientsListNewClientsToAttach.getClientsPK());
                attachedClientsListNew.add(clientsListNewClientsToAttach);
            }
            clientsListNew = attachedClientsListNew;
            services.setClientsList(clientsListNew);
            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getId());
                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
            }
            reviewsListNew = attachedReviewsListNew;
            services.setReviewsList(reviewsListNew);
            List<Ads> attachedAdsListNew = new ArrayList<Ads>();
            for (Ads adsListNewAdsToAttach : adsListNew) {
                adsListNewAdsToAttach = em.getReference(adsListNewAdsToAttach.getClass(), adsListNewAdsToAttach.getAdsPK());
                attachedAdsListNew.add(adsListNewAdsToAttach);
            }
            adsListNew = attachedAdsListNew;
            services.setAdsList(adsListNew);
            services = em.merge(services);
            if (openingHoursOld != null && !openingHoursOld.equals(openingHoursNew)) {
                openingHoursOld.getServicesList().remove(services);
                openingHoursOld = em.merge(openingHoursOld);
            }
            if (openingHoursNew != null && !openingHoursNew.equals(openingHoursOld)) {
                openingHoursNew.getServicesList().add(services);
                openingHoursNew = em.merge(openingHoursNew);
            }
            if (entitiesOld != null && !entitiesOld.equals(entitiesNew)) {
                entitiesOld.getServicesList().remove(services);
                entitiesOld = em.merge(entitiesOld);
            }
            if (entitiesNew != null && !entitiesNew.equals(entitiesOld)) {
                entitiesNew.getServicesList().add(services);
                entitiesNew = em.merge(entitiesNew);
            }
            if (categoriesOld != null && !categoriesOld.equals(categoriesNew)) {
                categoriesOld.getServicesList().remove(services);
                categoriesOld = em.merge(categoriesOld);
            }
            if (categoriesNew != null && !categoriesNew.equals(categoriesOld)) {
                categoriesNew.getServicesList().add(services);
                categoriesNew = em.merge(categoriesNew);
            }
            for (Clients clientsListOldClients : clientsListOld) {
                if (!clientsListNew.contains(clientsListOldClients)) {
                    clientsListOldClients.getServicesList().remove(services);
                    clientsListOldClients = em.merge(clientsListOldClients);
                }
            }
            for (Clients clientsListNewClients : clientsListNew) {
                if (!clientsListOld.contains(clientsListNewClients)) {
                    clientsListNewClients.getServicesList().add(services);
                    clientsListNewClients = em.merge(clientsListNewClients);
                }
            }
            for (Reviews reviewsListOldReviews : reviewsListOld) {
                if (!reviewsListNew.contains(reviewsListOldReviews)) {
                    reviewsListOldReviews.getServicesList().remove(services);
                    reviewsListOldReviews = em.merge(reviewsListOldReviews);
                }
            }
            for (Reviews reviewsListNewReviews : reviewsListNew) {
                if (!reviewsListOld.contains(reviewsListNewReviews)) {
                    reviewsListNewReviews.getServicesList().add(services);
                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
                }
            }
            for (Ads adsListNewAds : adsListNew) {
                if (!adsListOld.contains(adsListNewAds)) {
                    Services oldServicesOfAdsListNewAds = adsListNewAds.getServices();
                    adsListNewAds.setServices(services);
                    adsListNewAds = em.merge(adsListNewAds);
                    if (oldServicesOfAdsListNewAds != null && !oldServicesOfAdsListNewAds.equals(services)) {
                        oldServicesOfAdsListNewAds.getAdsList().remove(adsListNewAds);
                        oldServicesOfAdsListNewAds = em.merge(oldServicesOfAdsListNewAds);
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
                Integer id = services.getServicesPK();
                if (findServices(id) == null) {
                    throw new NonexistentEntityException("The services with id " + id + " no longer exists.");
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
            Services services;
            try {
                services = em.getReference(Services.class, id);
                services.getServicesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The services with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ads> adsListOrphanCheck = services.getAdsList();
            for (Ads adsListOrphanCheckAds : adsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Services (" + services + ") cannot be destroyed since the Ads " + adsListOrphanCheckAds + " in its adsList field has a non-nullable services field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            OpeningHours openingHours = services.getOpeningHours();
            if (openingHours != null) {
                openingHours.getServicesList().remove(services);
                openingHours = em.merge(openingHours);
            }
            Entities entities = services.getEntities();
            if (entities != null) {
                entities.getServicesList().remove(services);
                entities = em.merge(entities);
            }
            Categories categories = services.getCategories();
            if (categories != null) {
                categories.getServicesList().remove(services);
                categories = em.merge(categories);
            }
            List<Clients> clientsList = services.getClientsList();
            for (Clients clientsListClients : clientsList) {
                clientsListClients.getServicesList().remove(services);
                clientsListClients = em.merge(clientsListClients);
            }
            List<Reviews> reviewsList = services.getReviewsList();
            for (Reviews reviewsListReviews : reviewsList) {
                reviewsListReviews.getServicesList().remove(services);
                reviewsListReviews = em.merge(reviewsListReviews);
            }
            em.remove(services);
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

    public List<Services> findServicesEntities() {
        return findServicesEntities(true, -1, -1);
    }

    public List<Services> findServicesEntities(int maxResults, int firstResult) {
        return findServicesEntities(false, maxResults, firstResult);
    }

    private List<Services> findServicesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Services.class));
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

    public Services findServices(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Services.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Services> rt = cq.from(Services.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
