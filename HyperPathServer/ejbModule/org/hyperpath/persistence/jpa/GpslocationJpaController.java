package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hyperpath.persistence.entities.Gpslocation;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.PreexistingEntityException;

public class GpslocationJpaController implements Serializable {
  private static final long serialVersionUID = -8056592577113286641L;

  public GpslocationJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  public GpslocationJpaController(EntityManager mockedEM) {
    em = mockedEM;
  }

  private EntityManager        em  = null;
  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    if(em != null){
      return em;
    }
    return emf.createEntityManager();
  }

   public void create(Gpslocation gpslocation) throws PreexistingEntityException, Exception {
        if (gpslocation.getServicesList() == null) {
            gpslocation.setServicesList(new ArrayList<Services>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
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

    public void edit(Gpslocation gpslocation) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx.begin();
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

  public List<Gpslocation> findGpslocationEntities() {
    return findGpslocationEntities(true, -1, -1);
  }

  public List<Gpslocation> findGpslocationEntities(int maxResults,
                                                   int firstResult) {
    return findGpslocationEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Gpslocation> findGpslocationEntities(boolean all,
                                                    int maxResults,
                                                    int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Gpslocation> criteriaQuery = criteriaBuilder.createQuery(Gpslocation.class);
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
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Gpslocation> gpslocationRoot = criteriaQuery.from(Gpslocation.class);
      criteriaQuery.select(criteriaBuilder.count(gpslocationRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
