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
import beans.PagamentoAluguel;
import beans.Pessoa;
import daos.exceptions.NonexistentEntityException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class PagamentoAluguelJpaController implements Serializable {

    public PagamentoAluguelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<PagamentoAluguel> relatorioLocador(Pessoa p, Date dataInicio, Date dataFim) {
        Query q = getEntityManager().createQuery("Select pa from PagamentoAluguel pa  inner join Aluguel a on a = pa.aluguel inner join Imovel i on i = a.imovel inner join LocatarioImovel li on li.imovel = i  where li.locatario = :locatario and pa.dataEfetuacao BETWEEN :dataInicio and :dataFim order by pa.dataEfetuacao");
        q.setParameter("locatario", p);
        q.setParameter("dataInicio", dataInicio);
        q.setParameter("dataFim", dataFim);
        return q.getResultList();
    }
    public List<PagamentoAluguel> relatorioLocatario(Pessoa p, Date dataInicio, Date dataFim) {
        Query q = getEntityManager().createQuery("Select pa from PagamentoAluguel pa  inner join Aluguel a on a = pa.aluguel inner join LocatarioAluguel la on la.aluguel = a  where la.locatario = :locatario and pa.dataEfetuacao BETWEEN :dataInicio and :dataFim order by pa.dataEfetuacao");
        q.setParameter("locatario", p);
        q.setParameter("dataInicio", dataInicio);
        q.setParameter("dataFim", dataFim);
        return q.getResultList();
    }

    public void create(PagamentoAluguel pagamentoAluguel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluguel aluguel = pagamentoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel = em.getReference(aluguel.getClass(), aluguel.getId());
                pagamentoAluguel.setAluguel(aluguel);
            }
            em.persist(pagamentoAluguel);
            if (aluguel != null) {
                aluguel.getPagamentoAluguelList().add(pagamentoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PagamentoAluguel pagamentoAluguel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagamentoAluguel persistentPagamentoAluguel = em.find(PagamentoAluguel.class, pagamentoAluguel.getId());
            Aluguel aluguelOld = persistentPagamentoAluguel.getAluguel();
            Aluguel aluguelNew = pagamentoAluguel.getAluguel();
            if (aluguelNew != null) {
                aluguelNew = em.getReference(aluguelNew.getClass(), aluguelNew.getId());
                pagamentoAluguel.setAluguel(aluguelNew);
            }
            pagamentoAluguel = em.merge(pagamentoAluguel);
            if (aluguelOld != null && !aluguelOld.equals(aluguelNew)) {
                aluguelOld.getPagamentoAluguelList().remove(pagamentoAluguel);
                aluguelOld = em.merge(aluguelOld);
            }
            if (aluguelNew != null && !aluguelNew.equals(aluguelOld)) {
                aluguelNew.getPagamentoAluguelList().add(pagamentoAluguel);
                aluguelNew = em.merge(aluguelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagamentoAluguel.getId();
                if (findPagamentoAluguel(id) == null) {
                    throw new NonexistentEntityException("The pagamentoAluguel with id " + id + " no longer exists.");
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
            PagamentoAluguel pagamentoAluguel;
            try {
                pagamentoAluguel = em.getReference(PagamentoAluguel.class, id);
                pagamentoAluguel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagamentoAluguel with id " + id + " no longer exists.", enfe);
            }
            Aluguel aluguel = pagamentoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel.getPagamentoAluguelList().remove(pagamentoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.remove(pagamentoAluguel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PagamentoAluguel> findPagamentoAluguelEntities() {
        return findPagamentoAluguelEntities(true, -1, -1);
    }

    public List<PagamentoAluguel> findPagamentoAluguelEntities(int maxResults, int firstResult) {
        return findPagamentoAluguelEntities(false, maxResults, firstResult);
    }

    private List<PagamentoAluguel> findPagamentoAluguelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PagamentoAluguel.class));
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

    public PagamentoAluguel findPagamentoAluguel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PagamentoAluguel.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagamentoAluguelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PagamentoAluguel> rt = cq.from(PagamentoAluguel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
