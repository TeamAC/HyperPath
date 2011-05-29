package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.hyperpath.persistence.entities.Entities;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class PhonesJpaController implements Serializable {

    public PhonesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Phones phones) throws RollbackFailureException, Exception {
        if (phones.getEntitiesList() == null) {
            phones.setEntitiesList(new ArrayList<Entities>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Entities> attachedEntitiesList = new ArrayList<Entities>();
            for (Entities entitiesListEntitiesToAttach : phones.getEntitiesList()) {
                entitiesListEntitiesToAttach = em.getReference(entitiesListEntitiesToAttach.getClass(), entitiesListEntitiesToAttach.getId());
                attachedEntitiesList.add(entitiesListEntitiesToAttach);
            }
            phones.setEntitiesList(attachedEntitiesList);
            em.persist(phones);
            for (Entities entitiesListEntities : phones.getEntitiesList()) {
                entitiesListEntities.getPhonesList().add(phones);
                entitiesListEntities = em.merge(entitiesListEntities);
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

    public void edit(Phones phones) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Phones persistentPhones = em.find(Phones.class, phones.getId());
            List<Entities> entitiesListOld = persistentPhones.getEntitiesList();
            List<Entities> entitiesListNew = phones.getEntitiesList();
            List<Entities> attachedEntitiesListNew = new ArrayList<Entities>();
            for (Entities entitiesListNewEntitiesToAttach : entitiesListNew) {
                entitiesListNewEntitiesToAttach = em.getReference(entitiesListNewEntitiesToAttach.getClass(), entitiesListNewEntitiesToAttach.getId());
                attachedEntitiesListNew.add(entitiesListNewEntitiesToAttach);
            }
            entitiesListNew = attachedEntitiesListNew;
            phones.setEntitiesList(entitiesListNew);
            phones = em.merge(phones);
            for (Entities entitiesListOldEntities : entitiesListOld) {
                if (!entitiesListNew.contains(entitiesListOldEntities)) {
                    entitiesListOldEntities.getPhonesList().remove(phones);
                    entitiesListOldEntities = em.merge(entitiesListOldEntities);
                }
            }
            for (Entities entitiesListNewEntities : entitiesListNew) {
                if (!entitiesListOld.contains(entitiesListNewEntities)) {
                    entitiesListNewEntities.getPhonesList().add(phones);
                    entitiesListNewEntities = em.merge(entitiesListNewEntities);
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
                Integer id = phones.getId();
                if (findPhones(id) == null) {
                    throw new NonexistentEntityException("The phones with id " + id + " no longer exists.");
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
            Phones phones;
            try {
                phones = em.getReference(Phones.class, id);
                phones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phones with id " + id + " no longer exists.", enfe);
            }
            List<Entities> entitiesList = phones.getEntitiesList();
            for (Entities entitiesListEntities : entitiesList) {
                entitiesListEntities.getPhonesList().remove(phones);
                entitiesListEntities = em.merge(entitiesListEntities);
            }
            em.remove(phones);
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

    public List<Phones> findPhonesEntities() {
        return findPhonesEntities(true, -1, -1);
    }

    public List<Phones> findPhonesEntities(int maxResults, int firstResult) {
        return findPhonesEntities(false, maxResults, firstResult);
    }

    private List<Phones> findPhonesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Phones.class));
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

    public Phones findPhones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Phones.class, id);
        } finally {
            em.close();
        }
    }

    public int getPhonesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Phones> rt = cq.from(Phones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
