package org.hyperpath.persistence.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hyperpath.persistence.entities.Emails;
import java.util.ArrayList;
import java.util.List;
import org.hyperpath.persistence.entities.Entities;
import org.hyperpath.persistence.entities.Faxes;
import org.hyperpath.persistence.entities.Phones;
import org.hyperpath.persistence.entities.Address;
import org.hyperpath.persistence.entities.Services;
import org.hyperpath.persistence.entities.Advertisers;
import org.hyperpath.persistence.entities.Clients;
import org.hyperpath.persistence.jpa.exceptions.IllegalOrphanException;
import org.hyperpath.persistence.jpa.exceptions.NonexistentEntityException;

public class EntitiesJpaController implements Serializable {
  private static final long serialVersionUID = 4420567803215333749L;

  public EntitiesJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }

  private EntityManager        em  = null;
  private EntityManagerFactory emf = null;

  public EntitiesJpaController(EntityManager mockedEM) {
    em = mockedEM;
  }

  public EntityManager getEntityManager() {
    if(em != null){
      return em;
    }
    return emf.createEntityManager();
  }

  public void create(Entities entities) throws Exception {
    if (entities.getEmailsList() == null) {
        entities.setEmailsList(new ArrayList<Emails>());
    }
    if (entities.getFaxesList() == null) {
        entities.setFaxesList(new ArrayList<Faxes>());
    }
    if (entities.getPhonesList() == null) {
        entities.setPhonesList(new ArrayList<Phones>());
    }
    if (entities.getAddressList() == null) {
        entities.setAddressList(new ArrayList<Address>());
    }
    if (entities.getServicesList() == null) {
        entities.setServicesList(new ArrayList<Services>());
    }
    if (entities.getAdvertisersList() == null) {
        entities.setAdvertisersList(new ArrayList<Advertisers>());
    }
    if (entities.getClientsList() == null) {
        entities.setClientsList(new ArrayList<Clients>());
    }
    EntityManager em = null;
    try {
        em = getEntityManager();
        List<Emails> attachedEmailsList = new ArrayList<Emails>();
        for (Emails emailsListEmailsToAttach : entities.getEmailsList()) {
            emailsListEmailsToAttach = em.getReference(emailsListEmailsToAttach.getClass(), emailsListEmailsToAttach.getId());
            attachedEmailsList.add(emailsListEmailsToAttach);
        }
        entities.setEmailsList(attachedEmailsList);
        List<Faxes> attachedFaxesList = new ArrayList<Faxes>();
        for (Faxes faxesListFaxesToAttach : entities.getFaxesList()) {
            faxesListFaxesToAttach = em.getReference(faxesListFaxesToAttach.getClass(), faxesListFaxesToAttach.getId());
            attachedFaxesList.add(faxesListFaxesToAttach);
        }
        entities.setFaxesList(attachedFaxesList);
        List<Phones> attachedPhonesList = new ArrayList<Phones>();
        for (Phones phonesListPhonesToAttach : entities.getPhonesList()) {
            phonesListPhonesToAttach = em.getReference(phonesListPhonesToAttach.getClass(), phonesListPhonesToAttach.getId());
            attachedPhonesList.add(phonesListPhonesToAttach);
        }
        entities.setPhonesList(attachedPhonesList);
        List<Address> attachedAddressList = new ArrayList<Address>();
        for (Address addressListAddressToAttach : entities.getAddressList()) {
            addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getId());
            attachedAddressList.add(addressListAddressToAttach);
        }
        entities.setAddressList(attachedAddressList);
        List<Services> attachedServicesList = new ArrayList<Services>();
        for (Services servicesListServicesToAttach : entities.getServicesList()) {
            servicesListServicesToAttach = em.getReference(servicesListServicesToAttach.getClass(), servicesListServicesToAttach.getId());
            attachedServicesList.add(servicesListServicesToAttach);
        }
        entities.setServicesList(attachedServicesList);
        List<Advertisers> attachedAdvertisersList = new ArrayList<Advertisers>();
        for (Advertisers advertisersListAdvertisersToAttach : entities.getAdvertisersList()) {
            advertisersListAdvertisersToAttach = em.getReference(advertisersListAdvertisersToAttach.getClass(), advertisersListAdvertisersToAttach.getId());
            attachedAdvertisersList.add(advertisersListAdvertisersToAttach);
        }
        entities.setAdvertisersList(attachedAdvertisersList);
        List<Clients> attachedClientsList = new ArrayList<Clients>();
        for (Clients clientsListClientsToAttach : entities.getClientsList()) {
            clientsListClientsToAttach = em.getReference(clientsListClientsToAttach.getClass(), clientsListClientsToAttach.getId());
            attachedClientsList.add(clientsListClientsToAttach);
        }
        entities.setClientsList(attachedClientsList);
        em.persist(entities);
        for (Emails emailsListEmails : entities.getEmailsList()) {
            emailsListEmails.getEntitiesList().add(entities);
            emailsListEmails = em.merge(emailsListEmails);
        }
        for (Faxes faxesListFaxes : entities.getFaxesList()) {
            faxesListFaxes.getEntitiesList().add(entities);
            faxesListFaxes = em.merge(faxesListFaxes);
        }
        for (Phones phonesListPhones : entities.getPhonesList()) {
            phonesListPhones.getEntitiesList().add(entities);
            phonesListPhones = em.merge(phonesListPhones);
        }
        for (Address addressListAddress : entities.getAddressList()) {
            addressListAddress.getEntitiesList().add(entities);
            addressListAddress = em.merge(addressListAddress);
        }
        for (Services servicesListServices : entities.getServicesList()) {
            Entities oldEntitiesIdOfServicesListServices = servicesListServices.getEntitiesId();
            servicesListServices.setEntitiesId(entities);
            servicesListServices = em.merge(servicesListServices);
            if (oldEntitiesIdOfServicesListServices != null) {
                oldEntitiesIdOfServicesListServices.getServicesList().remove(servicesListServices);
                oldEntitiesIdOfServicesListServices = em.merge(oldEntitiesIdOfServicesListServices);
            }
        }
        for (Advertisers advertisersListAdvertisers : entities.getAdvertisersList()) {
            Entities oldEntitiesIdOfAdvertisersListAdvertisers = advertisersListAdvertisers.getEntitiesId();
            advertisersListAdvertisers.setEntitiesId(entities);
            advertisersListAdvertisers = em.merge(advertisersListAdvertisers);
            if (oldEntitiesIdOfAdvertisersListAdvertisers != null) {
                oldEntitiesIdOfAdvertisersListAdvertisers.getAdvertisersList().remove(advertisersListAdvertisers);
                oldEntitiesIdOfAdvertisersListAdvertisers = em.merge(oldEntitiesIdOfAdvertisersListAdvertisers);
            }
        }
        for (Clients clientsListClients : entities.getClientsList()) {
            Entities oldEntitiesIdOfClientsListClients = clientsListClients.getEntitiesId();
            clientsListClients.setEntitiesId(entities);
            clientsListClients = em.merge(clientsListClients);
            if (oldEntitiesIdOfClientsListClients != null) {
                oldEntitiesIdOfClientsListClients.getClientsList().remove(clientsListClients);
                oldEntitiesIdOfClientsListClients = em.merge(oldEntitiesIdOfClientsListClients);
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

  public void edit(Entities entities) throws IllegalOrphanException, NonexistentEntityException, Exception {
      EntityManager em = null;
      try {
          em = getEntityManager();
          Entities persistentEntities = em.find(Entities.class, entities.getId());
          List<Emails> emailsListOld = persistentEntities.getEmailsList();
          List<Emails> emailsListNew = entities.getEmailsList();
          List<Faxes> faxesListOld = persistentEntities.getFaxesList();
          List<Faxes> faxesListNew = entities.getFaxesList();
          List<Phones> phonesListOld = persistentEntities.getPhonesList();
          List<Phones> phonesListNew = entities.getPhonesList();
          List<Address> addressListOld = persistentEntities.getAddressList();
          List<Address> addressListNew = entities.getAddressList();
          List<Services> servicesListOld = persistentEntities.getServicesList();
          List<Services> servicesListNew = entities.getServicesList();
          List<Advertisers> advertisersListOld = persistentEntities.getAdvertisersList();
          List<Advertisers> advertisersListNew = entities.getAdvertisersList();
          List<Clients> clientsListOld = persistentEntities.getClientsList();
          List<Clients> clientsListNew = entities.getClientsList();
          List<String> illegalOrphanMessages = null;
          for (Services servicesListOldServices : servicesListOld) {
              if (!servicesListNew.contains(servicesListOldServices)) {
                  if (illegalOrphanMessages == null) {
                      illegalOrphanMessages = new ArrayList<String>();
                  }
                  illegalOrphanMessages.add("You must retain Services " + servicesListOldServices + " since its entitiesId field is not nullable.");
              }
          }
          for (Advertisers advertisersListOldAdvertisers : advertisersListOld) {
              if (!advertisersListNew.contains(advertisersListOldAdvertisers)) {
                  if (illegalOrphanMessages == null) {
                      illegalOrphanMessages = new ArrayList<String>();
                  }
                  illegalOrphanMessages.add("You must retain Advertisers " + advertisersListOldAdvertisers + " since its entitiesId field is not nullable.");
              }
          }
          for (Clients clientsListOldClients : clientsListOld) {
              if (!clientsListNew.contains(clientsListOldClients)) {
                  if (illegalOrphanMessages == null) {
                      illegalOrphanMessages = new ArrayList<String>();
                  }
                  illegalOrphanMessages.add("You must retain Clients " + clientsListOldClients + " since its entitiesId field is not nullable.");
              }
          }
          if (illegalOrphanMessages != null) {
              throw new IllegalOrphanException(illegalOrphanMessages);
          }
          List<Emails> attachedEmailsListNew = new ArrayList<Emails>();
          for (Emails emailsListNewEmailsToAttach : emailsListNew) {
              emailsListNewEmailsToAttach = em.getReference(emailsListNewEmailsToAttach.getClass(), emailsListNewEmailsToAttach.getId());
              attachedEmailsListNew.add(emailsListNewEmailsToAttach);
          }
          emailsListNew = attachedEmailsListNew;
          entities.setEmailsList(emailsListNew);
          List<Faxes> attachedFaxesListNew = new ArrayList<Faxes>();
          for (Faxes faxesListNewFaxesToAttach : faxesListNew) {
              faxesListNewFaxesToAttach = em.getReference(faxesListNewFaxesToAttach.getClass(), faxesListNewFaxesToAttach.getId());
              attachedFaxesListNew.add(faxesListNewFaxesToAttach);
          }
          faxesListNew = attachedFaxesListNew;
          entities.setFaxesList(faxesListNew);
          List<Phones> attachedPhonesListNew = new ArrayList<Phones>();
          for (Phones phonesListNewPhonesToAttach : phonesListNew) {
              phonesListNewPhonesToAttach = em.getReference(phonesListNewPhonesToAttach.getClass(), phonesListNewPhonesToAttach.getId());
              attachedPhonesListNew.add(phonesListNewPhonesToAttach);
          }
          phonesListNew = attachedPhonesListNew;
          entities.setPhonesList(phonesListNew);
          List<Address> attachedAddressListNew = new ArrayList<Address>();
          for (Address addressListNewAddressToAttach : addressListNew) {
              addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getId());
              attachedAddressListNew.add(addressListNewAddressToAttach);
          }
          addressListNew = attachedAddressListNew;
          entities.setAddressList(addressListNew);
          List<Services> attachedServicesListNew = new ArrayList<Services>();
          for (Services servicesListNewServicesToAttach : servicesListNew) {
              servicesListNewServicesToAttach = em.getReference(servicesListNewServicesToAttach.getClass(), servicesListNewServicesToAttach.getId());
              attachedServicesListNew.add(servicesListNewServicesToAttach);
          }
          servicesListNew = attachedServicesListNew;
          entities.setServicesList(servicesListNew);
          List<Advertisers> attachedAdvertisersListNew = new ArrayList<Advertisers>();
          for (Advertisers advertisersListNewAdvertisersToAttach : advertisersListNew) {
              advertisersListNewAdvertisersToAttach = em.getReference(advertisersListNewAdvertisersToAttach.getClass(), advertisersListNewAdvertisersToAttach.getId());
              attachedAdvertisersListNew.add(advertisersListNewAdvertisersToAttach);
          }
          advertisersListNew = attachedAdvertisersListNew;
          entities.setAdvertisersList(advertisersListNew);
          List<Clients> attachedClientsListNew = new ArrayList<Clients>();
          for (Clients clientsListNewClientsToAttach : clientsListNew) {
              clientsListNewClientsToAttach = em.getReference(clientsListNewClientsToAttach.getClass(), clientsListNewClientsToAttach.getId());
              attachedClientsListNew.add(clientsListNewClientsToAttach);
          }
          clientsListNew = attachedClientsListNew;
          entities.setClientsList(clientsListNew);
          entities = em.merge(entities);
          for (Emails emailsListOldEmails : emailsListOld) {
              if (!emailsListNew.contains(emailsListOldEmails)) {
                  emailsListOldEmails.getEntitiesList().remove(entities);
                  emailsListOldEmails = em.merge(emailsListOldEmails);
              }
          }
          for (Emails emailsListNewEmails : emailsListNew) {
              if (!emailsListOld.contains(emailsListNewEmails)) {
                  emailsListNewEmails.getEntitiesList().add(entities);
                  emailsListNewEmails = em.merge(emailsListNewEmails);
              }
          }
          for (Faxes faxesListOldFaxes : faxesListOld) {
              if (!faxesListNew.contains(faxesListOldFaxes)) {
                  faxesListOldFaxes.getEntitiesList().remove(entities);
                  faxesListOldFaxes = em.merge(faxesListOldFaxes);
              }
          }
          for (Faxes faxesListNewFaxes : faxesListNew) {
              if (!faxesListOld.contains(faxesListNewFaxes)) {
                  faxesListNewFaxes.getEntitiesList().add(entities);
                  faxesListNewFaxes = em.merge(faxesListNewFaxes);
              }
          }
          for (Phones phonesListOldPhones : phonesListOld) {
              if (!phonesListNew.contains(phonesListOldPhones)) {
                  phonesListOldPhones.getEntitiesList().remove(entities);
                  phonesListOldPhones = em.merge(phonesListOldPhones);
              }
          }
          for (Phones phonesListNewPhones : phonesListNew) {
              if (!phonesListOld.contains(phonesListNewPhones)) {
                  phonesListNewPhones.getEntitiesList().add(entities);
                  phonesListNewPhones = em.merge(phonesListNewPhones);
              }
          }
          for (Address addressListOldAddress : addressListOld) {
              if (!addressListNew.contains(addressListOldAddress)) {
                  addressListOldAddress.getEntitiesList().remove(entities);
                  addressListOldAddress = em.merge(addressListOldAddress);
              }
          }
          for (Address addressListNewAddress : addressListNew) {
              if (!addressListOld.contains(addressListNewAddress)) {
                  addressListNewAddress.getEntitiesList().add(entities);
                  addressListNewAddress = em.merge(addressListNewAddress);
              }
          }
          for (Services servicesListNewServices : servicesListNew) {
              if (!servicesListOld.contains(servicesListNewServices)) {
                  Entities oldEntitiesIdOfServicesListNewServices = servicesListNewServices.getEntitiesId();
                  servicesListNewServices.setEntitiesId(entities);
                  servicesListNewServices = em.merge(servicesListNewServices);
                  if (oldEntitiesIdOfServicesListNewServices != null && !oldEntitiesIdOfServicesListNewServices.equals(entities)) {
                      oldEntitiesIdOfServicesListNewServices.getServicesList().remove(servicesListNewServices);
                      oldEntitiesIdOfServicesListNewServices = em.merge(oldEntitiesIdOfServicesListNewServices);
                  }
              }
          }
          for (Advertisers advertisersListNewAdvertisers : advertisersListNew) {
              if (!advertisersListOld.contains(advertisersListNewAdvertisers)) {
                  Entities oldEntitiesIdOfAdvertisersListNewAdvertisers = advertisersListNewAdvertisers.getEntitiesId();
                  advertisersListNewAdvertisers.setEntitiesId(entities);
                  advertisersListNewAdvertisers = em.merge(advertisersListNewAdvertisers);
                  if (oldEntitiesIdOfAdvertisersListNewAdvertisers != null && !oldEntitiesIdOfAdvertisersListNewAdvertisers.equals(entities)) {
                      oldEntitiesIdOfAdvertisersListNewAdvertisers.getAdvertisersList().remove(advertisersListNewAdvertisers);
                      oldEntitiesIdOfAdvertisersListNewAdvertisers = em.merge(oldEntitiesIdOfAdvertisersListNewAdvertisers);
                  }
              }
          }
          for (Clients clientsListNewClients : clientsListNew) {
              if (!clientsListOld.contains(clientsListNewClients)) {
                  Entities oldEntitiesIdOfClientsListNewClients = clientsListNewClients.getEntitiesId();
                  clientsListNewClients.setEntitiesId(entities);
                  clientsListNewClients = em.merge(clientsListNewClients);
                  if (oldEntitiesIdOfClientsListNewClients != null && !oldEntitiesIdOfClientsListNewClients.equals(entities)) {
                      oldEntitiesIdOfClientsListNewClients.getClientsList().remove(clientsListNewClients);
                      oldEntitiesIdOfClientsListNewClients = em.merge(oldEntitiesIdOfClientsListNewClients);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Entities entities;
            try {
                entities = em.getReference(Entities.class, id);
                entities.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entities with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Services> servicesListOrphanCheck = entities.getServicesList();
            for (Services servicesListOrphanCheckServices : servicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entities (" + entities + ") cannot be destroyed since the Services " + servicesListOrphanCheckServices + " in its servicesList field has a non-nullable entitiesId field.");
            }
            List<Advertisers> advertisersListOrphanCheck = entities.getAdvertisersList();
            for (Advertisers advertisersListOrphanCheckAdvertisers : advertisersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entities (" + entities + ") cannot be destroyed since the Advertisers " + advertisersListOrphanCheckAdvertisers + " in its advertisersList field has a non-nullable entitiesId field.");
            }
            List<Clients> clientsListOrphanCheck = entities.getClientsList();
            for (Clients clientsListOrphanCheckClients : clientsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entities (" + entities + ") cannot be destroyed since the Clients " + clientsListOrphanCheckClients + " in its clientsList field has a non-nullable entitiesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Emails> emailsList = entities.getEmailsList();
            for (Emails emailsListEmails : emailsList) {
                emailsListEmails.getEntitiesList().remove(entities);
                emailsListEmails = em.merge(emailsListEmails);
            }
            List<Faxes> faxesList = entities.getFaxesList();
            for (Faxes faxesListFaxes : faxesList) {
                faxesListFaxes.getEntitiesList().remove(entities);
                faxesListFaxes = em.merge(faxesListFaxes);
            }
            List<Phones> phonesList = entities.getPhonesList();
            for (Phones phonesListPhones : phonesList) {
                phonesListPhones.getEntitiesList().remove(entities);
                phonesListPhones = em.merge(phonesListPhones);
            }
            List<Address> addressList = entities.getAddressList();
            for (Address addressListAddress : addressList) {
                addressListAddress.getEntitiesList().remove(entities);
                addressListAddress = em.merge(addressListAddress);
            }
            em.remove(entities);
        } catch (Exception ex) {
          throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

  public List<Entities> findEntitiesEntities() {
    return findEntitiesEntities(true, -1, -1);
  }

  public List<Entities> findEntitiesEntities(int maxResults, int firstResult) {
    return findEntitiesEntities(false, maxResults, firstResult);
  }

  @SuppressWarnings("unchecked")
  private List<Entities> findEntitiesEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Entities> criteriaQuery = criteriaBuilder.createQuery(Entities.class);
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

  public Entities findEntities(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(Entities.class, id);
    } finally {
      em.close();
    }
  }

  public int getEntitiesCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Entities> entitiesRoot = criteriaQuery.from(Entities.class);
      criteriaQuery.select(criteriaBuilder.count(entitiesRoot));
      Query query = em.createQuery(criteriaQuery);
      return ((Long) query.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }

}
