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
import beans.Aluguel;
import beans.DocumentoAluguel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class DocumentoAluguelJpaController implements Serializable {

    public DocumentoAluguelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocumentoAluguel documentoAluguel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento documento = documentoAluguel.getDocumento();
            if (documento != null) {
                documento = em.getReference(documento.getClass(), documento.getId());
                documentoAluguel.setDocumento(documento);
            }
            Aluguel aluguel = documentoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel = em.getReference(aluguel.getClass(), aluguel.getId());
                documentoAluguel.setAluguel(aluguel);
            }
            em.persist(documentoAluguel);
            if (documento != null) {
                documento.getDocumentoAluguelList().add(documentoAluguel);
                documento = em.merge(documento);
            }
            if (aluguel != null) {
                aluguel.getDocumentoAluguelList().add(documentoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocumentoAluguel documentoAluguel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentoAluguel persistentDocumentoAluguel = em.find(DocumentoAluguel.class, documentoAluguel.getId());
            Documento documentoOld = persistentDocumentoAluguel.getDocumento();
            Documento documentoNew = documentoAluguel.getDocumento();
            Aluguel aluguelOld = persistentDocumentoAluguel.getAluguel();
            Aluguel aluguelNew = documentoAluguel.getAluguel();
            if (documentoNew != null) {
                documentoNew = em.getReference(documentoNew.getClass(), documentoNew.getId());
                documentoAluguel.setDocumento(documentoNew);
            }
            if (aluguelNew != null) {
                aluguelNew = em.getReference(aluguelNew.getClass(), aluguelNew.getId());
                documentoAluguel.setAluguel(aluguelNew);
            }
            documentoAluguel = em.merge(documentoAluguel);
            if (documentoOld != null && !documentoOld.equals(documentoNew)) {
                documentoOld.getDocumentoAluguelList().remove(documentoAluguel);
                documentoOld = em.merge(documentoOld);
            }
            if (documentoNew != null && !documentoNew.equals(documentoOld)) {
                documentoNew.getDocumentoAluguelList().add(documentoAluguel);
                documentoNew = em.merge(documentoNew);
            }
            if (aluguelOld != null && !aluguelOld.equals(aluguelNew)) {
                aluguelOld.getDocumentoAluguelList().remove(documentoAluguel);
                aluguelOld = em.merge(aluguelOld);
            }
            if (aluguelNew != null && !aluguelNew.equals(aluguelOld)) {
                aluguelNew.getDocumentoAluguelList().add(documentoAluguel);
                aluguelNew = em.merge(aluguelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentoAluguel.getId();
                if (findDocumentoAluguel(id) == null) {
                    throw new NonexistentEntityException("The documentoAluguel with id " + id + " no longer exists.");
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
            DocumentoAluguel documentoAluguel;
            try {
                documentoAluguel = em.getReference(DocumentoAluguel.class, id);
                documentoAluguel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentoAluguel with id " + id + " no longer exists.", enfe);
            }
            Documento documento = documentoAluguel.getDocumento();
            if (documento != null) {
                documento.getDocumentoAluguelList().remove(documentoAluguel);
                documento = em.merge(documento);
            }
            Aluguel aluguel = documentoAluguel.getAluguel();
            if (aluguel != null) {
                aluguel.getDocumentoAluguelList().remove(documentoAluguel);
                aluguel = em.merge(aluguel);
            }
            em.remove(documentoAluguel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentoAluguel> findDocumentoAluguelEntities() {
        return findDocumentoAluguelEntities(true, -1, -1);
    }

    public List<DocumentoAluguel> findDocumentoAluguelEntities(int maxResults, int firstResult) {
        return findDocumentoAluguelEntities(false, maxResults, firstResult);
    }

    private List<DocumentoAluguel> findDocumentoAluguelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentoAluguel.class));
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

    public DocumentoAluguel findDocumentoAluguel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentoAluguel.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoAluguelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentoAluguel> rt = cq.from(DocumentoAluguel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
