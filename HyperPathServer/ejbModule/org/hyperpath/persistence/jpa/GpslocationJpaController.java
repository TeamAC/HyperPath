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
import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

/**
 *
 * @author chedi
 */
public class GpslocationJpaController implements Serializable {

    public GpslocationJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gpslocation gpslocation) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (gpslocation.getServicesList() == null) {
            gpslocation.setServicesList(new ArrayList<Services>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Services> attachedServicesList = new ArrayList<Services>();
            for (Services servicesListServicesToAttach : gpslocation.getServicesList()) {
                servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
                attachedServicesList.add(servicesListServicesToAttach);
            }
            gpslocation.setServicesList(attachedServicesList);
            em.persist(gpslocation);
            for (Services servicesListServices : gpslocation.getServicesList()) {
                Gpslocation oldGpslocationIdOfServicesListServices = servicesListServices.getGpslocationId();
                servicesListServices.setGpslocationId(gpslocation);
                servicesListServices = em.merge(servicesListServices);
                if (oldGpslocationIdOfServicesListServices != null) {
                    oldGpslocationIdOfServicesListServices.getServicesList().remove(servicesListServices);
                    oldGpslocationIdOfServicesListServices = em.merge(oldGpslocationIdOfServicesListServices);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGpslocation(gpslocation.getId()) != null) {
                throw new PreexistingEntityException("Gpslocation " + gpslocation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gpslocation gpslocation) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Gpslocation persistentGpslocation = em.find(Gpslocation.class, gpslocation.getId());
            List<Services> servicesListOld = persistentGpslocation.getServicesList();
            List<Services> servicesListNew = gpslocation.getServicesList();
            List<String> illegalOrphanMessages = null;
            for (Services servicesListOldServices : servicesListOld) {
                if (!servicesListNew.contains(servicesListOldServices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Services " + servicesListOldServices + " since its gpslocationId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Services> attachedServicesListNew = new ArrayList<Services>();
            for (Services servicesListNewServicesToAttach : servicesListNew) {
                servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
                attachedServicesListNew.add(servicesListNewServicesToAttach);
            }
            servicesListNew = attachedServicesListNew;
            gpslocation.setServicesList(servicesListNew);
            gpslocation = em.merge(gpslocation);
            for (Services servicesListNewServices : servicesListNew) {
                if (!servicesListOld.contains(servicesListNewServices)) {
                    Gpslocation oldGpslocationIdOfServicesListNewServices = servicesListNewServices.getGpslocationId();
                    servicesListNewServices.setGpslocationId(gpslocation);
                    servicesListNewServices = em.merge(servicesListNewServices);
                    if (oldGpslocationIdOfServicesListNewServices != null && !oldGpslocationIdOfServicesListNewServices.equals(gpslocation)) {
                        oldGpslocationIdOfServicesListNewServices.getServicesList().remove(servicesListNewServices);
                        oldGpslocationIdOfServicesListNewServices = em.merge(oldGpslocationIdOfServicesListNewServices);
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
                Integer id = gpslocation.getId();
                if (findGpslocation(id) == null) {
                    throw new NonexistentEntityException("The gpslocation with id " + id + " no longer exists.");
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
            Gpslocation gpslocation;
            try {
                gpslocation = em.getReference(Gpslocation.class, id);
                gpslocation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gpslocation with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Services> servicesListOrphanCheck = gpslocation.getServicesList();
            for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Gpslocation (" + gpslocation + ") cannot be destroyed since the Services " + servicesListOrphanCheckServices + " in its servicesList field has a non-nullable gpslocationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gpslocation);
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

    public List<Gpslocation> findGpslocationEntities() {
        return findGpslocationEntities(true, -1, -1);
    }

    public List<Gpslocation> findGpslocationEntities(int maxResults, int firstResult) {
        return findGpslocationEntities(false, maxResults, firstResult);
    }

    private List<Gpslocation> findGpslocationEntities(boolean all, int maxResults, int firstResult) {
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
