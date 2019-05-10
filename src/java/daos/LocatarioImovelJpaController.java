/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Pessoa;
import beans.Imovel;
import beans.LocatarioImovel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class LocatarioImovelJpaController implements Serializable {

    public LocatarioImovelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    public List<LocatarioImovel> findByPessoa(int id){
        Query q = getEntityManager().createQuery("Select li from LocatarioImovel li where li.locatario.id = :id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    public void create(LocatarioImovel locatarioImovel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa locatario = locatarioImovel.getLocatario();
            if (locatario != null) {
                locatario = em.getReference(locatario.getClass(), locatario.getId());
                locatarioImovel.setLocatario(locatario);
            }
            Imovel imovel = locatarioImovel.getImovel();
            if (imovel != null) {
                imovel = em.getReference(imovel.getClass(), imovel.getId());
                locatarioImovel.setImovel(imovel);
            }
            em.persist(locatarioImovel);
            if (locatario != null) {
                locatario.getLocatarioImovelList().add(locatarioImovel);
                locatario = em.merge(locatario);
            }
            if (imovel != null) {
                imovel.getLocatarioImovelList().add(locatarioImovel);
                imovel = em.merge(imovel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LocatarioImovel locatarioImovel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LocatarioImovel persistentLocatarioImovel = em.find(LocatarioImovel.class, locatarioImovel.getId());
            Pessoa locatarioOld = persistentLocatarioImovel.getLocatario();
            Pessoa locatarioNew = locatarioImovel.getLocatario();
            Imovel imovelOld = persistentLocatarioImovel.getImovel();
            Imovel imovelNew = locatarioImovel.getImovel();
            if (locatarioNew != null) {
                locatarioNew = em.getReference(locatarioNew.getClass(), locatarioNew.getId());
                locatarioImovel.setLocatario(locatarioNew);
            }
            if (imovelNew != null) {
                imovelNew = em.getReference(imovelNew.getClass(), imovelNew.getId());
                locatarioImovel.setImovel(imovelNew);
            }
            locatarioImovel = em.merge(locatarioImovel);
            if (locatarioOld != null && !locatarioOld.equals(locatarioNew)) {
                locatarioOld.getLocatarioImovelList().remove(locatarioImovel);
                locatarioOld = em.merge(locatarioOld);
            }
            if (locatarioNew != null && !locatarioNew.equals(locatarioOld)) {
                locatarioNew.getLocatarioImovelList().add(locatarioImovel);
                locatarioNew = em.merge(locatarioNew);
            }
            if (imovelOld != null && !imovelOld.equals(imovelNew)) {
                imovelOld.getLocatarioImovelList().remove(locatarioImovel);
                imovelOld = em.merge(imovelOld);
            }
            if (imovelNew != null && !imovelNew.equals(imovelOld)) {
                imovelNew.getLocatarioImovelList().add(locatarioImovel);
                imovelNew = em.merge(imovelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = locatarioImovel.getId();
                if (findLocatarioImovel(id) == null) {
                    throw new NonexistentEntityException("The locatarioImovel with id " + id + " no longer exists.");
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
            LocatarioImovel locatarioImovel;
            try {
                locatarioImovel = em.getReference(LocatarioImovel.class, id);
                locatarioImovel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locatarioImovel with id " + id + " no longer exists.", enfe);
            }
            Pessoa locatario = locatarioImovel.getLocatario();
            if (locatario != null) {
                locatario.getLocatarioImovelList().remove(locatarioImovel);
                locatario = em.merge(locatario);
            }
            Imovel imovel = locatarioImovel.getImovel();
            if (imovel != null) {
                imovel.getLocatarioImovelList().remove(locatarioImovel);
                imovel = em.merge(imovel);
            }
            em.remove(locatarioImovel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LocatarioImovel> findLocatarioImovelEntities() {
        return findLocatarioImovelEntities(true, -1, -1);
    }

    public List<LocatarioImovel> findLocatarioImovelEntities(int maxResults, int firstResult) {
        return findLocatarioImovelEntities(false, maxResults, firstResult);
    }

    private List<LocatarioImovel> findLocatarioImovelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LocatarioImovel.class));
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

    public LocatarioImovel findLocatarioImovel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LocatarioImovel.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocatarioImovelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LocatarioImovel> rt = cq.from(LocatarioImovel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
