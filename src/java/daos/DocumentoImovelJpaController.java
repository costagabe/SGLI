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
import beans.DocumentoImovel;
import beans.Imovel;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class DocumentoImovelJpaController implements Serializable {

    public DocumentoImovelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocumentoImovel documentoImovel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento documento = documentoImovel.getDocumento();
            if (documento != null) {
                documento = em.getReference(documento.getClass(), documento.getId());
                documentoImovel.setDocumento(documento);
            }
            Imovel imovel = documentoImovel.getImovel();
            if (imovel != null) {
                imovel = em.getReference(imovel.getClass(), imovel.getId());
                documentoImovel.setImovel(imovel);
            }
            em.persist(documentoImovel);
            if (documento != null) {
                documento.getDocumentoImovelList().add(documentoImovel);
                documento = em.merge(documento);
            }
            if (imovel != null) {
                imovel.getDocumentoImovelList().add(documentoImovel);
                imovel = em.merge(imovel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocumentoImovel documentoImovel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentoImovel persistentDocumentoImovel = em.find(DocumentoImovel.class, documentoImovel.getId());
            Documento documentoOld = persistentDocumentoImovel.getDocumento();
            Documento documentoNew = documentoImovel.getDocumento();
            Imovel imovelOld = persistentDocumentoImovel.getImovel();
            Imovel imovelNew = documentoImovel.getImovel();
            if (documentoNew != null) {
                documentoNew = em.getReference(documentoNew.getClass(), documentoNew.getId());
                documentoImovel.setDocumento(documentoNew);
            }
            if (imovelNew != null) {
                imovelNew = em.getReference(imovelNew.getClass(), imovelNew.getId());
                documentoImovel.setImovel(imovelNew);
            }
            documentoImovel = em.merge(documentoImovel);
            if (documentoOld != null && !documentoOld.equals(documentoNew)) {
                documentoOld.getDocumentoImovelList().remove(documentoImovel);
                documentoOld = em.merge(documentoOld);
            }
            if (documentoNew != null && !documentoNew.equals(documentoOld)) {
                documentoNew.getDocumentoImovelList().add(documentoImovel);
                documentoNew = em.merge(documentoNew);
            }
            if (imovelOld != null && !imovelOld.equals(imovelNew)) {
                imovelOld.getDocumentoImovelList().remove(documentoImovel);
                imovelOld = em.merge(imovelOld);
            }
            if (imovelNew != null && !imovelNew.equals(imovelOld)) {
                imovelNew.getDocumentoImovelList().add(documentoImovel);
                imovelNew = em.merge(imovelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentoImovel.getId();
                if (findDocumentoImovel(id) == null) {
                    throw new NonexistentEntityException("The documentoImovel with id " + id + " no longer exists.");
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
            DocumentoImovel documentoImovel;
            try {
                documentoImovel = em.getReference(DocumentoImovel.class, id);
                documentoImovel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentoImovel with id " + id + " no longer exists.", enfe);
            }
            Documento documento = documentoImovel.getDocumento();
            if (documento != null) {
                documento.getDocumentoImovelList().remove(documentoImovel);
                documento = em.merge(documento);
            }
            Imovel imovel = documentoImovel.getImovel();
            if (imovel != null) {
                imovel.getDocumentoImovelList().remove(documentoImovel);
                imovel = em.merge(imovel);
            }
            em.remove(documentoImovel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentoImovel> findDocumentoImovelEntities() {
        return findDocumentoImovelEntities(true, -1, -1);
    }

    public List<DocumentoImovel> findDocumentoImovelEntities(int maxResults, int firstResult) {
        return findDocumentoImovelEntities(false, maxResults, firstResult);
    }

    private List<DocumentoImovel> findDocumentoImovelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentoImovel.class));
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

    public DocumentoImovel findDocumentoImovel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentoImovel.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoImovelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentoImovel> rt = cq.from(DocumentoImovel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
