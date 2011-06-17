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
import org.hyperpath.persistence.entities.Services;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class OpeningHoursJpaController implements Serializable {
  private static final long serialVersionUID = -2301993862953287459L;

  public OpeningHoursJpaController(UserTransaction utx,
      EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }

  private UserTransaction      utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(OpeningHours openingHours)
      throws RollbackFailureException, Exception {
    if (openingHours.getServicesList() == null) {
      openingHours.setServicesList(new ArrayList<Services>());
    }
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      List<Services> attachedServicesList = new ArrayList<Services>();
      for (Services servicesListServicesToAttach : openingHours
          .getServicesList()) {
        servicesListServicesToAttach = em.getReference(
            servicesListServicesToAttach.getClass(),
            servicesListServicesToAttach.getServicesPK());
        attachedServicesList.add(servicesListServicesToAttach);
      }
      openingHours.setServicesList(attachedServicesList);
      em.persist(openingHours);
      for (Services servicesListServices : openingHours.getServicesList()) {
        OpeningHours oldOpeningHoursOfServicesListServices = servicesListServices
            .getOpeningHours();
        servicesListServices.setOpeningHours(openingHours);
        servicesListServices = em.merge(servicesListServices);
        if (oldOpeningHoursOfServicesListServices != null) {
          oldOpeningHoursOfServicesListServices.getServicesList()
              .remove(servicesListServices);
          oldOpeningHoursOfServicesListServices = em
              .merge(oldOpeningHoursOfServicesListServices);
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(OpeningHours openingHours) throws IllegalOrphanException,
      NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      OpeningHours persistentOpeningHours = em.find(OpeningHours.class,
          openingHours.getId());
      List<Services> servicesListOld = persistentOpeningHours
          .getServicesList();
      List<Services> servicesListNew = openingHours.getServicesList();
      List<String> illegalOrphanMessages = null;
      for (Services servicesListOldServices : servicesListOld) {
        if (!servicesListNew.contains(servicesListOldServices)) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<String>();
          }
          illegalOrphanMessages.add("You must retain Services "
              + servicesListOldServices
              + " since its openingHours field is not nullable.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      List<Services> attachedServicesListNew = new ArrayList<Services>();
      for (Services servicesListNewServicesToAttach : servicesListNew) {
        servicesListNewServicesToAttach = em.getReference(
            servicesListNewServicesToAttach.getClass(),
            servicesListNewServicesToAttach.getServicesPK());
        attachedServicesListNew.add(servicesListNewServicesToAttach);
      }
      servicesListNew = attachedServicesListNew;
      openingHours.setServicesList(servicesListNew);
      openingHours = em.merge(openingHours);
      for (Services servicesListNewServices : servicesListNew) {
        if (!servicesListOld.contains(servicesListNewServices)) {
          OpeningHours oldOpeningHoursOfServicesListNewServices = servicesListNewServices
              .getOpeningHours();
          servicesListNewServices.setOpeningHours(openingHours);
          servicesListNewServices = em.merge(servicesListNewServices);
          if (oldOpeningHoursOfServicesListNewServices != null
              && !oldOpeningHoursOfServicesListNewServices
                  .equals(openingHours)) {
            oldOpeningHoursOfServicesListNewServices
                .getServicesList().remove(
                    servicesListNewServices);
            oldOpeningHoursOfServicesListNewServices = em
                .merge(oldOpeningHoursOfServicesListNewServices);
          }
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = openingHours.getId();
        if (findOpeningHours(id) == null) {
          throw new NonexistentEntityException(
              "The openingHours with id " + id
                  + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Integer id) throws IllegalOrphanException,
      NonexistentEntityException, RollbackFailureException, Exception {
    EntityManager em = null;
    try {
      utx.begin();
      em = getEntityManager();
      OpeningHours openingHours;
      try {
        openingHours = em.getReference(OpeningHours.class, id);
        openingHours.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException(
            "The openingHours with id " + id + " no longer exists.",
            enfe);
      }
      List<String> illegalOrphanMessages = null;
      List<Services> servicesListOrphanCheck = openingHours
          .getServicesList();
      for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages
            .add("This OpeningHours ("
                + openingHours
                + ") cannot be destroyed since the Services "
                + servicesListOrphanCheckServices
                + " in its servicesList field has a non-nullable openingHours field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      em.remove(openingHours);
      utx.commit();
    } catch (Exception ex) {
      try {
        utx.rollback();
      } catch (Exception re) {
        throw new RollbackFailureException(
            "An error occurred attempting to roll back the transaction.",
            re);
      }
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

  public List<OpeningHours> findOpeningHoursEntities(int maxResults,
                                                     int firstResult) {
    return findOpeningHoursEntities(false, maxResults, firstResult);
  }

  private List<OpeningHours> findOpeningHoursEntities(boolean all,
                                                      int maxResults,
                                                      int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(OpeningHours.class));
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

  public OpeningHours findOpeningHours(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(OpeningHours.class, id);
    } finally {
      em.close();
    }
  }

  public int getOpeningHoursCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<OpeningHours> rt = cq.from(OpeningHours.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
