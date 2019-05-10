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
import beans.Aluguel;
import beans.LocatarioAluguel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class LocatarioAluguelJpaController implements Serializable {

    public LocatarioAluguelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LocatarioAluguel locatarioAluguel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa locatario = locatarioAluguel.getLocatario();
            if (locatario != null) {
                locatario = em.getReference(locatario.getClass(), locatario.getId());
                locatarioAluguel.setLocatario(locatario);
            }
            Aluguel aluguel = locatarioAluguel.getAluguel();
            if (aluguel != null) {
                aluguel = em.getReference(aluguel.getClass(), aluguel.getId());
                locatarioAluguel.setAluguel(aluguel);
            }
            em.persist(locatarioAluguel);
            if (locatario != null) {
                locatario.getLocatarioAluguelList().add(locatarioAluguel);
                locatario = em.merge(locatario);
            }
            if (aluguel != null) {
                aluguel.getLocatarioAluguelList().add(locatarioAluguel);
                aluguel = em.merge(aluguel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LocatarioAluguel locatarioAluguel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LocatarioAluguel persistentLocatarioAluguel = em.find(LocatarioAluguel.class, locatarioAluguel.getId());
            Pessoa locatarioOld = persistentLocatarioAluguel.getLocatario();
            Pessoa locatarioNew = locatarioAluguel.getLocatario();
            Aluguel aluguelOld = persistentLocatarioAluguel.getAluguel();
            Aluguel aluguelNew = locatarioAluguel.getAluguel();
            if (locatarioNew != null) {
                locatarioNew = em.getReference(locatarioNew.getClass(), locatarioNew.getId());
                locatarioAluguel.setLocatario(locatarioNew);
            }
            if (aluguelNew != null) {
                aluguelNew = em.getReference(aluguelNew.getClass(), aluguelNew.getId());
                locatarioAluguel.setAluguel(aluguelNew);
            }
            locatarioAluguel = em.merge(locatarioAluguel);
            if (locatarioOld != null && !locatarioOld.equals(locatarioNew)) {
                locatarioOld.getLocatarioAluguelList().remove(locatarioAluguel);
                locatarioOld = em.merge(locatarioOld);
            }
            if (locatarioNew != null && !locatarioNew.equals(locatarioOld)) {
                locatarioNew.getLocatarioAluguelList().add(locatarioAluguel);
                locatarioNew = em.merge(locatarioNew);
            }
            if (aluguelOld != null && !aluguelOld.equals(aluguelNew)) {
                aluguelOld.getLocatarioAluguelList().remove(locatarioAluguel);
                aluguelOld = em.merge(aluguelOld);
            }
            if (aluguelNew != null && !aluguelNew.equals(aluguelOld)) {
                aluguelNew.getLocatarioAluguelList().add(locatarioAluguel);
                aluguelNew = em.merge(aluguelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = locatarioAluguel.getId();
                if (findLocatarioAluguel(id) == null) {
                    throw new NonexistentEntityException("The locatarioAluguel with id " + id + " no longer exists.");
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
            LocatarioAluguel locatarioAluguel;
            try {
                locatarioAluguel = em.getReference(LocatarioAluguel.class, id);
                locatarioAluguel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locatarioAluguel with id " + id + " no longer exists.", enfe);
            }
            Pessoa locatario = locatarioAluguel.getLocatario();
            if (locatario != null) {
                locatario.getLocatarioAluguelList().remove(locatarioAluguel);
                locatario = em.merge(locatario);
            }
            Aluguel aluguel = locatarioAluguel.getAluguel();
            if (aluguel != null) {
                aluguel.getLocatarioAluguelList().remove(locatarioAluguel);
                aluguel = em.merge(aluguel);
            }
            em.remove(locatarioAluguel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LocatarioAluguel> findLocatarioAluguelEntities() {
        return findLocatarioAluguelEntities(true, -1, -1);
    }

    public List<LocatarioAluguel> findLocatarioAluguelEntities(int maxResults, int firstResult) {
        return findLocatarioAluguelEntities(false, maxResults, firstResult);
    }

    private List<LocatarioAluguel> findLocatarioAluguelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LocatarioAluguel.class));
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

    public LocatarioAluguel findLocatarioAluguel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LocatarioAluguel.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocatarioAluguelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LocatarioAluguel> rt = cq.from(LocatarioAluguel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
