/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaControladoresEntidades;

import Entidades.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.PrestamoLibro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpaControladoresEntidades.exceptions.IllegalOrphanException;
import jpaControladoresEntidades.exceptions.NonexistentEntityException;
import jpaControladoresEntidades.exceptions.RollbackFailureException;

/**
 *
 * @author mario
 */
public class ClienteJpaController implements Serializable {

     public ClienteJpaController(EntityManagerFactory emf) {
      
        this.emf = emf;
    }
    
    public ClienteJpaController() {
   
        this.emf = Persistence.createEntityManagerFactory("bibliteca");
    }
    
    
    //private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws RollbackFailureException, Exception {
        if (cliente.getPrestamoLibroList() == null) {
            cliente.setPrestamoLibroList(new ArrayList<PrestamoLibro>());
        }
        EntityManager em = null;
        try {
          
           
            em = getEntityManager();
            em.getTransaction().begin();
            List<PrestamoLibro> attachedPrestamoLibroList = new ArrayList<PrestamoLibro>();
            for (PrestamoLibro prestamoLibroListPrestamoLibroToAttach : cliente.getPrestamoLibroList()) {
                prestamoLibroListPrestamoLibroToAttach = em.getReference(prestamoLibroListPrestamoLibroToAttach.getClass(), prestamoLibroListPrestamoLibroToAttach.getIdPrestamoLibro());
                attachedPrestamoLibroList.add(prestamoLibroListPrestamoLibroToAttach);
            }
            cliente.setPrestamoLibroList(attachedPrestamoLibroList);
            em.persist(cliente);
            for (PrestamoLibro prestamoLibroListPrestamoLibro : cliente.getPrestamoLibroList()) {
                Cliente oldIdClienteOfPrestamoLibroListPrestamoLibro = prestamoLibroListPrestamoLibro.getIdCliente();
                prestamoLibroListPrestamoLibro.setIdCliente(cliente);
                prestamoLibroListPrestamoLibro = em.merge(prestamoLibroListPrestamoLibro);
                if (oldIdClienteOfPrestamoLibroListPrestamoLibro != null) {
                    oldIdClienteOfPrestamoLibroListPrestamoLibro.getPrestamoLibroList().remove(prestamoLibroListPrestamoLibro);
                    oldIdClienteOfPrestamoLibroListPrestamoLibro = em.merge(oldIdClienteOfPrestamoLibroListPrestamoLibro);
                }
            }
        
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
                em.getTransaction().rollback();
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

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
         
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<PrestamoLibro> prestamoLibroListOld = persistentCliente.getPrestamoLibroList();
            List<PrestamoLibro> prestamoLibroListNew = cliente.getPrestamoLibroList();
            List<String> illegalOrphanMessages = null;
            for (PrestamoLibro prestamoLibroListOldPrestamoLibro : prestamoLibroListOld) {
                if (!prestamoLibroListNew.contains(prestamoLibroListOldPrestamoLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrestamoLibro " + prestamoLibroListOldPrestamoLibro + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PrestamoLibro> attachedPrestamoLibroListNew = new ArrayList<PrestamoLibro>();
            for (PrestamoLibro prestamoLibroListNewPrestamoLibroToAttach : prestamoLibroListNew) {
                prestamoLibroListNewPrestamoLibroToAttach = em.getReference(prestamoLibroListNewPrestamoLibroToAttach.getClass(), prestamoLibroListNewPrestamoLibroToAttach.getIdPrestamoLibro());
                attachedPrestamoLibroListNew.add(prestamoLibroListNewPrestamoLibroToAttach);
            }
            prestamoLibroListNew = attachedPrestamoLibroListNew;
            cliente.setPrestamoLibroList(prestamoLibroListNew);
            cliente = em.merge(cliente);
            for (PrestamoLibro prestamoLibroListNewPrestamoLibro : prestamoLibroListNew) {
                if (!prestamoLibroListOld.contains(prestamoLibroListNewPrestamoLibro)) {
                    Cliente oldIdClienteOfPrestamoLibroListNewPrestamoLibro = prestamoLibroListNewPrestamoLibro.getIdCliente();
                    prestamoLibroListNewPrestamoLibro.setIdCliente(cliente);
                    prestamoLibroListNewPrestamoLibro = em.merge(prestamoLibroListNewPrestamoLibro);
                    if (oldIdClienteOfPrestamoLibroListNewPrestamoLibro != null && !oldIdClienteOfPrestamoLibroListNewPrestamoLibro.equals(cliente)) {
                        oldIdClienteOfPrestamoLibroListNewPrestamoLibro.getPrestamoLibroList().remove(prestamoLibroListNewPrestamoLibro);
                        oldIdClienteOfPrestamoLibroListNewPrestamoLibro = em.merge(oldIdClienteOfPrestamoLibroListNewPrestamoLibro);
                    }
                }
            }
          
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
            
               em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
          
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PrestamoLibro> prestamoLibroListOrphanCheck = cliente.getPrestamoLibroList();
            for (PrestamoLibro prestamoLibroListOrphanCheckPrestamoLibro : prestamoLibroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the PrestamoLibro " + prestamoLibroListOrphanCheckPrestamoLibro + " in its prestamoLibroList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
          
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
             
                em.getTransaction().rollback();
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
