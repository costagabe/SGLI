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
import beans.Documento;
import beans.DocumentoPessoa;
import beans.Pessoa;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class DocumentoPessoaJpaController implements Serializable {

    public DocumentoPessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocumentoPessoa documentoPessoa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento documento = documentoPessoa.getDocumento();
            if (documento != null) {
                documento = em.getReference(documento.getClass(), documento.getId());
                documentoPessoa.setDocumento(documento);
            }
            Pessoa pessoa = documentoPessoa.getPessoa();
            if (pessoa != null) {
                pessoa = em.getReference(pessoa.getClass(), pessoa.getId());
                documentoPessoa.setPessoa(pessoa);
            }
            em.persist(documentoPessoa);
            if (documento != null) {
                documento.getDocumentoPessoaList().add(documentoPessoa);
                documento = em.merge(documento);
            }
            if (pessoa != null) {
                pessoa.getDocumentoPessoaList().add(documentoPessoa);
                pessoa = em.merge(pessoa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocumentoPessoa documentoPessoa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentoPessoa persistentDocumentoPessoa = em.find(DocumentoPessoa.class, documentoPessoa.getId());
            Documento documentoOld = persistentDocumentoPessoa.getDocumento();
            Documento documentoNew = documentoPessoa.getDocumento();
            Pessoa pessoaOld = persistentDocumentoPessoa.getPessoa();
            Pessoa pessoaNew = documentoPessoa.getPessoa();
            if (documentoNew != null) {
                documentoNew = em.getReference(documentoNew.getClass(), documentoNew.getId());
                documentoPessoa.setDocumento(documentoNew);
            }
            if (pessoaNew != null) {
                pessoaNew = em.getReference(pessoaNew.getClass(), pessoaNew.getId());
                documentoPessoa.setPessoa(pessoaNew);
            }
            documentoPessoa = em.merge(documentoPessoa);
            if (documentoOld != null && !documentoOld.equals(documentoNew)) {
                documentoOld.getDocumentoPessoaList().remove(documentoPessoa);
                documentoOld = em.merge(documentoOld);
            }
            if (documentoNew != null && !documentoNew.equals(documentoOld)) {
                documentoNew.getDocumentoPessoaList().add(documentoPessoa);
                documentoNew = em.merge(documentoNew);
            }
            if (pessoaOld != null && !pessoaOld.equals(pessoaNew)) {
                pessoaOld.getDocumentoPessoaList().remove(documentoPessoa);
                pessoaOld = em.merge(pessoaOld);
            }
            if (pessoaNew != null && !pessoaNew.equals(pessoaOld)) {
                pessoaNew.getDocumentoPessoaList().add(documentoPessoa);
                pessoaNew = em.merge(pessoaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentoPessoa.getId();
                if (findDocumentoPessoa(id) == null) {
                    throw new NonexistentEntityException("The documentoPessoa with id " + id + " no longer exists.");
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
            DocumentoPessoa documentoPessoa;
            try {
                documentoPessoa = em.getReference(DocumentoPessoa.class, id);
                documentoPessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentoPessoa with id " + id + " no longer exists.", enfe);
            }
            Documento documento = documentoPessoa.getDocumento();
            if (documento != null) {
                documento.getDocumentoPessoaList().remove(documentoPessoa);
                documento = em.merge(documento);
            }
            Pessoa pessoa = documentoPessoa.getPessoa();
            if (pessoa != null) {
                pessoa.getDocumentoPessoaList().remove(documentoPessoa);
                pessoa = em.merge(pessoa);
            }
            em.remove(documentoPessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentoPessoa> findDocumentoPessoaEntities() {
        return findDocumentoPessoaEntities(true, -1, -1);
    }

    public List<DocumentoPessoa> findDocumentoPessoaEntities(int maxResults, int firstResult) {
        return findDocumentoPessoaEntities(false, maxResults, firstResult);
    }

    private List<DocumentoPessoa> findDocumentoPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentoPessoa.class));
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

    public DocumentoPessoa findDocumentoPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentoPessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentoPessoa> rt = cq.from(DocumentoPessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
