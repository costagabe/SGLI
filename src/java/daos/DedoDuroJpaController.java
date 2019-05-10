/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.DedoDuro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Usuario;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class DedoDuroJpaController implements Serializable {

    public DedoDuroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DedoDuro dedoDuro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = dedoDuro.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                dedoDuro.setUsuario(usuario);
            }
            em.persist(dedoDuro);
            if (usuario != null) {
                usuario.getDedoDuroList().add(dedoDuro);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DedoDuro dedoDuro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DedoDuro persistentDedoDuro = em.find(DedoDuro.class, dedoDuro.getId());
            Usuario usuarioOld = persistentDedoDuro.getUsuario();
            Usuario usuarioNew = dedoDuro.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                dedoDuro.setUsuario(usuarioNew);
            }
            dedoDuro = em.merge(dedoDuro);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getDedoDuroList().remove(dedoDuro);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getDedoDuroList().add(dedoDuro);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dedoDuro.getId();
                if (findDedoDuro(id) == null) {
                    throw new NonexistentEntityException("The dedoDuro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DedoDuro dedoDuro;
            try {
                dedoDuro = em.getReference(DedoDuro.class, id);
                dedoDuro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dedoDuro with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = dedoDuro.getUsuario();
            if (usuario != null) {
                usuario.getDedoDuroList().remove(dedoDuro);
                usuario = em.merge(usuario);
            }
            em.remove(dedoDuro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DedoDuro> findDedoDuroEntities() {
        return findDedoDuroEntities(true, -1, -1);
    }

    public List<DedoDuro> findDedoDuroEntities(int maxResults, int firstResult) {
        return findDedoDuroEntities(false, maxResults, firstResult);
    }

    private List<DedoDuro> findDedoDuroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DedoDuro.class));
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

    public DedoDuro findDedoDuro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DedoDuro.class, id);
        } finally {
            em.close();
        }
    }

    public int getDedoDuroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DedoDuro> rt = cq.from(DedoDuro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
