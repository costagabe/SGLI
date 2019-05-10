/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Grupo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Permissoes;
import java.util.ArrayList;
import java.util.List;
import beans.Usuario;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) {
        if (grupo.getPermissoesList() == null) {
            grupo.setPermissoesList(new ArrayList<Permissoes>());
        }
        if (grupo.getUsuarioList() == null) {
            grupo.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permissoes> attachedPermissoesList = new ArrayList<Permissoes>();
            for (Permissoes permissoesListPermissoesToAttach : grupo.getPermissoesList()) {
                permissoesListPermissoesToAttach = em.getReference(permissoesListPermissoesToAttach.getClass(), permissoesListPermissoesToAttach.getId());
                attachedPermissoesList.add(permissoesListPermissoesToAttach);
            }
            grupo.setPermissoesList(attachedPermissoesList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : grupo.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getId());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            grupo.setUsuarioList(attachedUsuarioList);
            em.persist(grupo);
            for (Permissoes permissoesListPermissoes : grupo.getPermissoesList()) {
                Grupo oldGrupoOfPermissoesListPermissoes = permissoesListPermissoes.getGrupo();
                permissoesListPermissoes.setGrupo(grupo);
                permissoesListPermissoes = em.merge(permissoesListPermissoes);
                if (oldGrupoOfPermissoesListPermissoes != null) {
                    oldGrupoOfPermissoesListPermissoes.getPermissoesList().remove(permissoesListPermissoes);
                    oldGrupoOfPermissoesListPermissoes = em.merge(oldGrupoOfPermissoesListPermissoes);
                }
            }
            for (Usuario usuarioListUsuario : grupo.getUsuarioList()) {
                Grupo oldGrupoOfUsuarioListUsuario = usuarioListUsuario.getGrupo();
                usuarioListUsuario.setGrupo(grupo);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldGrupoOfUsuarioListUsuario != null) {
                    oldGrupoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldGrupoOfUsuarioListUsuario = em.merge(oldGrupoOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getId());
            List<Permissoes> permissoesListOld = persistentGrupo.getPermissoesList();
            List<Permissoes> permissoesListNew = grupo.getPermissoesList();
            List<Usuario> usuarioListOld = persistentGrupo.getUsuarioList();
            List<Usuario> usuarioListNew = grupo.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Permissoes permissoesListOldPermissoes : permissoesListOld) {
                if (!permissoesListNew.contains(permissoesListOldPermissoes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permissoes " + permissoesListOldPermissoes + " since its grupo field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its grupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permissoes> attachedPermissoesListNew = new ArrayList<Permissoes>();
            for (Permissoes permissoesListNewPermissoesToAttach : permissoesListNew) {
                permissoesListNewPermissoesToAttach = em.getReference(permissoesListNewPermissoesToAttach.getClass(), permissoesListNewPermissoesToAttach.getId());
                attachedPermissoesListNew.add(permissoesListNewPermissoesToAttach);
            }
            permissoesListNew = attachedPermissoesListNew;
            grupo.setPermissoesList(permissoesListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getId());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            grupo.setUsuarioList(usuarioListNew);
            grupo = em.merge(grupo);
            for (Permissoes permissoesListNewPermissoes : permissoesListNew) {
                if (!permissoesListOld.contains(permissoesListNewPermissoes)) {
                    Grupo oldGrupoOfPermissoesListNewPermissoes = permissoesListNewPermissoes.getGrupo();
                    permissoesListNewPermissoes.setGrupo(grupo);
                    permissoesListNewPermissoes = em.merge(permissoesListNewPermissoes);
                    if (oldGrupoOfPermissoesListNewPermissoes != null && !oldGrupoOfPermissoesListNewPermissoes.equals(grupo)) {
                        oldGrupoOfPermissoesListNewPermissoes.getPermissoesList().remove(permissoesListNewPermissoes);
                        oldGrupoOfPermissoesListNewPermissoes = em.merge(oldGrupoOfPermissoesListNewPermissoes);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Grupo oldGrupoOfUsuarioListNewUsuario = usuarioListNewUsuario.getGrupo();
                    usuarioListNewUsuario.setGrupo(grupo);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldGrupoOfUsuarioListNewUsuario != null && !oldGrupoOfUsuarioListNewUsuario.equals(grupo)) {
                        oldGrupoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldGrupoOfUsuarioListNewUsuario = em.merge(oldGrupoOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grupo.getId();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Permissoes> permissoesListOrphanCheck = grupo.getPermissoesList();
            for (Permissoes permissoesListOrphanCheckPermissoes : permissoesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Permissoes " + permissoesListOrphanCheckPermissoes + " in its permissoesList field has a non-nullable grupo field.");
            }
            List<Usuario> usuarioListOrphanCheck = grupo.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable grupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
