/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaControladoresEntidades;

import Entidades.Libro;
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
import javax.transaction.UserTransaction;
import jpaControladoresEntidades.exceptions.NonexistentEntityException;
import jpaControladoresEntidades.exceptions.RollbackFailureException;

/**
 *
 * @author mario
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) throws RollbackFailureException, Exception {
        if (libro.getPrestamoLibroList() == null) {
            libro.setPrestamoLibroList(new ArrayList<PrestamoLibro>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PrestamoLibro> attachedPrestamoLibroList = new ArrayList<PrestamoLibro>();
            for (PrestamoLibro prestamoLibroListPrestamoLibroToAttach : libro.getPrestamoLibroList()) {
                prestamoLibroListPrestamoLibroToAttach = em.getReference(prestamoLibroListPrestamoLibroToAttach.getClass(), prestamoLibroListPrestamoLibroToAttach.getIdPrestamoLibro());
                attachedPrestamoLibroList.add(prestamoLibroListPrestamoLibroToAttach);
            }
            libro.setPrestamoLibroList(attachedPrestamoLibroList);
            em.persist(libro);
            for (PrestamoLibro prestamoLibroListPrestamoLibro : libro.getPrestamoLibroList()) {
                Libro oldIdLibroOfPrestamoLibroListPrestamoLibro = prestamoLibroListPrestamoLibro.getIdLibro();
                prestamoLibroListPrestamoLibro.setIdLibro(libro);
                prestamoLibroListPrestamoLibro = em.merge(prestamoLibroListPrestamoLibro);
                if (oldIdLibroOfPrestamoLibroListPrestamoLibro != null) {
                    oldIdLibroOfPrestamoLibroListPrestamoLibro.getPrestamoLibroList().remove(prestamoLibroListPrestamoLibro);
                    oldIdLibroOfPrestamoLibroListPrestamoLibro = em.merge(oldIdLibroOfPrestamoLibroListPrestamoLibro);
                }
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

    public void edit(Libro libro) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Libro persistentLibro = em.find(Libro.class, libro.getIdLibro());
            List<PrestamoLibro> prestamoLibroListOld = persistentLibro.getPrestamoLibroList();
            List<PrestamoLibro> prestamoLibroListNew = libro.getPrestamoLibroList();
            List<PrestamoLibro> attachedPrestamoLibroListNew = new ArrayList<PrestamoLibro>();
            for (PrestamoLibro prestamoLibroListNewPrestamoLibroToAttach : prestamoLibroListNew) {
                prestamoLibroListNewPrestamoLibroToAttach = em.getReference(prestamoLibroListNewPrestamoLibroToAttach.getClass(), prestamoLibroListNewPrestamoLibroToAttach.getIdPrestamoLibro());
                attachedPrestamoLibroListNew.add(prestamoLibroListNewPrestamoLibroToAttach);
            }
            prestamoLibroListNew = attachedPrestamoLibroListNew;
            libro.setPrestamoLibroList(prestamoLibroListNew);
            libro = em.merge(libro);
            for (PrestamoLibro prestamoLibroListOldPrestamoLibro : prestamoLibroListOld) {
                if (!prestamoLibroListNew.contains(prestamoLibroListOldPrestamoLibro)) {
                    prestamoLibroListOldPrestamoLibro.setIdLibro(null);
                    prestamoLibroListOldPrestamoLibro = em.merge(prestamoLibroListOldPrestamoLibro);
                }
            }
            for (PrestamoLibro prestamoLibroListNewPrestamoLibro : prestamoLibroListNew) {
                if (!prestamoLibroListOld.contains(prestamoLibroListNewPrestamoLibro)) {
                    Libro oldIdLibroOfPrestamoLibroListNewPrestamoLibro = prestamoLibroListNewPrestamoLibro.getIdLibro();
                    prestamoLibroListNewPrestamoLibro.setIdLibro(libro);
                    prestamoLibroListNewPrestamoLibro = em.merge(prestamoLibroListNewPrestamoLibro);
                    if (oldIdLibroOfPrestamoLibroListNewPrestamoLibro != null && !oldIdLibroOfPrestamoLibroListNewPrestamoLibro.equals(libro)) {
                        oldIdLibroOfPrestamoLibroListNewPrestamoLibro.getPrestamoLibroList().remove(prestamoLibroListNewPrestamoLibro);
                        oldIdLibroOfPrestamoLibroListNewPrestamoLibro = em.merge(oldIdLibroOfPrestamoLibroListNewPrestamoLibro);
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
                Integer id = libro.getIdLibro();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
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
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getIdLibro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<PrestamoLibro> prestamoLibroList = libro.getPrestamoLibroList();
            for (PrestamoLibro prestamoLibroListPrestamoLibro : prestamoLibroList) {
                prestamoLibroListPrestamoLibro.setIdLibro(null);
                prestamoLibroListPrestamoLibro = em.merge(prestamoLibroListPrestamoLibro);
            }
            em.remove(libro);
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

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
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

    public Libro findLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
