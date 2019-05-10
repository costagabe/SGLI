/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import utils.ArrayListCopier;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Imovel.findAll", query = "SELECT i FROM Imovel i")
    , @NamedQuery(name = "Imovel.findById", query = "SELECT i FROM Imovel i WHERE i.id = :id")
    , @NamedQuery(name = "Imovel.findByDataCapitacao", query = "SELECT i FROM Imovel i WHERE i.dataCapitacao = :dataCapitacao")})
public class Imovel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data_capitacao")
    @Temporal(TemporalType.DATE)
    private Date dataCapitacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "imovel")
    private List<FotoImovel> fotoImovelList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "imovel")
    private List<LocatarioImovel> locatarioImovelList = new ArrayList<>();
    @JoinColumn(name = "endereco", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Endereco endereco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "imovel")
    private List<Aluguel> aluguelList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "imovel")
    private List<DocumentoImovel> documentoImovelList = new ArrayList<>();

    public Imovel() {
    }

    public Imovel(Integer id) {
        this.id = id;
    }

    public Imovel(Integer id, Date dataCapitacao) {
        this.id = id;
        this.dataCapitacao = dataCapitacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getDataCapitacao() {
        
        return dataCapitacao;
    }

    public void setDataCapitacao(Date dataCapitacao) {
        this.dataCapitacao = dataCapitacao;
    }

    @XmlTransient
    public List<FotoImovel> getFotoImovelList() {
        return fotoImovelList;
    }

    public void setFotoImovelList(List<FotoImovel> fotoImovelList) {
        this.fotoImovelList = fotoImovelList;
    }

    @XmlTransient
    public List<LocatarioImovel> getLocatarioImovelList() {
        return locatarioImovelList;
    }

    public void setLocatarioImovelList(List<LocatarioImovel> locatarioImovelList) {
        this.locatarioImovelList = locatarioImovelList;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @XmlTransient
    public List<Aluguel> getAluguelList() {
        return aluguelList;
    }

    public void setAluguelList(List<Aluguel> aluguelList) {
        this.aluguelList = aluguelList;
    }

    @XmlTransient
    public List<DocumentoImovel> getDocumentoImovelList() {
        return documentoImovelList;
    }

    public void setDocumentoImovelList(List<DocumentoImovel> documentoImovelList) {
        this.documentoImovelList = documentoImovelList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Imovel)) {
            return false;
        }
        Imovel other = (Imovel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Imovel[ id=" + id + " ]";
    }

    public Imovel clone() {
        Imovel novo = new Imovel();
        novo.aluguelList = ArrayListCopier.copyList(aluguelList);
        novo.documentoImovelList = ArrayListCopier.copyList(documentoImovelList);
        novo.fotoImovelList = ArrayListCopier.copyList(fotoImovelList);
        novo.locatarioImovelList = ArrayListCopier.copyList(locatarioImovelList);
        novo.dataCapitacao = dataCapitacao;
        novo.endereco = new Endereco(endereco.getId());
        novo.id = id;
        return novo;
    }
}
