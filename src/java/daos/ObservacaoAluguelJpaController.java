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
import beans.Aluguel;
import beans.ObservacaoAluguel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class ObservacaoAluguelJpaController implements Serializable {

    public ObservacaoAluguelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ObservacaoAluguel observacaoAluguel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluguel aluguel = observacaoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel = em.getReference(aluguel.getClass(), aluguel.getId());
                observacaoAluguel.setAluguel(aluguel);
            }
            em.persist(observacaoAluguel);
            if (aluguel != null) {
                aluguel.getObservacaoAluguelList().add(observacaoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ObservacaoAluguel observacaoAluguel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObservacaoAluguel persistentObservacaoAluguel = em.find(ObservacaoAluguel.class, observacaoAluguel.getId());
            Aluguel aluguelOld = persistentObservacaoAluguel.getAluguel();
            Aluguel aluguelNew = observacaoAluguel.getAluguel();
            if (aluguelNew != null) {
                aluguelNew = em.getReference(aluguelNew.getClass(), aluguelNew.getId());
                observacaoAluguel.setAluguel(aluguelNew);
            }
            observacaoAluguel = em.merge(observacaoAluguel);
            if (aluguelOld != null && !aluguelOld.equals(aluguelNew)) {
                aluguelOld.getObservacaoAluguelList().remove(observacaoAluguel);
                aluguelOld = em.merge(aluguelOld);
            }
            if (aluguelNew != null && !aluguelNew.equals(aluguelOld)) {
                aluguelNew.getObservacaoAluguelList().add(observacaoAluguel);
                aluguelNew = em.merge(aluguelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = observacaoAluguel.getId();
                if (findObservacaoAluguel(id) == null) {
                    throw new NonexistentEntityException("The observacaoAluguel with id " + id + " no longer exists.");
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
            ObservacaoAluguel observacaoAluguel;
            try {
                observacaoAluguel = em.getReference(ObservacaoAluguel.class, id);
                observacaoAluguel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The observacaoAluguel with id " + id + " no longer exists.", enfe);
            }
            Aluguel aluguel = observacaoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel.getObservacaoAluguelList().remove(observacaoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.remove(observacaoAluguel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ObservacaoAluguel> findObservacaoAluguelEntities() {
        return findObservacaoAluguelEntities(true, -1, -1);
    }

    public List<ObservacaoAluguel> findObservacaoAluguelEntities(int maxResults, int firstResult) {
        return findObservacaoAluguelEntities(false, maxResults, firstResult);
    }

    private List<ObservacaoAluguel> findObservacaoAluguelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ObservacaoAluguel.class));
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

    public ObservacaoAluguel findObservacaoAluguel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ObservacaoAluguel.class, id);
        } finally {
            em.close();
        }
    }

    public int getObservacaoAluguelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ObservacaoAluguel> rt = cq.from(ObservacaoAluguel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
