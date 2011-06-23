package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.Entities;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;
import org.hyperpath.persistence.jpa.exceptions.RollbackFailureException;

public class FaxesJpaController implements Serializable {
  private static final long serialVersionUID = 2393904818501360067L;
  private EntityManager em = null;

	public FaxesJpaController(EntityManagerFactory emf) {
      this.em = emf.createEntityManager();
  }

	public FaxesJpaController(EntityManager mockedEM) {
		this.em = mockedEM;
	}
    public void create(Faxes faxes) throws RollbackFailureException, Exception {
        if (faxes.getEntitiesList() == null) {
            faxes.setEntitiesList(new ArrayList<Entities>());
        }
        try {
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
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Faxes faxes) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
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
        } catch (Exception ex) {
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

    @SuppressWarnings("unchecked")
    private List<Faxes> findFaxesEntities(boolean all, int maxResults, int firstResult) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Faxes> criteriaQuery = criteriaBuilder.createQuery(Faxes.class);
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


    @SuppressWarnings("unchecked")
    public List<Faxes> findExactFaxes(String faxNumber) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Faxes> criteriaQuery = criteriaBuilder.createQuery(Faxes.class);
        Root<Faxes> faxRoot = criteriaQuery.from(Faxes.class);
        criteriaQuery.select(faxRoot).where( criteriaBuilder.equal(faxRoot.get("number"),faxNumber));
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
      } finally {
        em.close();
      }
    }

    @SuppressWarnings("unchecked")
    public List<Faxes> findApproximateFaxes(String faxNumber) {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Faxes> criteriaQuery = criteriaBuilder.createQuery(Faxes.class);
        Root<Faxes> faxRoot = criteriaQuery.from(Faxes.class);
        criteriaQuery.select(faxRoot).where(criteriaBuilder.like(faxRoot.<String> get("number"),'%' + faxNumber + '%'));
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
      } finally {
        em.close();
      }
    }

    public Faxes findFaxes(Integer id) {
      try {
        return em.find(Faxes.class, id);
      } finally {
        em.close();
      }
    }

    public int getFaxesCount() {
      try {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Faxes> faxRoot = criteriaQuery.from(Faxes.class);
        criteriaQuery.select(criteriaBuilder.count(faxRoot));
        Query query = em.createQuery(criteriaQuery);
        return ((Long) query.getSingleResult()).intValue();
      } finally {
        em.close();
      }
    }

}
