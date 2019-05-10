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
import beans.DedoDuro;
import beans.Usuario;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getDedoDuroList() == null) {
            usuario.setDedoDuroList(new ArrayList<DedoDuro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = usuario.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getId());
                usuario.setGrupo(grupo);
            }
            List<DedoDuro> attachedDedoDuroList = new ArrayList<DedoDuro>();
            for (DedoDuro dedoDuroListDedoDuroToAttach : usuario.getDedoDuroList()) {
                dedoDuroListDedoDuroToAttach = em.getReference(dedoDuroListDedoDuroToAttach.getClass(), dedoDuroListDedoDuroToAttach.getId());
                attachedDedoDuroList.add(dedoDuroListDedoDuroToAttach);
            }
            usuario.setDedoDuroList(attachedDedoDuroList);
            em.persist(usuario);
            if (grupo != null) {
                grupo.getUsuarioList().add(usuario);
                grupo = em.merge(grupo);
            }
            for (DedoDuro dedoDuroListDedoDuro : usuario.getDedoDuroList()) {
                Usuario oldUsuarioOfDedoDuroListDedoDuro = dedoDuroListDedoDuro.getUsuario();
                dedoDuroListDedoDuro.setUsuario(usuario);
                dedoDuroListDedoDuro = em.merge(dedoDuroListDedoDuro);
                if (oldUsuarioOfDedoDuroListDedoDuro != null) {
                    oldUsuarioOfDedoDuroListDedoDuro.getDedoDuroList().remove(dedoDuroListDedoDuro);
                    oldUsuarioOfDedoDuroListDedoDuro = em.merge(oldUsuarioOfDedoDuroListDedoDuro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Grupo grupoOld = persistentUsuario.getGrupo();
            Grupo grupoNew = usuario.getGrupo();
            List<DedoDuro> dedoDuroListOld = persistentUsuario.getDedoDuroList();
            List<DedoDuro> dedoDuroListNew = usuario.getDedoDuroList();
            List<String> illegalOrphanMessages = null;
            for (DedoDuro dedoDuroListOldDedoDuro : dedoDuroListOld) {
                if (!dedoDuroListNew.contains(dedoDuroListOldDedoDuro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DedoDuro " + dedoDuroListOldDedoDuro + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getId());
                usuario.setGrupo(grupoNew);
            }
            List<DedoDuro> attachedDedoDuroListNew = new ArrayList<DedoDuro>();
            for (DedoDuro dedoDuroListNewDedoDuroToAttach : dedoDuroListNew) {
                dedoDuroListNewDedoDuroToAttach = em.getReference(dedoDuroListNewDedoDuroToAttach.getClass(), dedoDuroListNewDedoDuroToAttach.getId());
                attachedDedoDuroListNew.add(dedoDuroListNewDedoDuroToAttach);
            }
            dedoDuroListNew = attachedDedoDuroListNew;
            usuario.setDedoDuroList(dedoDuroListNew);
            usuario = em.merge(usuario);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getUsuarioList().remove(usuario);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getUsuarioList().add(usuario);
                grupoNew = em.merge(grupoNew);
            }
            for (DedoDuro dedoDuroListNewDedoDuro : dedoDuroListNew) {
                if (!dedoDuroListOld.contains(dedoDuroListNewDedoDuro)) {
                    Usuario oldUsuarioOfDedoDuroListNewDedoDuro = dedoDuroListNewDedoDuro.getUsuario();
                    dedoDuroListNewDedoDuro.setUsuario(usuario);
                    dedoDuroListNewDedoDuro = em.merge(dedoDuroListNewDedoDuro);
                    if (oldUsuarioOfDedoDuroListNewDedoDuro != null && !oldUsuarioOfDedoDuroListNewDedoDuro.equals(usuario)) {
                        oldUsuarioOfDedoDuroListNewDedoDuro.getDedoDuroList().remove(dedoDuroListNewDedoDuro);
                        oldUsuarioOfDedoDuroListNewDedoDuro = em.merge(oldUsuarioOfDedoDuroListNewDedoDuro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DedoDuro> dedoDuroListOrphanCheck = usuario.getDedoDuroList();
            for (DedoDuro dedoDuroListOrphanCheckDedoDuro : dedoDuroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the DedoDuro " + dedoDuroListOrphanCheckDedoDuro + " in its dedoDuroList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupo grupo = usuario.getGrupo();
            if (grupo != null) {
                grupo.getUsuarioList().remove(usuario);
                grupo = em.merge(grupo);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario login(String user, String password) {
        Query q = getEntityManager().createQuery("Select u from Usuario u where u.usuario  = :user and u.senha = :password");
        q.setParameter("user", user);
        q.setParameter("password", password);
        if (q.getResultList().isEmpty()) {
            return null;
        }
        return (Usuario) q.getSingleResult();
    }

}
