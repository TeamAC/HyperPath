package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.OpeningHours;
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class OpeningHoursJpaController implements Serializable {
  private static final long serialVersionUID = -2382587471485336776L;
  private EntityManager em = null;

	public OpeningHoursJpaController(EntityManagerFactory emf) {
      this.em = emf.createEntityManager();
  }

	public OpeningHoursJpaController(EntityManager mockedEM) {
		this.em = mockedEM;
	}

    public void create(OpeningHours openingHours) throws RollbackFailureException, Exception {
        if (openingHours.getServicesList() == null) {
            openingHours.setServicesList(new ArrayList<Services>());
        }
        try {
            List<Services> attachedServicesList = new ArrayList<Services>();
            for (Services servicesListServicesToAttach : openingHours.getServicesList()) {
                servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
                attachedServicesList.add(servicesListServicesToAttach);
            }
            openingHours.setServicesList(attachedServicesList);
            em.persist(openingHours);
            for (Services servicesListServices : openingHours.getServicesList()) {
                OpeningHours oldOpeningHoursidOfServicesListServices = servicesListServices.getOpeningHoursid();
                servicesListServices.setOpeningHoursid(openingHours);
                servicesListServices = em.merge(servicesListServices);
                if (oldOpeningHoursidOfServicesListServices != null) {
                    oldOpeningHoursidOfServicesListServices.getServicesList().remove(servicesListServices);
                    oldOpeningHoursidOfServicesListServices = em.merge(oldOpeningHoursidOfServicesListServices);
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OpeningHours openingHours) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            OpeningHours persistentOpeningHours = em.find(OpeningHours.class, openingHours.getId());
            List<Services> servicesListOld = persistentOpeningHours.getServicesList();
            List<Services> servicesListNew = openingHours.getServicesList();
            List<String> illegalOrphanMessages = null;
            for (Services servicesListOldServices : servicesListOld) {
                if (!servicesListNew.contains(servicesListOldServices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Services " + servicesListOldServices + " since its openingHoursid field is not nullable.");
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
            openingHours.setServicesList(servicesListNew);
            openingHours = em.merge(openingHours);
            for (Services servicesListNewServices : servicesListNew) {
                if (!servicesListOld.contains(servicesListNewServices)) {
                    OpeningHours oldOpeningHoursidOfServicesListNewServices = servicesListNewServices.getOpeningHoursid();
                    servicesListNewServices.setOpeningHoursid(openingHours);
                    servicesListNewServices = em.merge(servicesListNewServices);
                    if (oldOpeningHoursidOfServicesListNewServices != null && !oldOpeningHoursidOfServicesListNewServices.equals(openingHours)) {
                        oldOpeningHoursidOfServicesListNewServices.getServicesList().remove(servicesListNewServices);
                        oldOpeningHoursidOfServicesListNewServices = em.merge(oldOpeningHoursidOfServicesListNewServices);
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            OpeningHours openingHours;
            try {
                openingHours = em.getReference(OpeningHours.class, id);
                openingHours.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The openingHours with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Services> servicesListOrphanCheck = openingHours.getServicesList();
            for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This OpeningHours (" + openingHours + ") cannot be destroyed since the Services " + servicesListOrphanCheckServices + " in its servicesList field has a non-nullable openingHoursid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(openingHours);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OpeningHours> findOpeningHoursEntities() {
        return findOpeningHoursEntities(true, -1, -1);
    }

    public List<OpeningHours> findOpeningHoursEntities(int maxResults, int firstResult) {
        return findOpeningHoursEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<OpeningHours> findOpeningHoursEntities(boolean all,
                                                        int maxResults,
                                                        int firstResult) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<OpeningHours> criteriaQuery = criteriaBuilder.createQuery(OpeningHours.class);
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

    public OpeningHours findOpeningHours(Integer id) {
      try {
        return em.find(OpeningHours.class, id);
      } finally {
        em.close();
      }
    }

    public int getOpeningHoursCount() {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<OpeningHours> openningHoursRoot = criteriaQuery.from(OpeningHours.class);
        criteriaQuery.select(criteriaBuilder.count(openningHoursRoot));
        Query query = em.createQuery(criteriaQuery);
        return ((Long) query.getSingleResult()).intValue();
      } finally {
        em.close();
      }
    }

}
