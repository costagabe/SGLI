/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Aluguel;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Imovel;
import beans.DocumentoAluguel;
import java.util.ArrayList;
import java.util.List;
import beans.ObservacaoAluguel;
import beans.LocatarioAluguel;
import beans.PagamentoAluguel;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class AluguelJpaController implements Serializable {

    public AluguelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aluguel aluguel) {
        if (aluguel.getDocumentoAluguelList() == null) {
            aluguel.setDocumentoAluguelList(new ArrayList<DocumentoAluguel>());
        }
        if (aluguel.getObservacaoAluguelList() == null) {
            aluguel.setObservacaoAluguelList(new ArrayList<ObservacaoAluguel>());
        }
        if (aluguel.getLocatarioAluguelList() == null) {
            aluguel.setLocatarioAluguelList(new ArrayList<LocatarioAluguel>());
        }
        if (aluguel.getPagamentoAluguelList() == null) {
            aluguel.setPagamentoAluguelList(new ArrayList<PagamentoAluguel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imovel imovel = aluguel.getImovel();
            if (imovel != null) {
                imovel = em.getReference(imovel.getClass(), imovel.getId());
                aluguel.setImovel(imovel);
            }
            List<DocumentoAluguel> attachedDocumentoAluguelList = new ArrayList<DocumentoAluguel>();
            for (DocumentoAluguel documentoAluguelListDocumentoAluguelToAttach : aluguel.getDocumentoAluguelList()) {
                documentoAluguelListDocumentoAluguelToAttach = em.getReference(documentoAluguelListDocumentoAluguelToAttach.getClass(), documentoAluguelListDocumentoAluguelToAttach.getId());
                attachedDocumentoAluguelList.add(documentoAluguelListDocumentoAluguelToAttach);
            }
            aluguel.setDocumentoAluguelList(attachedDocumentoAluguelList);
            List<ObservacaoAluguel> attachedObservacaoAluguelList = new ArrayList<ObservacaoAluguel>();
            for (ObservacaoAluguel observacaoAluguelListObservacaoAluguelToAttach : aluguel.getObservacaoAluguelList()) {
                observacaoAluguelListObservacaoAluguelToAttach = em.getReference(observacaoAluguelListObservacaoAluguelToAttach.getClass(), observacaoAluguelListObservacaoAluguelToAttach.getId());
                attachedObservacaoAluguelList.add(observacaoAluguelListObservacaoAluguelToAttach);
            }
            aluguel.setObservacaoAluguelList(attachedObservacaoAluguelList);
            List<LocatarioAluguel> attachedLocatarioAluguelList = new ArrayList<LocatarioAluguel>();
            for (LocatarioAluguel locatarioAluguelListLocatarioAluguelToAttach : aluguel.getLocatarioAluguelList()) {
                locatarioAluguelListLocatarioAluguelToAttach = em.getReference(locatarioAluguelListLocatarioAluguelToAttach.getClass(), locatarioAluguelListLocatarioAluguelToAttach.getId());
                attachedLocatarioAluguelList.add(locatarioAluguelListLocatarioAluguelToAttach);
            }
            aluguel.setLocatarioAluguelList(attachedLocatarioAluguelList);
            List<PagamentoAluguel> attachedPagamentoAluguelList = new ArrayList<PagamentoAluguel>();
            for (PagamentoAluguel pagamentoAluguelListPagamentoAluguelToAttach : aluguel.getPagamentoAluguelList()) {
                pagamentoAluguelListPagamentoAluguelToAttach = em.getReference(pagamentoAluguelListPagamentoAluguelToAttach.getClass(), pagamentoAluguelListPagamentoAluguelToAttach.getId());
                attachedPagamentoAluguelList.add(pagamentoAluguelListPagamentoAluguelToAttach);
            }
            aluguel.setPagamentoAluguelList(attachedPagamentoAluguelList);
            em.persist(aluguel);
            if (imovel != null) {
                imovel.getAluguelList().add(aluguel);
                imovel = em.merge(imovel);
            }
            for (DocumentoAluguel documentoAluguelListDocumentoAluguel : aluguel.getDocumentoAluguelList()) {
                Aluguel oldAluguelOfDocumentoAluguelListDocumentoAluguel = documentoAluguelListDocumentoAluguel.getAluguel();
                documentoAluguelListDocumentoAluguel.setAluguel(aluguel);
                documentoAluguelListDocumentoAluguel = em.merge(documentoAluguelListDocumentoAluguel);
                if (oldAluguelOfDocumentoAluguelListDocumentoAluguel != null) {
                    oldAluguelOfDocumentoAluguelListDocumentoAluguel.getDocumentoAluguelList().remove(documentoAluguelListDocumentoAluguel);
                    oldAluguelOfDocumentoAluguelListDocumentoAluguel = em.merge(oldAluguelOfDocumentoAluguelListDocumentoAluguel);
                }
            }
            for (ObservacaoAluguel observacaoAluguelListObservacaoAluguel : aluguel.getObservacaoAluguelList()) {
                Aluguel oldAluguelOfObservacaoAluguelListObservacaoAluguel = observacaoAluguelListObservacaoAluguel.getAluguel();
                observacaoAluguelListObservacaoAluguel.setAluguel(aluguel);
                observacaoAluguelListObservacaoAluguel = em.merge(observacaoAluguelListObservacaoAluguel);
                if (oldAluguelOfObservacaoAluguelListObservacaoAluguel != null) {
                    oldAluguelOfObservacaoAluguelListObservacaoAluguel.getObservacaoAluguelList().remove(observacaoAluguelListObservacaoAluguel);
                    oldAluguelOfObservacaoAluguelListObservacaoAluguel = em.merge(oldAluguelOfObservacaoAluguelListObservacaoAluguel);
                }
            }
            for (LocatarioAluguel locatarioAluguelListLocatarioAluguel : aluguel.getLocatarioAluguelList()) {
                Aluguel oldAluguelOfLocatarioAluguelListLocatarioAluguel = locatarioAluguelListLocatarioAluguel.getAluguel();
                locatarioAluguelListLocatarioAluguel.setAluguel(aluguel);
                locatarioAluguelListLocatarioAluguel = em.merge(locatarioAluguelListLocatarioAluguel);
                if (oldAluguelOfLocatarioAluguelListLocatarioAluguel != null) {
                    oldAluguelOfLocatarioAluguelListLocatarioAluguel.getLocatarioAluguelList().remove(locatarioAluguelListLocatarioAluguel);
                    oldAluguelOfLocatarioAluguelListLocatarioAluguel = em.merge(oldAluguelOfLocatarioAluguelListLocatarioAluguel);
                }
            }
            for (PagamentoAluguel pagamentoAluguelListPagamentoAluguel : aluguel.getPagamentoAluguelList()) {
                Aluguel oldAluguelOfPagamentoAluguelListPagamentoAluguel = pagamentoAluguelListPagamentoAluguel.getAluguel();
                pagamentoAluguelListPagamentoAluguel.setAluguel(aluguel);
                pagamentoAluguelListPagamentoAluguel = em.merge(pagamentoAluguelListPagamentoAluguel);
                if (oldAluguelOfPagamentoAluguelListPagamentoAluguel != null) {
                    oldAluguelOfPagamentoAluguelListPagamentoAluguel.getPagamentoAluguelList().remove(pagamentoAluguelListPagamentoAluguel);
                    oldAluguelOfPagamentoAluguelListPagamentoAluguel = em.merge(oldAluguelOfPagamentoAluguelListPagamentoAluguel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aluguel aluguel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aluguel persistentAluguel = em.find(Aluguel.class, aluguel.getId());
            Imovel imovelOld = persistentAluguel.getImovel();
            Imovel imovelNew = aluguel.getImovel();
            List<DocumentoAluguel> documentoAluguelListOld = persistentAluguel.getDocumentoAluguelList();
            List<DocumentoAluguel> documentoAluguelListNew = aluguel.getDocumentoAluguelList();
            List<ObservacaoAluguel> observacaoAluguelListOld = persistentAluguel.getObservacaoAluguelList();
            List<ObservacaoAluguel> observacaoAluguelListNew = aluguel.getObservacaoAluguelList();
            List<LocatarioAluguel> locatarioAluguelListOld = persistentAluguel.getLocatarioAluguelList();
            List<LocatarioAluguel> locatarioAluguelListNew = aluguel.getLocatarioAluguelList();
            List<PagamentoAluguel> pagamentoAluguelListOld = persistentAluguel.getPagamentoAluguelList();
            List<PagamentoAluguel> pagamentoAluguelListNew = aluguel.getPagamentoAluguelList();
            List<String> illegalOrphanMessages = null;
            for (DocumentoAluguel documentoAluguelListOldDocumentoAluguel : documentoAluguelListOld) {
                if (!documentoAluguelListNew.contains(documentoAluguelListOldDocumentoAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoAluguel " + documentoAluguelListOldDocumentoAluguel + " since its aluguel field is not nullable.");
                }
            }
            for (ObservacaoAluguel observacaoAluguelListOldObservacaoAluguel : observacaoAluguelListOld) {
                if (!observacaoAluguelListNew.contains(observacaoAluguelListOldObservacaoAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ObservacaoAluguel " + observacaoAluguelListOldObservacaoAluguel + " since its aluguel field is not nullable.");
                }
            }
            for (LocatarioAluguel locatarioAluguelListOldLocatarioAluguel : locatarioAluguelListOld) {
                if (!locatarioAluguelListNew.contains(locatarioAluguelListOldLocatarioAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LocatarioAluguel " + locatarioAluguelListOldLocatarioAluguel + " since its aluguel field is not nullable.");
                }
            }
            for (PagamentoAluguel pagamentoAluguelListOldPagamentoAluguel : pagamentoAluguelListOld) {
                if (!pagamentoAluguelListNew.contains(pagamentoAluguelListOldPagamentoAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PagamentoAluguel " + pagamentoAluguelListOldPagamentoAluguel + " since its aluguel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (imovelNew != null) {
                imovelNew = em.getReference(imovelNew.getClass(), imovelNew.getId());
                aluguel.setImovel(imovelNew);
            }
            List<DocumentoAluguel> attachedDocumentoAluguelListNew = new ArrayList<DocumentoAluguel>();
            for (DocumentoAluguel documentoAluguelListNewDocumentoAluguelToAttach : documentoAluguelListNew) {
                documentoAluguelListNewDocumentoAluguelToAttach = em.getReference(documentoAluguelListNewDocumentoAluguelToAttach.getClass(), documentoAluguelListNewDocumentoAluguelToAttach.getId());
                attachedDocumentoAluguelListNew.add(documentoAluguelListNewDocumentoAluguelToAttach);
            }
            documentoAluguelListNew = attachedDocumentoAluguelListNew;
            aluguel.setDocumentoAluguelList(documentoAluguelListNew);
            List<ObservacaoAluguel> attachedObservacaoAluguelListNew = new ArrayList<ObservacaoAluguel>();
            for (ObservacaoAluguel observacaoAluguelListNewObservacaoAluguelToAttach : observacaoAluguelListNew) {
                observacaoAluguelListNewObservacaoAluguelToAttach = em.getReference(observacaoAluguelListNewObservacaoAluguelToAttach.getClass(), observacaoAluguelListNewObservacaoAluguelToAttach.getId());
                attachedObservacaoAluguelListNew.add(observacaoAluguelListNewObservacaoAluguelToAttach);
            }
            observacaoAluguelListNew = attachedObservacaoAluguelListNew;
            aluguel.setObservacaoAluguelList(observacaoAluguelListNew);
            List<LocatarioAluguel> attachedLocatarioAluguelListNew = new ArrayList<LocatarioAluguel>();
            for (LocatarioAluguel locatarioAluguelListNewLocatarioAluguelToAttach : locatarioAluguelListNew) {
                locatarioAluguelListNewLocatarioAluguelToAttach = em.getReference(locatarioAluguelListNewLocatarioAluguelToAttach.getClass(), locatarioAluguelListNewLocatarioAluguelToAttach.getId());
                attachedLocatarioAluguelListNew.add(locatarioAluguelListNewLocatarioAluguelToAttach);
            }
            locatarioAluguelListNew = attachedLocatarioAluguelListNew;
            aluguel.setLocatarioAluguelList(locatarioAluguelListNew);
            List<PagamentoAluguel> attachedPagamentoAluguelListNew = new ArrayList<PagamentoAluguel>();
            for (PagamentoAluguel pagamentoAluguelListNewPagamentoAluguelToAttach : pagamentoAluguelListNew) {
                pagamentoAluguelListNewPagamentoAluguelToAttach = em.getReference(pagamentoAluguelListNewPagamentoAluguelToAttach.getClass(), pagamentoAluguelListNewPagamentoAluguelToAttach.getId());
                attachedPagamentoAluguelListNew.add(pagamentoAluguelListNewPagamentoAluguelToAttach);
            }
            pagamentoAluguelListNew = attachedPagamentoAluguelListNew;
            aluguel.setPagamentoAluguelList(pagamentoAluguelListNew);
            aluguel = em.merge(aluguel);
            if (imovelOld != null && !imovelOld.equals(imovelNew)) {
                imovelOld.getAluguelList().remove(aluguel);
                imovelOld = em.merge(imovelOld);
            }
            if (imovelNew != null && !imovelNew.equals(imovelOld)) {
                imovelNew.getAluguelList().add(aluguel);
                imovelNew = em.merge(imovelNew);
            }
            for (DocumentoAluguel documentoAluguelListNewDocumentoAluguel : documentoAluguelListNew) {
                if (!documentoAluguelListOld.contains(documentoAluguelListNewDocumentoAluguel)) {
                    Aluguel oldAluguelOfDocumentoAluguelListNewDocumentoAluguel = documentoAluguelListNewDocumentoAluguel.getAluguel();
                    documentoAluguelListNewDocumentoAluguel.setAluguel(aluguel);
                    documentoAluguelListNewDocumentoAluguel = em.merge(documentoAluguelListNewDocumentoAluguel);
                    if (oldAluguelOfDocumentoAluguelListNewDocumentoAluguel != null && !oldAluguelOfDocumentoAluguelListNewDocumentoAluguel.equals(aluguel)) {
                        oldAluguelOfDocumentoAluguelListNewDocumentoAluguel.getDocumentoAluguelList().remove(documentoAluguelListNewDocumentoAluguel);
                        oldAluguelOfDocumentoAluguelListNewDocumentoAluguel = em.merge(oldAluguelOfDocumentoAluguelListNewDocumentoAluguel);
                    }
                }
            }
            for (ObservacaoAluguel observacaoAluguelListNewObservacaoAluguel : observacaoAluguelListNew) {
                if (!observacaoAluguelListOld.contains(observacaoAluguelListNewObservacaoAluguel)) {
                    Aluguel oldAluguelOfObservacaoAluguelListNewObservacaoAluguel = observacaoAluguelListNewObservacaoAluguel.getAluguel();
                    observacaoAluguelListNewObservacaoAluguel.setAluguel(aluguel);
                    observacaoAluguelListNewObservacaoAluguel = em.merge(observacaoAluguelListNewObservacaoAluguel);
                    if (oldAluguelOfObservacaoAluguelListNewObservacaoAluguel != null && !oldAluguelOfObservacaoAluguelListNewObservacaoAluguel.equals(aluguel)) {
                        oldAluguelOfObservacaoAluguelListNewObservacaoAluguel.getObservacaoAluguelList().remove(observacaoAluguelListNewObservacaoAluguel);
                        oldAluguelOfObservacaoAluguelListNewObservacaoAluguel = em.merge(oldAluguelOfObservacaoAluguelListNewObservacaoAluguel);
                    }
                }
            }
            for (LocatarioAluguel locatarioAluguelListNewLocatarioAluguel : locatarioAluguelListNew) {
                if (!locatarioAluguelListOld.contains(locatarioAluguelListNewLocatarioAluguel)) {
                    Aluguel oldAluguelOfLocatarioAluguelListNewLocatarioAluguel = locatarioAluguelListNewLocatarioAluguel.getAluguel();
                    locatarioAluguelListNewLocatarioAluguel.setAluguel(aluguel);
                    locatarioAluguelListNewLocatarioAluguel = em.merge(locatarioAluguelListNewLocatarioAluguel);
                    if (oldAluguelOfLocatarioAluguelListNewLocatarioAluguel != null && !oldAluguelOfLocatarioAluguelListNewLocatarioAluguel.equals(aluguel)) {
                        oldAluguelOfLocatarioAluguelListNewLocatarioAluguel.getLocatarioAluguelList().remove(locatarioAluguelListNewLocatarioAluguel);
                        oldAluguelOfLocatarioAluguelListNewLocatarioAluguel = em.merge(oldAluguelOfLocatarioAluguelListNewLocatarioAluguel);
                    }
                }
            }
            for (PagamentoAluguel pagamentoAluguelListNewPagamentoAluguel : pagamentoAluguelListNew) {
                if (!pagamentoAluguelListOld.contains(pagamentoAluguelListNewPagamentoAluguel)) {
                    Aluguel oldAluguelOfPagamentoAluguelListNewPagamentoAluguel = pagamentoAluguelListNewPagamentoAluguel.getAluguel();
                    pagamentoAluguelListNewPagamentoAluguel.setAluguel(aluguel);
                    pagamentoAluguelListNewPagamentoAluguel = em.merge(pagamentoAluguelListNewPagamentoAluguel);
                    if (oldAluguelOfPagamentoAluguelListNewPagamentoAluguel != null && !oldAluguelOfPagamentoAluguelListNewPagamentoAluguel.equals(aluguel)) {
                        oldAluguelOfPagamentoAluguelListNewPagamentoAluguel.getPagamentoAluguelList().remove(pagamentoAluguelListNewPagamentoAluguel);
                        oldAluguelOfPagamentoAluguelListNewPagamentoAluguel = em.merge(oldAluguelOfPagamentoAluguelListNewPagamentoAluguel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aluguel.getId();
                if (findAluguel(id) == null) {
                    throw new NonexistentEntityException("The aluguel with id " + id + " no longer exists.");
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
            Aluguel aluguel;
            try {
                aluguel = em.getReference(Aluguel.class, id);
                aluguel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aluguel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DocumentoAluguel> documentoAluguelListOrphanCheck = aluguel.getDocumentoAluguelList();
            for (DocumentoAluguel documentoAluguelListOrphanCheckDocumentoAluguel : documentoAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aluguel (" + aluguel + ") cannot be destroyed since the DocumentoAluguel " + documentoAluguelListOrphanCheckDocumentoAluguel + " in its documentoAluguelList field has a non-nullable aluguel field.");
            }
            List<ObservacaoAluguel> observacaoAluguelListOrphanCheck = aluguel.getObservacaoAluguelList();
            for (ObservacaoAluguel observacaoAluguelListOrphanCheckObservacaoAluguel : observacaoAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aluguel (" + aluguel + ") cannot be destroyed since the ObservacaoAluguel " + observacaoAluguelListOrphanCheckObservacaoAluguel + " in its observacaoAluguelList field has a non-nullable aluguel field.");
            }
            List<LocatarioAluguel> locatarioAluguelListOrphanCheck = aluguel.getLocatarioAluguelList();
            for (LocatarioAluguel locatarioAluguelListOrphanCheckLocatarioAluguel : locatarioAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aluguel (" + aluguel + ") cannot be destroyed since the LocatarioAluguel " + locatarioAluguelListOrphanCheckLocatarioAluguel + " in its locatarioAluguelList field has a non-nullable aluguel field.");
            }
            List<PagamentoAluguel> pagamentoAluguelListOrphanCheck = aluguel.getPagamentoAluguelList();
            for (PagamentoAluguel pagamentoAluguelListOrphanCheckPagamentoAluguel : pagamentoAluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aluguel (" + aluguel + ") cannot be destroyed since the PagamentoAluguel " + pagamentoAluguelListOrphanCheckPagamentoAluguel + " in its pagamentoAluguelList field has a non-nullable aluguel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Imovel imovel = aluguel.getImovel();
            if (imovel != null) {
                imovel.getAluguelList().remove(aluguel);
                imovel = em.merge(imovel);
            }
            em.remove(aluguel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aluguel> findAluguelEntities() {
        return findAluguelEntities(true, -1, -1);
    }

    public List<Aluguel> findAluguelEntities(int maxResults, int firstResult) {
        return findAluguelEntities(false, maxResults, firstResult);
    }

    private List<Aluguel> findAluguelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aluguel.class));
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

    public Aluguel findAluguel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aluguel.class, id);
        } finally {
            em.close();
        }
    }

    public int getAluguelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aluguel> rt = cq.from(Aluguel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
