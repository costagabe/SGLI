/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Documento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.DocumentoAluguel;
import java.util.ArrayList;
import java.util.List;
import beans.DocumentoPessoa;
import beans.DocumentoImovel;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gabriel Alves
 */
public class DocumentoJpaController implements Serializable {

    public DocumentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Documento findByNomeAndTipo(String nome, int tipo) {
        Query q = getEntityManager().createQuery("SELECT d FROM Documento d where d.tipo = :tipo and UPPER(d.nome) = UPPER(:nome)");
        q.setParameter("nome", nome);
        q.setParameter("tipo", tipo);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Documento) q.getSingleResult();
        }
    }

    public void create(Documento documento) {
        if (documento.getDocumentoAluguelList() == null) {
            documento.setDocumentoAluguelList(new ArrayList<DocumentoAluguel>());
        }
        if (documento.getDocumentoPessoaList() == null) {
            documento.setDocumentoPessoaList(new ArrayList<DocumentoPessoa>());
        }
        if (documento.getDocumentoImovelList() == null) {
            documento.setDocumentoImovelList(new ArrayList<DocumentoImovel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DocumentoAluguel> attachedDocumentoAluguelList = new ArrayList<DocumentoAluguel>();
            for (DocumentoAluguel documentoAluguelListDocumentoAluguelToAttach : documento.getDocumentoAluguelList()) {
                documentoAluguelListDocumentoAluguelToAttach = em.getReference(documentoAluguelListDocumentoAluguelToAttach.getClass(), documentoAluguelListDocumentoAluguelToAttach.getId());
                attachedDocumentoAluguelList.add(documentoAluguelListDocumentoAluguelToAttach);
            }
            documento.setDocumentoAluguelList(attachedDocumentoAluguelList);
            List<DocumentoPessoa> attachedDocumentoPessoaList = new ArrayList<DocumentoPessoa>();
            for (DocumentoPessoa documentoPessoaListDocumentoPessoaToAttach : documento.getDocumentoPessoaList()) {
                documentoPessoaListDocumentoPessoaToAttach = em.getReference(documentoPessoaListDocumentoPessoaToAttach.getClass(), documentoPessoaListDocumentoPessoaToAttach.getId());
                attachedDocumentoPessoaList.add(documentoPessoaListDocumentoPessoaToAttach);
            }
            documento.setDocumentoPessoaList(attachedDocumentoPessoaList);
            List<DocumentoImovel> attachedDocumentoImovelList = new ArrayList<DocumentoImovel>();
            for (DocumentoImovel documentoImovelListDocumentoImovelToAttach : documento.getDocumentoImovelList()) {
                documentoImovelListDocumentoImovelToAttach = em.getReference(documentoImovelListDocumentoImovelToAttach.getClass(), documentoImovelListDocumentoImovelToAttach.getId());
                attachedDocumentoImovelList.add(documentoImovelListDocumentoImovelToAttach);
            }
            documento.setDocumentoImovelList(attachedDocumentoImovelList);
            em.persist(documento);
            for (DocumentoAluguel documentoAluguelListDocumentoAluguel : documento.getDocumentoAluguelList()) {
                Documento oldDocumentoOfDocumentoAluguelListDocumentoAluguel = documentoAluguelListDocumentoAluguel.getDocumento();
                documentoAluguelListDocumentoAluguel.setDocumento(documento);
                documentoAluguelListDocumentoAluguel = em.merge(documentoAluguelListDocumentoAluguel);
                if (oldDocumentoOfDocumentoAluguelListDocumentoAluguel != null) {
                    oldDocumentoOfDocumentoAluguelListDocumentoAluguel.getDocumentoAluguelList().remove(documentoAluguelListDocumentoAluguel);
                    oldDocumentoOfDocumentoAluguelListDocumentoAluguel = em.merge(oldDocumentoOfDocumentoAluguelListDocumentoAluguel);
                }
            }
            for (DocumentoPessoa documentoPessoaListDocumentoPessoa : documento.getDocumentoPessoaList()) {
                Documento oldDocumentoOfDocumentoPessoaListDocumentoPessoa = documentoPessoaListDocumentoPessoa.getDocumento();
                documentoPessoaListDocumentoPessoa.setDocumento(documento);
                documentoPessoaListDocumentoPessoa = em.merge(documentoPessoaListDocumentoPessoa);
                if (oldDocumentoOfDocumentoPessoaListDocumentoPessoa != null) {
                    oldDocumentoOfDocumentoPessoaListDocumentoPessoa.getDocumentoPessoaList().remove(documentoPessoaListDocumentoPessoa);
                    oldDocumentoOfDocumentoPessoaListDocumentoPessoa = em.merge(oldDocumentoOfDocumentoPessoaListDocumentoPessoa);
                }
            }
            for (DocumentoImovel documentoImovelListDocumentoImovel : documento.getDocumentoImovelList()) {
                Documento oldDocumentoOfDocumentoImovelListDocumentoImovel = documentoImovelListDocumentoImovel.getDocumento();
                documentoImovelListDocumentoImovel.setDocumento(documento);
                documentoImovelListDocumentoImovel = em.merge(documentoImovelListDocumentoImovel);
                if (oldDocumentoOfDocumentoImovelListDocumentoImovel != null) {
                    oldDocumentoOfDocumentoImovelListDocumentoImovel.getDocumentoImovelList().remove(documentoImovelListDocumentoImovel);
                    oldDocumentoOfDocumentoImovelListDocumentoImovel = em.merge(oldDocumentoOfDocumentoImovelListDocumentoImovel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documento documento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento persistentDocumento = em.find(Documento.class, documento.getId());
            List<DocumentoAluguel> documentoAluguelListOld = persistentDocumento.getDocumentoAluguelList();
            List<DocumentoAluguel> documentoAluguelListNew = documento.getDocumentoAluguelList();
            List<DocumentoPessoa> documentoPessoaListOld = persistentDocumento.getDocumentoPessoaList();
            List<DocumentoPessoa> documentoPessoaListNew = documento.getDocumentoPessoaList();
            List<DocumentoImovel> documentoImovelListOld = persistentDocumento.getDocumentoImovelList();
            List<DocumentoImovel> documentoImovelListNew = documento.getDocumentoImovelList();
            List<String> illegalOrphanMessages = null;
            for (DocumentoAluguel documentoAluguelListOldDocumentoAluguel : documentoAluguelListOld) {
                if (!documentoAluguelListNew.contains(documentoAluguelListOldDocumentoAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoAluguel " + documentoAluguelListOldDocumentoAluguel + " since its documento field is not nullable.");
                }
            }
            for (DocumentoPessoa documentoPessoaListOldDocumentoPessoa : documentoPessoaListOld) {
                if (!documentoPessoaListNew.contains(documentoPessoaListOldDocumentoPessoa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoPessoa " + documentoPessoaListOldDocumentoPessoa + " since its documento field is not nullable.");
                }
            }
            for (DocumentoImovel documentoImovelListOldDocumentoImovel : documentoImovelListOld) {
                if (!documentoImovelListNew.contains(documentoImovelListOldDocumentoImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoImovel " + documentoImovelListOldDocumentoImovel + " since its documento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DocumentoAluguel> attachedDocumentoAluguelListNew = new ArrayList<DocumentoAluguel>();
            for (DocumentoAluguel documentoAluguelListNewDocumentoAluguelToAttach : documentoAluguelListNew) {
                documentoAluguelListNewDocumentoAluguelToAttach = em.getReference(documentoAluguelListNewDocumentoAluguelToAttach.getClass(), documentoAluguelListNewDocumentoAluguelToAttach.getId());
                attachedDocumentoAluguelListNew.add(documentoAluguelListNewDocumentoAluguelToAttach);
            }
            documentoAluguelListNew = attachedDocumentoAluguelListNew;
            documento.setDocumentoAluguelList(documentoAluguelListNew);
            List<DocumentoPessoa> attachedDocumentoPessoaListNew = new ArrayList<DocumentoPessoa>();
            for (DocumentoPessoa documentoPessoaListNewDocumentoPessoaToAttach : documentoPessoaListNew) {
                documentoPessoaListNewDocumentoPessoaToAttach = em.getReference(documentoPessoaListNewDocumentoPessoaToAttach.getClass(), documentoPessoaListNewDocumentoPessoaToAttach.getId());
                attachedDocumentoPessoaListNew.add(documentoPessoaListNewDocumentoPessoaToAttach);
            }
            documentoPessoaListNew = attachedDocumentoPessoaListNew;
            documento.setDocumentoPessoaList(documentoPessoaListNew);
            List<DocumentoImovel> attachedDocumentoImovelListNew = new ArrayList<DocumentoImovel>();
            for (DocumentoImovel documentoImovelListNewDocumentoImovelToAttach : documentoImovelListNew) {
                documentoImovelListNewDocumentoImovelToAttach = em.getReference(documentoImovelListNewDocumentoImovelToAttach.getClass(), documentoImovelListNewDocumentoImovelToAttach.getId());
                attachedDocumentoImovelListNew.add(documentoImovelListNewDocumentoImovelToAttach);
            }
            documentoImovelListNew = attachedDocumentoImovelListNew;
            documento.setDocumentoImovelList(documentoImovelListNew);
            documento = em.merge(documento);
            for (DocumentoAluguel documentoAluguelListNewDocumentoAluguel : documentoAluguelListNew) {
                if (!documentoAluguelListOld.contains(documentoAluguelListNewDocumentoAluguel)) {
                    Documento oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel = documentoAluguelListNewDocumentoAluguel.getDocumento();
                    documentoAluguelListNewDocumentoAluguel.setDocumento(documento);
                    documentoAluguelListNewDocumentoAluguel = em.merge(documentoAluguelListNewDocumentoAluguel);
                    if (oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel != null && !oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel.equals(documento)) {
                        oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel.getDocumentoAluguelList().remove(documentoAluguelListNewDocumentoAluguel);
                        oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel = em.merge(oldDocumentoOfDocumentoAluguelListNewDocumentoAluguel);
                    }
                }
            }
            for (DocumentoPessoa documentoPessoaListNewDocumentoPessoa : documentoPessoaListNew) {
                if (!documentoPessoaListOld.contains(documentoPessoaListNewDocumentoPessoa)) {
                    Documento oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa = documentoPessoaListNewDocumentoPessoa.getDocumento();
                    documentoPessoaListNewDocumentoPessoa.setDocumento(documento);
                    documentoPessoaListNewDocumentoPessoa = em.merge(documentoPessoaListNewDocumentoPessoa);
                    if (oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa != null && !oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa.equals(documento)) {
                        oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa.getDocumentoPessoaList().remove(documentoPessoaListNewDocumentoPessoa);
                        oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa = em.merge(oldDocumentoOfDocumentoPessoaListNewDocumentoPessoa);
                    }
                }
            }
            for (DocumentoImovel documentoImovelListNewDocumentoImovel : documentoImovelListNew) {
                if (!documentoImovelListOld.contains(documentoImovelListNewDocumentoImovel)) {
                    Documento oldDocumentoOfDocumentoImovelListNewDocumentoImovel = documentoImovelListNewDocumentoImovel.getDocumento();
                    documentoImovelListNewDocumentoImovel.setDocumento(documento);
                    documentoImovelListNewDocumentoImovel = em.merge(documentoImovelListNewDocumentoImovel);
                    if (oldDocumentoOfDocumentoImovelListNewDocumentoImovel != null && !oldDocumentoOfDocumentoImovelListNewDocumentoImovel.equals(documento)) {
                        oldDocumentoOfDocumentoImovelListNewDocumentoImovel.getDocumentoImovelList().remove(documentoImovelListNewDocumentoImovel);
                        oldDocumentoOfDocumentoImovelListNewDocumentoImovel = em.merge(oldDocumentoOfDocumentoImovelListNewDocumentoImovel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documento.getId();
                if (findDocumento(id) == null) {
                    throw new NonexistentEntityException("The documento with id " + id + " no longer exists.");
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
            Documento documento;
            try {
                documento = em.getReference(Documento.class, id);
                documento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DocumentoAluguel> documentoAluguelListOrphanCheck = documento.getDocumentoAluguelList();
            for (DocumentoAluguel documentoAluguelListOrphanCheckDocumentoAluguel : documentoAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documento (" + documento + ") cannot be destroyed since the DocumentoAluguel " + documentoAluguelListOrphanCheckDocumentoAluguel + " in its documentoAluguelList field has a non-nullable documento field.");
            }
            List<DocumentoPessoa> documentoPessoaListOrphanCheck = documento.getDocumentoPessoaList();
            for (DocumentoPessoa documentoPessoaListOrphanCheckDocumentoPessoa : documentoPessoaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documento (" + documento + ") cannot be destroyed since the DocumentoPessoa " + documentoPessoaListOrphanCheckDocumentoPessoa + " in its documentoPessoaList field has a non-nullable documento field.");
            }
            List<DocumentoImovel> documentoImovelListOrphanCheck = documento.getDocumentoImovelList();
            for (DocumentoImovel documentoImovelListOrphanCheckDocumentoImovel : documentoImovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documento (" + documento + ") cannot be destroyed since the DocumentoImovel " + documentoImovelListOrphanCheckDocumentoImovel + " in its documentoImovelList field has a non-nullable documento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(documento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documento> findDocumentoEntities() {
        return findDocumentoEntities(true, -1, -1);
    }
    public List<Documento> findDocumentoEntities(int tipo) {
        Query q = getEntityManager().createNamedQuery("Documento.findByTipo");
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }

    public List<Documento> findDocumentoEntities(int maxResults, int firstResult) {
        return findDocumentoEntities(false, maxResults, firstResult);
    }

    private List<Documento> findDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documento.class));
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

    public Documento findDocumento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from(Documento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
