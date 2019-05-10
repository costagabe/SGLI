/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Endereco;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Pessoa;
import java.util.ArrayList;
import java.util.List;
import beans.Imovel;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class EnderecoJpaController implements Serializable {

    public EnderecoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Endereco endereco) {
        if (endereco.getPessoaList() == null) {
            endereco.setPessoaList(new ArrayList<Pessoa>());
        }
        if (endereco.getImovelList() == null) {
            endereco.setImovelList(new ArrayList<Imovel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pessoa> attachedPessoaList = new ArrayList<Pessoa>();
            for (Pessoa pessoaListPessoaToAttach : endereco.getPessoaList()) {
                pessoaListPessoaToAttach = em.getReference(pessoaListPessoaToAttach.getClass(), pessoaListPessoaToAttach.getId());
                attachedPessoaList.add(pessoaListPessoaToAttach);
            }
            endereco.setPessoaList(attachedPessoaList);
            List<Imovel> attachedImovelList = new ArrayList<Imovel>();
            for (Imovel imovelListImovelToAttach : endereco.getImovelList()) {
                imovelListImovelToAttach = em.getReference(imovelListImovelToAttach.getClass(), imovelListImovelToAttach.getId());
                attachedImovelList.add(imovelListImovelToAttach);
            }
            endereco.setImovelList(attachedImovelList);
            em.persist(endereco);
            for (Pessoa pessoaListPessoa : endereco.getPessoaList()) {
                Endereco oldEnderecoOfPessoaListPessoa = pessoaListPessoa.getEndereco();
                pessoaListPessoa.setEndereco(endereco);
                pessoaListPessoa = em.merge(pessoaListPessoa);
                if (oldEnderecoOfPessoaListPessoa != null) {
                    oldEnderecoOfPessoaListPessoa.getPessoaList().remove(pessoaListPessoa);
                    oldEnderecoOfPessoaListPessoa = em.merge(oldEnderecoOfPessoaListPessoa);
                }
            }
            for (Imovel imovelListImovel : endereco.getImovelList()) {
                Endereco oldEnderecoOfImovelListImovel = imovelListImovel.getEndereco();
                imovelListImovel.setEndereco(endereco);
                imovelListImovel = em.merge(imovelListImovel);
                if (oldEnderecoOfImovelListImovel != null) {
                    oldEnderecoOfImovelListImovel.getImovelList().remove(imovelListImovel);
                    oldEnderecoOfImovelListImovel = em.merge(oldEnderecoOfImovelListImovel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Endereco endereco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getId());
            List<Pessoa> pessoaListOld = persistentEndereco.getPessoaList();
            List<Pessoa> pessoaListNew = endereco.getPessoaList();
            List<Imovel> imovelListOld = persistentEndereco.getImovelList();
            List<Imovel> imovelListNew = endereco.getImovelList();
            List<String> illegalOrphanMessages = null;
            for (Pessoa pessoaListOldPessoa : pessoaListOld) {
                if (!pessoaListNew.contains(pessoaListOldPessoa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pessoa " + pessoaListOldPessoa + " since its endereco field is not nullable.");
                }
            }
            for (Imovel imovelListOldImovel : imovelListOld) {
                if (!imovelListNew.contains(imovelListOldImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Imovel " + imovelListOldImovel + " since its endereco field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pessoa> attachedPessoaListNew = new ArrayList<Pessoa>();
            for (Pessoa pessoaListNewPessoaToAttach : pessoaListNew) {
                pessoaListNewPessoaToAttach = em.getReference(pessoaListNewPessoaToAttach.getClass(), pessoaListNewPessoaToAttach.getId());
                attachedPessoaListNew.add(pessoaListNewPessoaToAttach);
            }
            pessoaListNew = attachedPessoaListNew;
            endereco.setPessoaList(pessoaListNew);
            List<Imovel> attachedImovelListNew = new ArrayList<Imovel>();
            for (Imovel imovelListNewImovelToAttach : imovelListNew) {
                imovelListNewImovelToAttach = em.getReference(imovelListNewImovelToAttach.getClass(), imovelListNewImovelToAttach.getId());
                attachedImovelListNew.add(imovelListNewImovelToAttach);
            }
            imovelListNew = attachedImovelListNew;
            endereco.setImovelList(imovelListNew);
            endereco = em.merge(endereco);
            for (Pessoa pessoaListNewPessoa : pessoaListNew) {
                if (!pessoaListOld.contains(pessoaListNewPessoa)) {
                    Endereco oldEnderecoOfPessoaListNewPessoa = pessoaListNewPessoa.getEndereco();
                    pessoaListNewPessoa.setEndereco(endereco);
                    pessoaListNewPessoa = em.merge(pessoaListNewPessoa);
                    if (oldEnderecoOfPessoaListNewPessoa != null && !oldEnderecoOfPessoaListNewPessoa.equals(endereco)) {
                        oldEnderecoOfPessoaListNewPessoa.getPessoaList().remove(pessoaListNewPessoa);
                        oldEnderecoOfPessoaListNewPessoa = em.merge(oldEnderecoOfPessoaListNewPessoa);
                    }
                }
            }
            for (Imovel imovelListNewImovel : imovelListNew) {
                if (!imovelListOld.contains(imovelListNewImovel)) {
                    Endereco oldEnderecoOfImovelListNewImovel = imovelListNewImovel.getEndereco();
                    imovelListNewImovel.setEndereco(endereco);
                    imovelListNewImovel = em.merge(imovelListNewImovel);
                    if (oldEnderecoOfImovelListNewImovel != null && !oldEnderecoOfImovelListNewImovel.equals(endereco)) {
                        oldEnderecoOfImovelListNewImovel.getImovelList().remove(imovelListNewImovel);
                        oldEnderecoOfImovelListNewImovel = em.merge(oldEnderecoOfImovelListNewImovel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getId();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
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
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pessoa> pessoaListOrphanCheck = endereco.getPessoaList();
            for (Pessoa pessoaListOrphanCheckPessoa : pessoaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Endereco (" + endereco + ") cannot be destroyed since the Pessoa " + pessoaListOrphanCheckPessoa + " in its pessoaList field has a non-nullable endereco field.");
            }
            List<Imovel> imovelListOrphanCheck = endereco.getImovelList();
            for (Imovel imovelListOrphanCheckImovel : imovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Endereco (" + endereco + ") cannot be destroyed since the Imovel " + imovelListOrphanCheckImovel + " in its imovelList field has a non-nullable endereco field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(endereco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
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

    public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
