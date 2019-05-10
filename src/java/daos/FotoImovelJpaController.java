/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.FotoImovel;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Imovel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class FotoImovelJpaController implements Serializable {

    public FotoImovelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FotoImovel fotoImovel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imovel imovel = fotoImovel.getImovel();
            if (imovel != null) {
                imovel = em.getReference(imovel.getClass(), imovel.getId());
                fotoImovel.setImovel(imovel);
            }
            em.persist(fotoImovel);
            if (imovel != null) {
                imovel.getFotoImovelList().add(fotoImovel);
                imovel = em.merge(imovel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FotoImovel fotoImovel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FotoImovel persistentFotoImovel = em.find(FotoImovel.class, fotoImovel.getId());
            Imovel imovelOld = persistentFotoImovel.getImovel();
            Imovel imovelNew = fotoImovel.getImovel();
            if (imovelNew != null) {
                imovelNew = em.getReference(imovelNew.getClass(), imovelNew.getId());
                fotoImovel.setImovel(imovelNew);
            }
            fotoImovel = em.merge(fotoImovel);
            if (imovelOld != null && !imovelOld.equals(imovelNew)) {
                imovelOld.getFotoImovelList().remove(fotoImovel);
                imovelOld = em.merge(imovelOld);
            }
            if (imovelNew != null && !imovelNew.equals(imovelOld)) {
                imovelNew.getFotoImovelList().add(fotoImovel);
                imovelNew = em.merge(imovelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fotoImovel.getId();
                if (findFotoImovel(id) == null) {
                    throw new NonexistentEntityException("The fotoImovel with id " + id + " no longer exists.");
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
            FotoImovel fotoImovel;
            try {
                fotoImovel = em.getReference(FotoImovel.class, id);
                fotoImovel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fotoImovel with id " + id + " no longer exists.", enfe);
            }
            Imovel imovel = fotoImovel.getImovel();
            if (imovel != null) {
                imovel.getFotoImovelList().remove(fotoImovel);
                imovel = em.merge(imovel);
            }
            em.remove(fotoImovel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FotoImovel> findFotoImovelEntities() {
        return findFotoImovelEntities(true, -1, -1);
    }

    public List<FotoImovel> findFotoImovelEntities(int maxResults, int firstResult) {
        return findFotoImovelEntities(false, maxResults, firstResult);
    }

    private List<FotoImovel> findFotoImovelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FotoImovel.class));
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

    public FotoImovel findFotoImovel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FotoImovel.class, id);
        } finally {
            em.close();
        }
    }

    public int getFotoImovelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FotoImovel> rt = cq.from(FotoImovel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
