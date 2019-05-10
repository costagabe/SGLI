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
import beans.Endereco;
import beans.LocatarioImovel;
import java.util.ArrayList;
import java.util.List;
import beans.DocumentoPessoa;
import beans.LocatarioAluguel;
import beans.Pessoa;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Pessoa> findAllByNome(String nome) {
        Query q = getEntityManager().createQuery("SELECT p FROM Pessoa p WHERE p.nome LIKE :nome");
        q.setParameter("nome", "%"+nome.replace("\"", "")+"%");
        List result = q.getResultList();
        System.out.println(nome);
        System.out.println(q.toString());
        return result;

    }

    public Pessoa findByNome(String nome) {
        Query q = getEntityManager().createNamedQuery("Pessoa.findByNome");
        q.setParameter("nome", nome);
        System.out.println(q.toString());
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Pessoa) q.getResultList().get(0);
        }
    }

    public void create(Pessoa pessoa) {
        if (pessoa.getLocatarioImovelList() == null) {
            pessoa.setLocatarioImovelList(new ArrayList<LocatarioImovel>());
        }
        if (pessoa.getDocumentoPessoaList() == null) {
            pessoa.setDocumentoPessoaList(new ArrayList<DocumentoPessoa>());
        }
        if (pessoa.getLocatarioAluguelList() == null) {
            pessoa.setLocatarioAluguelList(new ArrayList<LocatarioAluguel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco endereco = pessoa.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getId());
                pessoa.setEndereco(endereco);
            }
            List<LocatarioImovel> attachedLocatarioImovelList = new ArrayList<LocatarioImovel>();
            for (LocatarioImovel locatarioImovelListLocatarioImovelToAttach : pessoa.getLocatarioImovelList()) {
                locatarioImovelListLocatarioImovelToAttach = em.getReference(locatarioImovelListLocatarioImovelToAttach.getClass(), locatarioImovelListLocatarioImovelToAttach.getId());
                attachedLocatarioImovelList.add(locatarioImovelListLocatarioImovelToAttach);
            }
            pessoa.setLocatarioImovelList(attachedLocatarioImovelList);
            List<DocumentoPessoa> attachedDocumentoPessoaList = new ArrayList<DocumentoPessoa>();
            for (DocumentoPessoa documentoPessoaListDocumentoPessoaToAttach : pessoa.getDocumentoPessoaList()) {
                documentoPessoaListDocumentoPessoaToAttach = em.getReference(documentoPessoaListDocumentoPessoaToAttach.getClass(), documentoPessoaListDocumentoPessoaToAttach.getId());
                attachedDocumentoPessoaList.add(documentoPessoaListDocumentoPessoaToAttach);
            }
            pessoa.setDocumentoPessoaList(attachedDocumentoPessoaList);
            List<LocatarioAluguel> attachedLocatarioAluguelList = new ArrayList<LocatarioAluguel>();
            for (LocatarioAluguel locatarioAluguelListLocatarioAluguelToAttach : pessoa.getLocatarioAluguelList()) {
                locatarioAluguelListLocatarioAluguelToAttach = em.getReference(locatarioAluguelListLocatarioAluguelToAttach.getClass(), locatarioAluguelListLocatarioAluguelToAttach.getId());
                attachedLocatarioAluguelList.add(locatarioAluguelListLocatarioAluguelToAttach);
            }
            pessoa.setLocatarioAluguelList(attachedLocatarioAluguelList);
            em.persist(pessoa);
            if (endereco != null) {
                endereco.getPessoaList().add(pessoa);
                endereco = em.merge(endereco);
            }
            for (LocatarioImovel locatarioImovelListLocatarioImovel : pessoa.getLocatarioImovelList()) {
                Pessoa oldLocatarioOfLocatarioImovelListLocatarioImovel = locatarioImovelListLocatarioImovel.getLocatario();
                locatarioImovelListLocatarioImovel.setLocatario(pessoa);
                locatarioImovelListLocatarioImovel = em.merge(locatarioImovelListLocatarioImovel);
                if (oldLocatarioOfLocatarioImovelListLocatarioImovel != null) {
                    oldLocatarioOfLocatarioImovelListLocatarioImovel.getLocatarioImovelList().remove(locatarioImovelListLocatarioImovel);
                    oldLocatarioOfLocatarioImovelListLocatarioImovel = em.merge(oldLocatarioOfLocatarioImovelListLocatarioImovel);
                }
            }
            for (DocumentoPessoa documentoPessoaListDocumentoPessoa : pessoa.getDocumentoPessoaList()) {
                Pessoa oldPessoaOfDocumentoPessoaListDocumentoPessoa = documentoPessoaListDocumentoPessoa.getPessoa();
                documentoPessoaListDocumentoPessoa.setPessoa(pessoa);
                documentoPessoaListDocumentoPessoa = em.merge(documentoPessoaListDocumentoPessoa);
                if (oldPessoaOfDocumentoPessoaListDocumentoPessoa != null) {
                    oldPessoaOfDocumentoPessoaListDocumentoPessoa.getDocumentoPessoaList().remove(documentoPessoaListDocumentoPessoa);
                    oldPessoaOfDocumentoPessoaListDocumentoPessoa = em.merge(oldPessoaOfDocumentoPessoaListDocumentoPessoa);
                }
            }
            for (LocatarioAluguel locatarioAluguelListLocatarioAluguel : pessoa.getLocatarioAluguelList()) {
                Pessoa oldLocatarioOfLocatarioAluguelListLocatarioAluguel = locatarioAluguelListLocatarioAluguel.getLocatario();
                locatarioAluguelListLocatarioAluguel.setLocatario(pessoa);
                locatarioAluguelListLocatarioAluguel = em.merge(locatarioAluguelListLocatarioAluguel);
                if (oldLocatarioOfLocatarioAluguelListLocatarioAluguel != null) {
                    oldLocatarioOfLocatarioAluguelListLocatarioAluguel.getLocatarioAluguelList().remove(locatarioAluguelListLocatarioAluguel);
                    oldLocatarioOfLocatarioAluguelListLocatarioAluguel = em.merge(oldLocatarioOfLocatarioAluguelListLocatarioAluguel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getId());
            Endereco enderecoOld = persistentPessoa.getEndereco();
            Endereco enderecoNew = pessoa.getEndereco();
            List<LocatarioImovel> locatarioImovelListOld = persistentPessoa.getLocatarioImovelList();
            List<LocatarioImovel> locatarioImovelListNew = pessoa.getLocatarioImovelList();
            List<DocumentoPessoa> documentoPessoaListOld = persistentPessoa.getDocumentoPessoaList();
            List<DocumentoPessoa> documentoPessoaListNew = pessoa.getDocumentoPessoaList();
            List<LocatarioAluguel> locatarioAluguelListOld = persistentPessoa.getLocatarioAluguelList();
            List<LocatarioAluguel> locatarioAluguelListNew = pessoa.getLocatarioAluguelList();
            List<String> illegalOrphanMessages = null;
            for (LocatarioImovel locatarioImovelListOldLocatarioImovel : locatarioImovelListOld) {
                if (!locatarioImovelListNew.contains(locatarioImovelListOldLocatarioImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LocatarioImovel " + locatarioImovelListOldLocatarioImovel + " since its locatario field is not nullable.");
                }
            }
            for (DocumentoPessoa documentoPessoaListOldDocumentoPessoa : documentoPessoaListOld) {
                if (!documentoPessoaListNew.contains(documentoPessoaListOldDocumentoPessoa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoPessoa " + documentoPessoaListOldDocumentoPessoa + " since its pessoa field is not nullable.");
                }
            }
            for (LocatarioAluguel locatarioAluguelListOldLocatarioAluguel : locatarioAluguelListOld) {
                if (!locatarioAluguelListNew.contains(locatarioAluguelListOldLocatarioAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LocatarioAluguel " + locatarioAluguelListOldLocatarioAluguel + " since its locatario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getId());
                pessoa.setEndereco(enderecoNew);
            }
            List<LocatarioImovel> attachedLocatarioImovelListNew = new ArrayList<LocatarioImovel>();
            for (LocatarioImovel locatarioImovelListNewLocatarioImovelToAttach : locatarioImovelListNew) {
                locatarioImovelListNewLocatarioImovelToAttach = em.getReference(locatarioImovelListNewLocatarioImovelToAttach.getClass(), locatarioImovelListNewLocatarioImovelToAttach.getId());
                attachedLocatarioImovelListNew.add(locatarioImovelListNewLocatarioImovelToAttach);
            }
            locatarioImovelListNew = attachedLocatarioImovelListNew;
            pessoa.setLocatarioImovelList(locatarioImovelListNew);
            List<DocumentoPessoa> attachedDocumentoPessoaListNew = new ArrayList<DocumentoPessoa>();
            for (DocumentoPessoa documentoPessoaListNewDocumentoPessoaToAttach : documentoPessoaListNew) {
                documentoPessoaListNewDocumentoPessoaToAttach = em.getReference(documentoPessoaListNewDocumentoPessoaToAttach.getClass(), documentoPessoaListNewDocumentoPessoaToAttach.getId());
                attachedDocumentoPessoaListNew.add(documentoPessoaListNewDocumentoPessoaToAttach);
            }
            documentoPessoaListNew = attachedDocumentoPessoaListNew;
            pessoa.setDocumentoPessoaList(documentoPessoaListNew);
            List<LocatarioAluguel> attachedLocatarioAluguelListNew = new ArrayList<LocatarioAluguel>();
            for (LocatarioAluguel locatarioAluguelListNewLocatarioAluguelToAttach : locatarioAluguelListNew) {
                locatarioAluguelListNewLocatarioAluguelToAttach = em.getReference(locatarioAluguelListNewLocatarioAluguelToAttach.getClass(), locatarioAluguelListNewLocatarioAluguelToAttach.getId());
                attachedLocatarioAluguelListNew.add(locatarioAluguelListNewLocatarioAluguelToAttach);
            }
            locatarioAluguelListNew = attachedLocatarioAluguelListNew;
            pessoa.setLocatarioAluguelList(locatarioAluguelListNew);
            pessoa = em.merge(pessoa);
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getPessoaList().remove(pessoa);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getPessoaList().add(pessoa);
                enderecoNew = em.merge(enderecoNew);
            }
            for (LocatarioImovel locatarioImovelListNewLocatarioImovel : locatarioImovelListNew) {
                if (!locatarioImovelListOld.contains(locatarioImovelListNewLocatarioImovel)) {
                    Pessoa oldLocatarioOfLocatarioImovelListNewLocatarioImovel = locatarioImovelListNewLocatarioImovel.getLocatario();
                    locatarioImovelListNewLocatarioImovel.setLocatario(pessoa);
                    locatarioImovelListNewLocatarioImovel = em.merge(locatarioImovelListNewLocatarioImovel);
                    if (oldLocatarioOfLocatarioImovelListNewLocatarioImovel != null && !oldLocatarioOfLocatarioImovelListNewLocatarioImovel.equals(pessoa)) {
                        oldLocatarioOfLocatarioImovelListNewLocatarioImovel.getLocatarioImovelList().remove(locatarioImovelListNewLocatarioImovel);
                        oldLocatarioOfLocatarioImovelListNewLocatarioImovel = em.merge(oldLocatarioOfLocatarioImovelListNewLocatarioImovel);
                    }
                }
            }
            for (DocumentoPessoa documentoPessoaListNewDocumentoPessoa : documentoPessoaListNew) {
                if (!documentoPessoaListOld.contains(documentoPessoaListNewDocumentoPessoa)) {
                    Pessoa oldPessoaOfDocumentoPessoaListNewDocumentoPessoa = documentoPessoaListNewDocumentoPessoa.getPessoa();
                    documentoPessoaListNewDocumentoPessoa.setPessoa(pessoa);
                    documentoPessoaListNewDocumentoPessoa = em.merge(documentoPessoaListNewDocumentoPessoa);
                    if (oldPessoaOfDocumentoPessoaListNewDocumentoPessoa != null && !oldPessoaOfDocumentoPessoaListNewDocumentoPessoa.equals(pessoa)) {
                        oldPessoaOfDocumentoPessoaListNewDocumentoPessoa.getDocumentoPessoaList().remove(documentoPessoaListNewDocumentoPessoa);
                        oldPessoaOfDocumentoPessoaListNewDocumentoPessoa = em.merge(oldPessoaOfDocumentoPessoaListNewDocumentoPessoa);
                    }
                }
            }
            for (LocatarioAluguel locatarioAluguelListNewLocatarioAluguel : locatarioAluguelListNew) {
                if (!locatarioAluguelListOld.contains(locatarioAluguelListNewLocatarioAluguel)) {
                    Pessoa oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel = locatarioAluguelListNewLocatarioAluguel.getLocatario();
                    locatarioAluguelListNewLocatarioAluguel.setLocatario(pessoa);
                    locatarioAluguelListNewLocatarioAluguel = em.merge(locatarioAluguelListNewLocatarioAluguel);
                    if (oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel != null && !oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel.equals(pessoa)) {
                        oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel.getLocatarioAluguelList().remove(locatarioAluguelListNewLocatarioAluguel);
                        oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel = em.merge(oldLocatarioOfLocatarioAluguelListNewLocatarioAluguel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getId();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
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
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LocatarioImovel> locatarioImovelListOrphanCheck = pessoa.getLocatarioImovelList();
            for (LocatarioImovel locatarioImovelListOrphanCheckLocatarioImovel : locatarioImovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the LocatarioImovel " + locatarioImovelListOrphanCheckLocatarioImovel + " in its locatarioImovelList field has a non-nullable locatario field.");
            }
            List<DocumentoPessoa> documentoPessoaListOrphanCheck = pessoa.getDocumentoPessoaList();
            for (DocumentoPessoa documentoPessoaListOrphanCheckDocumentoPessoa : documentoPessoaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the DocumentoPessoa " + documentoPessoaListOrphanCheckDocumentoPessoa + " in its documentoPessoaList field has a non-nullable pessoa field.");
            }
            List<LocatarioAluguel> locatarioAluguelListOrphanCheck = pessoa.getLocatarioAluguelList();
            for (LocatarioAluguel locatarioAluguelListOrphanCheckLocatarioAluguel : locatarioAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the LocatarioAluguel " + locatarioAluguelListOrphanCheckLocatarioAluguel + " in its locatarioAluguelList field has a non-nullable locatario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Endereco endereco = pessoa.getEndereco();
            if (endereco != null) {
                endereco.getPessoaList().remove(pessoa);
                endereco = em.merge(endereco);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
