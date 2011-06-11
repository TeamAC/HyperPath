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
import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class FaxesJpaController implements Serializable {

    public FaxesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Faxes faxes) throws RollbackFailureException, Exception {
        if (faxes.getEntitiesList() == null) {
            faxes.setEntitiesList(new ArrayList<Entities>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Entities> attachedEntitiesList = new ArrayList<Entities>();
            for (Entities entitiesListEntitiesToAttach : faxes.getEntitiesList()) {
                entitiesListEntitiesToAttach = em.getReference(entitiesListEntitiesToAttach.getClass(), entitiesListEntitiesToAttach.getId());
                attachedEntitiesList.add(entitiesListEntitiesToAttach);
            }
            faxes.setEntitiesList(attachedEntitiesList);
            em.persist(faxes);
            for (Entities entitiesListEntities : faxes.getEntitiesList()) {
                entitiesListEntities.getFaxesList().add(faxes);
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

    public void edit(Faxes faxes) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Faxes persistentFaxes = em.find(Faxes.class, faxes.getId());
            List<Entities> entitiesListOld = persistentFaxes.getEntitiesList();
            List<Entities> entitiesListNew = faxes.getEntitiesList();
            List<Entities> attachedEntitiesListNew = new ArrayList<Entities>();
            for (Entities entitiesListNewEntitiesToAttach : entitiesListNew) {
                entitiesListNewEntitiesToAttach = em.getReference(entitiesListNewEntitiesToAttach.getClass(), entitiesListNewEntitiesToAttach.getId());
                attachedEntitiesListNew.add(entitiesListNewEntitiesToAttach);
            }
            entitiesListNew = attachedEntitiesListNew;
            faxes.setEntitiesList(entitiesListNew);
            faxes = em.merge(faxes);
            for (Entities entitiesListOldEntities : entitiesListOld) {
                if (!entitiesListNew.contains(entitiesListOldEntities)) {
                    entitiesListOldEntities.getFaxesList().remove(faxes);
                    entitiesListOldEntities = em.merge(entitiesListOldEntities);
                }
            }
            for (Entities entitiesListNewEntities : entitiesListNew) {
                if (!entitiesListOld.contains(entitiesListNewEntities)) {
                    entitiesListNewEntities.getFaxesList().add(faxes);
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
                Integer id = faxes.getId();
                if (findFaxes(id) == null) {
                    throw new NonexistentEntityException("The faxes with id " + id + " no longer exists.");
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
            Faxes faxes;
            try {
                faxes = em.getReference(Faxes.class, id);
                faxes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faxes with id " + id + " no longer exists.", enfe);
            }
            List<Entities> entitiesList = faxes.getEntitiesList();
            for (Entities entitiesListEntities : entitiesList) {
                entitiesListEntities.getFaxesList().remove(faxes);
                entitiesListEntities = em.merge(entitiesListEntities);
            }
            em.remove(faxes);
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

    public List<Faxes> findFaxesEntities() {
        return findFaxesEntities(true, -1, -1);
    }

    public List<Faxes> findFaxesEntities(int maxResults, int firstResult) {
        return findFaxesEntities(false, maxResults, firstResult);
    }

    private List<Faxes> findFaxesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Faxes.class));
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

    public Faxes findFaxes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Faxes.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaxesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Faxes> rt = cq.from(Faxes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
