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
import beans.FotoImovel;
import java.util.ArrayList;
import java.util.List;
import beans.LocatarioImovel;
import beans.Aluguel;
import beans.DocumentoImovel;
import beans.Imovel;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Gabriel Alves
 */
public class ImovelJpaController implements Serializable {

    public ImovelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    public void create(Imovel imovel) {
        if (imovel.getFotoImovelList() == null) {
            imovel.setFotoImovelList(new ArrayList<FotoImovel>());
        }
        if (imovel.getLocatarioImovelList() == null) {
            imovel.setLocatarioImovelList(new ArrayList<LocatarioImovel>());
        }
        if (imovel.getAluguelList() == null) {
            imovel.setAluguelList(new ArrayList<Aluguel>());
        }
        if (imovel.getDocumentoImovelList() == null) {
            imovel.setDocumentoImovelList(new ArrayList<DocumentoImovel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco endereco = imovel.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getId());
                imovel.setEndereco(endereco);
            }
            List<FotoImovel> attachedFotoImovelList = new ArrayList<FotoImovel>();
            for (FotoImovel fotoImovelListFotoImovelToAttach : imovel.getFotoImovelList()) {
                fotoImovelListFotoImovelToAttach = em.getReference(fotoImovelListFotoImovelToAttach.getClass(), fotoImovelListFotoImovelToAttach.getId());
                attachedFotoImovelList.add(fotoImovelListFotoImovelToAttach);
            }
            imovel.setFotoImovelList(attachedFotoImovelList);
            List<LocatarioImovel> attachedLocatarioImovelList = new ArrayList<LocatarioImovel>();
            for (LocatarioImovel locatarioImovelListLocatarioImovelToAttach : imovel.getLocatarioImovelList()) {
                locatarioImovelListLocatarioImovelToAttach = em.getReference(locatarioImovelListLocatarioImovelToAttach.getClass(), locatarioImovelListLocatarioImovelToAttach.getId());
                attachedLocatarioImovelList.add(locatarioImovelListLocatarioImovelToAttach);
            }
            imovel.setLocatarioImovelList(attachedLocatarioImovelList);
            List<Aluguel> attachedAluguelList = new ArrayList<Aluguel>();
            for (Aluguel aluguelListAluguelToAttach : imovel.getAluguelList()) {
                aluguelListAluguelToAttach = em.getReference(aluguelListAluguelToAttach.getClass(), aluguelListAluguelToAttach.getId());
                attachedAluguelList.add(aluguelListAluguelToAttach);
            }
            imovel.setAluguelList(attachedAluguelList);
            List<DocumentoImovel> attachedDocumentoImovelList = new ArrayList<DocumentoImovel>();
            for (DocumentoImovel documentoImovelListDocumentoImovelToAttach : imovel.getDocumentoImovelList()) {
                documentoImovelListDocumentoImovelToAttach = em.getReference(documentoImovelListDocumentoImovelToAttach.getClass(), documentoImovelListDocumentoImovelToAttach.getId());
                attachedDocumentoImovelList.add(documentoImovelListDocumentoImovelToAttach);
            }
            imovel.setDocumentoImovelList(attachedDocumentoImovelList);
            em.persist(imovel);
            if (endereco != null) {
                endereco.getImovelList().add(imovel);
                endereco = em.merge(endereco);
            }
            for (FotoImovel fotoImovelListFotoImovel : imovel.getFotoImovelList()) {
                Imovel oldImovelOfFotoImovelListFotoImovel = fotoImovelListFotoImovel.getImovel();
                fotoImovelListFotoImovel.setImovel(imovel);
                fotoImovelListFotoImovel = em.merge(fotoImovelListFotoImovel);
                if (oldImovelOfFotoImovelListFotoImovel != null) {
                    oldImovelOfFotoImovelListFotoImovel.getFotoImovelList().remove(fotoImovelListFotoImovel);
                    oldImovelOfFotoImovelListFotoImovel = em.merge(oldImovelOfFotoImovelListFotoImovel);
                }
            }
            for (LocatarioImovel locatarioImovelListLocatarioImovel : imovel.getLocatarioImovelList()) {
                Imovel oldImovelOfLocatarioImovelListLocatarioImovel = locatarioImovelListLocatarioImovel.getImovel();
                locatarioImovelListLocatarioImovel.setImovel(imovel);
                locatarioImovelListLocatarioImovel = em.merge(locatarioImovelListLocatarioImovel);
                if (oldImovelOfLocatarioImovelListLocatarioImovel != null) {
                    oldImovelOfLocatarioImovelListLocatarioImovel.getLocatarioImovelList().remove(locatarioImovelListLocatarioImovel);
                    oldImovelOfLocatarioImovelListLocatarioImovel = em.merge(oldImovelOfLocatarioImovelListLocatarioImovel);
                }
            }
            for (Aluguel aluguelListAluguel : imovel.getAluguelList()) {
                Imovel oldImovelOfAluguelListAluguel = aluguelListAluguel.getImovel();
                aluguelListAluguel.setImovel(imovel);
                aluguelListAluguel = em.merge(aluguelListAluguel);
                if (oldImovelOfAluguelListAluguel != null) {
                    oldImovelOfAluguelListAluguel.getAluguelList().remove(aluguelListAluguel);
                    oldImovelOfAluguelListAluguel = em.merge(oldImovelOfAluguelListAluguel);
                }
            }
            for (DocumentoImovel documentoImovelListDocumentoImovel : imovel.getDocumentoImovelList()) {
                Imovel oldImovelOfDocumentoImovelListDocumentoImovel = documentoImovelListDocumentoImovel.getImovel();
                documentoImovelListDocumentoImovel.setImovel(imovel);
                documentoImovelListDocumentoImovel = em.merge(documentoImovelListDocumentoImovel);
                if (oldImovelOfDocumentoImovelListDocumentoImovel != null) {
                    oldImovelOfDocumentoImovelListDocumentoImovel.getDocumentoImovelList().remove(documentoImovelListDocumentoImovel);
                    oldImovelOfDocumentoImovelListDocumentoImovel = em.merge(oldImovelOfDocumentoImovelListDocumentoImovel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Imovel imovel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imovel persistentImovel = em.find(Imovel.class, imovel.getId());
            Endereco enderecoOld = persistentImovel.getEndereco();
            Endereco enderecoNew = imovel.getEndereco();
            List<FotoImovel> fotoImovelListOld = persistentImovel.getFotoImovelList();
            List<FotoImovel> fotoImovelListNew = imovel.getFotoImovelList();
            List<LocatarioImovel> locatarioImovelListOld = persistentImovel.getLocatarioImovelList();
            List<LocatarioImovel> locatarioImovelListNew = imovel.getLocatarioImovelList();
            List<Aluguel> aluguelListOld = persistentImovel.getAluguelList();
            List<Aluguel> aluguelListNew = imovel.getAluguelList();
            List<DocumentoImovel> documentoImovelListOld = persistentImovel.getDocumentoImovelList();
            List<DocumentoImovel> documentoImovelListNew = imovel.getDocumentoImovelList();
            List<String> illegalOrphanMessages = null;
            for (FotoImovel fotoImovelListOldFotoImovel : fotoImovelListOld) {
                if (!fotoImovelListNew.contains(fotoImovelListOldFotoImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FotoImovel " + fotoImovelListOldFotoImovel + " since its imovel field is not nullable.");
                }
            }
            for (LocatarioImovel locatarioImovelListOldLocatarioImovel : locatarioImovelListOld) {
                if (!locatarioImovelListNew.contains(locatarioImovelListOldLocatarioImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LocatarioImovel " + locatarioImovelListOldLocatarioImovel + " since its imovel field is not nullable.");
                }
            }
            for (Aluguel aluguelListOldAluguel : aluguelListOld) {
                if (!aluguelListNew.contains(aluguelListOldAluguel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aluguel " + aluguelListOldAluguel + " since its imovel field is not nullable.");
                }
            }
            for (DocumentoImovel documentoImovelListOldDocumentoImovel : documentoImovelListOld) {
                if (!documentoImovelListNew.contains(documentoImovelListOldDocumentoImovel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentoImovel " + documentoImovelListOldDocumentoImovel + " since its imovel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getId());
                imovel.setEndereco(enderecoNew);
            }
            List<FotoImovel> attachedFotoImovelListNew = new ArrayList<FotoImovel>();
            for (FotoImovel fotoImovelListNewFotoImovelToAttach : fotoImovelListNew) {
                fotoImovelListNewFotoImovelToAttach = em.getReference(fotoImovelListNewFotoImovelToAttach.getClass(), fotoImovelListNewFotoImovelToAttach.getId());
                attachedFotoImovelListNew.add(fotoImovelListNewFotoImovelToAttach);
            }
            fotoImovelListNew = attachedFotoImovelListNew;
            imovel.setFotoImovelList(fotoImovelListNew);
            List<LocatarioImovel> attachedLocatarioImovelListNew = new ArrayList<LocatarioImovel>();
            for (LocatarioImovel locatarioImovelListNewLocatarioImovelToAttach : locatarioImovelListNew) {
                locatarioImovelListNewLocatarioImovelToAttach = em.getReference(locatarioImovelListNewLocatarioImovelToAttach.getClass(), locatarioImovelListNewLocatarioImovelToAttach.getId());
                attachedLocatarioImovelListNew.add(locatarioImovelListNewLocatarioImovelToAttach);
            }
            locatarioImovelListNew = attachedLocatarioImovelListNew;
            imovel.setLocatarioImovelList(locatarioImovelListNew);
            List<Aluguel> attachedAluguelListNew = new ArrayList<Aluguel>();
            for (Aluguel aluguelListNewAluguelToAttach : aluguelListNew) {
                aluguelListNewAluguelToAttach = em.getReference(aluguelListNewAluguelToAttach.getClass(), aluguelListNewAluguelToAttach.getId());
                attachedAluguelListNew.add(aluguelListNewAluguelToAttach);
            }
            aluguelListNew = attachedAluguelListNew;
            imovel.setAluguelList(aluguelListNew);
            List<DocumentoImovel> attachedDocumentoImovelListNew = new ArrayList<DocumentoImovel>();
            for (DocumentoImovel documentoImovelListNewDocumentoImovelToAttach : documentoImovelListNew) {
                documentoImovelListNewDocumentoImovelToAttach = em.getReference(documentoImovelListNewDocumentoImovelToAttach.getClass(), documentoImovelListNewDocumentoImovelToAttach.getId());
                attachedDocumentoImovelListNew.add(documentoImovelListNewDocumentoImovelToAttach);
            }
            documentoImovelListNew = attachedDocumentoImovelListNew;
            imovel.setDocumentoImovelList(documentoImovelListNew);
            imovel = em.merge(imovel);
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getImovelList().remove(imovel);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getImovelList().add(imovel);
                enderecoNew = em.merge(enderecoNew);
            }
            for (FotoImovel fotoImovelListNewFotoImovel : fotoImovelListNew) {
                if (!fotoImovelListOld.contains(fotoImovelListNewFotoImovel)) {
                    Imovel oldImovelOfFotoImovelListNewFotoImovel = fotoImovelListNewFotoImovel.getImovel();
                    fotoImovelListNewFotoImovel.setImovel(imovel);
                    fotoImovelListNewFotoImovel = em.merge(fotoImovelListNewFotoImovel);
                    if (oldImovelOfFotoImovelListNewFotoImovel != null && !oldImovelOfFotoImovelListNewFotoImovel.equals(imovel)) {
                        oldImovelOfFotoImovelListNewFotoImovel.getFotoImovelList().remove(fotoImovelListNewFotoImovel);
                        oldImovelOfFotoImovelListNewFotoImovel = em.merge(oldImovelOfFotoImovelListNewFotoImovel);
                    }
                }
            }
            for (LocatarioImovel locatarioImovelListNewLocatarioImovel : locatarioImovelListNew) {
                if (!locatarioImovelListOld.contains(locatarioImovelListNewLocatarioImovel)) {
                    Imovel oldImovelOfLocatarioImovelListNewLocatarioImovel = locatarioImovelListNewLocatarioImovel.getImovel();
                    locatarioImovelListNewLocatarioImovel.setImovel(imovel);
                    locatarioImovelListNewLocatarioImovel = em.merge(locatarioImovelListNewLocatarioImovel);
                    if (oldImovelOfLocatarioImovelListNewLocatarioImovel != null && !oldImovelOfLocatarioImovelListNewLocatarioImovel.equals(imovel)) {
                        oldImovelOfLocatarioImovelListNewLocatarioImovel.getLocatarioImovelList().remove(locatarioImovelListNewLocatarioImovel);
                        oldImovelOfLocatarioImovelListNewLocatarioImovel = em.merge(oldImovelOfLocatarioImovelListNewLocatarioImovel);
                    }
                }
            }
            for (Aluguel aluguelListNewAluguel : aluguelListNew) {
                if (!aluguelListOld.contains(aluguelListNewAluguel)) {
                    Imovel oldImovelOfAluguelListNewAluguel = aluguelListNewAluguel.getImovel();
                    aluguelListNewAluguel.setImovel(imovel);
                    aluguelListNewAluguel = em.merge(aluguelListNewAluguel);
                    if (oldImovelOfAluguelListNewAluguel != null && !oldImovelOfAluguelListNewAluguel.equals(imovel)) {
                        oldImovelOfAluguelListNewAluguel.getAluguelList().remove(aluguelListNewAluguel);
                        oldImovelOfAluguelListNewAluguel = em.merge(oldImovelOfAluguelListNewAluguel);
                    }
                }
            }
            for (DocumentoImovel documentoImovelListNewDocumentoImovel : documentoImovelListNew) {
                if (!documentoImovelListOld.contains(documentoImovelListNewDocumentoImovel)) {
                    Imovel oldImovelOfDocumentoImovelListNewDocumentoImovel = documentoImovelListNewDocumentoImovel.getImovel();
                    documentoImovelListNewDocumentoImovel.setImovel(imovel);
                    documentoImovelListNewDocumentoImovel = em.merge(documentoImovelListNewDocumentoImovel);
                    if (oldImovelOfDocumentoImovelListNewDocumentoImovel != null && !oldImovelOfDocumentoImovelListNewDocumentoImovel.equals(imovel)) {
                        oldImovelOfDocumentoImovelListNewDocumentoImovel.getDocumentoImovelList().remove(documentoImovelListNewDocumentoImovel);
                        oldImovelOfDocumentoImovelListNewDocumentoImovel = em.merge(oldImovelOfDocumentoImovelListNewDocumentoImovel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = imovel.getId();
                if (findImovel(id) == null) {
                    throw new NonexistentEntityException("The imovel with id " + id + " no longer exists.");
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
            Imovel imovel;
            try {
                imovel = em.getReference(Imovel.class, id);
                imovel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The imovel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FotoImovel> fotoImovelListOrphanCheck = imovel.getFotoImovelList();
            for (FotoImovel fotoImovelListOrphanCheckFotoImovel : fotoImovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Imovel (" + imovel + ") cannot be destroyed since the FotoImovel " + fotoImovelListOrphanCheckFotoImovel + " in its fotoImovelList field has a non-nullable imovel field.");
            }
            List<LocatarioImovel> locatarioImovelListOrphanCheck = imovel.getLocatarioImovelList();
            for (LocatarioImovel locatarioImovelListOrphanCheckLocatarioImovel : locatarioImovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Imovel (" + imovel + ") cannot be destroyed since the LocatarioImovel " + locatarioImovelListOrphanCheckLocatarioImovel + " in its locatarioImovelList field has a non-nullable imovel field.");
            }
            List<Aluguel> aluguelListOrphanCheck = imovel.getAluguelList();
            for (Aluguel aluguelListOrphanCheckAluguel : aluguelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Imovel (" + imovel + ") cannot be destroyed since the Aluguel " + aluguelListOrphanCheckAluguel + " in its aluguelList field has a non-nullable imovel field.");
            }
            List<DocumentoImovel> documentoImovelListOrphanCheck = imovel.getDocumentoImovelList();
            for (DocumentoImovel documentoImovelListOrphanCheckDocumentoImovel : documentoImovelListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Imovel (" + imovel + ") cannot be destroyed since the DocumentoImovel " + documentoImovelListOrphanCheckDocumentoImovel + " in its documentoImovelList field has a non-nullable imovel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Endereco endereco = imovel.getEndereco();
            if (endereco != null) {
                endereco.getImovelList().remove(imovel);
                endereco = em.merge(endereco);
            }
            em.remove(imovel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Imovel> findImovelEntities() {
        return findImovelEntities(true, -1, -1);
    }

    public List<Imovel> findImovelEntities(int maxResults, int firstResult) {
        return findImovelEntities(false, maxResults, firstResult);
    }

    private List<Imovel> findImovelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Imovel.class));
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

    public Imovel findImovel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Imovel.class, id);
        } finally {
            em.close();
        }
    }

    public int getImovelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Imovel> rt = cq.from(Imovel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
