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
import beans.Grupo;
import beans.Permissoes;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class PermissoesJpaController implements Serializable {

    public PermissoesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permissoes permissoes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = permissoes.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getId());
                permissoes.setGrupo(grupo);
            }
            em.persist(permissoes);
            if (grupo != null) {
                grupo.getPermissoesList().add(permissoes);
                grupo = em.merge(grupo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permissoes permissoes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permissoes persistentPermissoes = em.find(Permissoes.class, permissoes.getId());
            Grupo grupoOld = persistentPermissoes.getGrupo();
            Grupo grupoNew = permissoes.getGrupo();
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getId());
                permissoes.setGrupo(grupoNew);
            }
            permissoes = em.merge(permissoes);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getPermissoesList().remove(permissoes);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getPermissoesList().add(permissoes);
                grupoNew = em.merge(grupoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permissoes.getId();
                if (findPermissoes(id) == null) {
                    throw new NonexistentEntityException("The permissoes with id " + id + " no longer exists.");
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
            Permissoes permissoes;
            try {
                permissoes = em.getReference(Permissoes.class, id);
                permissoes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permissoes with id " + id + " no longer exists.", enfe);
            }
            Grupo grupo = permissoes.getGrupo();
            if (grupo != null) {
                grupo.getPermissoesList().remove(permissoes);
                grupo = em.merge(grupo);
            }
            em.remove(permissoes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permissoes> findPermissoesEntities() {
        return findPermissoesEntities(true, -1, -1);
    }

    public List<Permissoes> findPermissoesEntities(int maxResults, int firstResult) {
        return findPermissoesEntities(false, maxResults, firstResult);
    }

    private List<Permissoes> findPermissoesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permissoes.class));
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

    public Permissoes findPermissoes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permissoes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermissoesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permissoes> rt = cq.from(Permissoes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
