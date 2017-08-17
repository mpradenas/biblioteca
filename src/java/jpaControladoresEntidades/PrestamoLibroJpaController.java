/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaControladoresEntidades;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Cliente;
import Entidades.Libro;
import Entidades.PrestamoLibro;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jpaControladoresEntidades.exceptions.NonexistentEntityException;
import jpaControladoresEntidades.exceptions.RollbackFailureException;

/**
 *
 * @author mario
 */
public class PrestamoLibroJpaController implements Serializable {

    public PrestamoLibroJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrestamoLibro prestamoLibro) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idCliente = prestamoLibro.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                prestamoLibro.setIdCliente(idCliente);
            }
            Libro idLibro = prestamoLibro.getIdLibro();
            if (idLibro != null) {
                idLibro = em.getReference(idLibro.getClass(), idLibro.getIdLibro());
                prestamoLibro.setIdLibro(idLibro);
            }
            em.persist(prestamoLibro);
            if (idCliente != null) {
                idCliente.getPrestamoLibroList().add(prestamoLibro);
                idCliente = em.merge(idCliente);
            }
            if (idLibro != null) {
                idLibro.getPrestamoLibroList().add(prestamoLibro);
                idLibro = em.merge(idLibro);
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

    public void edit(PrestamoLibro prestamoLibro) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PrestamoLibro persistentPrestamoLibro = em.find(PrestamoLibro.class, prestamoLibro.getIdPrestamoLibro());
            Cliente idClienteOld = persistentPrestamoLibro.getIdCliente();
            Cliente idClienteNew = prestamoLibro.getIdCliente();
            Libro idLibroOld = persistentPrestamoLibro.getIdLibro();
            Libro idLibroNew = prestamoLibro.getIdLibro();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                prestamoLibro.setIdCliente(idClienteNew);
            }
            if (idLibroNew != null) {
                idLibroNew = em.getReference(idLibroNew.getClass(), idLibroNew.getIdLibro());
                prestamoLibro.setIdLibro(idLibroNew);
            }
            prestamoLibro = em.merge(prestamoLibro);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getPrestamoLibroList().remove(prestamoLibro);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getPrestamoLibroList().add(prestamoLibro);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idLibroOld != null && !idLibroOld.equals(idLibroNew)) {
                idLibroOld.getPrestamoLibroList().remove(prestamoLibro);
                idLibroOld = em.merge(idLibroOld);
            }
            if (idLibroNew != null && !idLibroNew.equals(idLibroOld)) {
                idLibroNew.getPrestamoLibroList().add(prestamoLibro);
                idLibroNew = em.merge(idLibroNew);
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
                Integer id = prestamoLibro.getIdPrestamoLibro();
                if (findPrestamoLibro(id) == null) {
                    throw new NonexistentEntityException("The prestamoLibro with id " + id + " no longer exists.");
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
            PrestamoLibro prestamoLibro;
            try {
                prestamoLibro = em.getReference(PrestamoLibro.class, id);
                prestamoLibro.getIdPrestamoLibro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamoLibro with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = prestamoLibro.getIdCliente();
            if (idCliente != null) {
                idCliente.getPrestamoLibroList().remove(prestamoLibro);
                idCliente = em.merge(idCliente);
            }
            Libro idLibro = prestamoLibro.getIdLibro();
            if (idLibro != null) {
                idLibro.getPrestamoLibroList().remove(prestamoLibro);
                idLibro = em.merge(idLibro);
            }
            em.remove(prestamoLibro);
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

    public List<PrestamoLibro> findPrestamoLibroEntities() {
        return findPrestamoLibroEntities(true, -1, -1);
    }

    public List<PrestamoLibro> findPrestamoLibroEntities(int maxResults, int firstResult) {
        return findPrestamoLibroEntities(false, maxResults, firstResult);
    }

    private List<PrestamoLibro> findPrestamoLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrestamoLibro.class));
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

    public PrestamoLibro findPrestamoLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrestamoLibro.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamoLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrestamoLibro> rt = cq.from(PrestamoLibro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
