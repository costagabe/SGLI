/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import utils.ArrayListCopier;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "aluguel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aluguel.findAll", query = "SELECT a FROM Aluguel a")
    , @NamedQuery(name = "Aluguel.findById", query = "SELECT a FROM Aluguel a WHERE a.id = :id")
    , @NamedQuery(name = "Aluguel.findByDiaPagamento", query = "SELECT a FROM Aluguel a WHERE a.diaPagamento = :diaPagamento")
    , @NamedQuery(name = "Aluguel.findByDataInicioContrato", query = "SELECT a FROM Aluguel a WHERE a.dataInicioContrato = :dataInicioContrato")
    , @NamedQuery(name = "Aluguel.findByDataFimContrato", query = "SELECT a FROM Aluguel a WHERE a.dataFimContrato = :dataFimContrato")
    , @NamedQuery(name = "Aluguel.findByValor", query = "SELECT a FROM Aluguel a WHERE a.valor = :valor")
    , @NamedQuery(name = "Aluguel.findByDataPrimeiroPagamento", query = "SELECT a FROM Aluguel a WHERE a.dataPrimeiroPagamento = :dataPrimeiroPagamento")
    , @NamedQuery(name = "Aluguel.findByGarantia", query = "SELECT a FROM Aluguel a WHERE a.garantia = :garantia")
    , @NamedQuery(name = "Aluguel.findByTipoPagamento", query = "SELECT a FROM Aluguel a WHERE a.tipoPagamento = :tipoPagamento")})
public class Aluguel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "diaPagamento")
    private int diaPagamento;
    @Basic(optional = false)
    @Column(name = "dataInicioContrato")
    @Temporal(TemporalType.DATE)
    private Date dataInicioContrato;
    @Basic(optional = false)
    @Column(name = "dataFimContrato")
    @Temporal(TemporalType.DATE)
    private Date dataFimContrato;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @Basic(optional = false)
    @Column(name = "dataPrimeiroPagamento")
    @Temporal(TemporalType.DATE)
    private Date dataPrimeiroPagamento;
    @Basic(optional = false)
    @Column(name = "garantia")
    private String garantia;
    @Basic(optional = false)
    @Column(name = "tipoPagamento")
    private int tipoPagamento;
    @JoinColumn(name = "imovel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Imovel imovel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluguel")
    private List<DocumentoAluguel> documentoAluguelList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluguel")
    private List<ObservacaoAluguel> observacaoAluguelList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluguel")
    private List<LocatarioAluguel> locatarioAluguelList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aluguel")
    private List<PagamentoAluguel> pagamentoAluguelList;

    public Aluguel() {
    }

    public Aluguel(Integer id) {
        this.id = id;
    }

    public Aluguel(Integer id, int diaPagamento, Date dataInicioContrato, Date dataFimContrato, double valor, Date dataPrimeiroPagamento, String garantia, int tipoPagamento) {
        this.id = id;
        this.diaPagamento = diaPagamento;
        this.dataInicioContrato = dataInicioContrato;
        this.dataFimContrato = dataFimContrato;
        this.valor = valor;
        this.dataPrimeiroPagamento = dataPrimeiroPagamento;
        this.garantia = garantia;
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDiaPagamento() {
        return diaPagamento;
    }

    public void setDiaPagamento(int diaPagamento) {
        this.diaPagamento = diaPagamento;
    }

    public Date getDataInicioContrato() {
        return dataInicioContrato;
    }

    public void setDataInicioContrato(Date dataInicioContrato) {
        this.dataInicioContrato = dataInicioContrato;
    }

    public Date getDataFimContrato() {
        return dataFimContrato;
    }

    public void setDataFimContrato(Date dataFimContrato) {
        this.dataFimContrato = dataFimContrato;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataPrimeiroPagamento() {
        return dataPrimeiroPagamento;
    }

    public void setDataPrimeiroPagamento(Date dataPrimeiroPagamento) {
        this.dataPrimeiroPagamento = dataPrimeiroPagamento;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public int getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(int tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    @XmlTransient
    public List<DocumentoAluguel> getDocumentoAluguelList() {
        return documentoAluguelList;
    }

    public void setDocumentoAluguelList(List<DocumentoAluguel> documentoAluguelList) {
        this.documentoAluguelList = documentoAluguelList;
    }

    @XmlTransient
    public List<ObservacaoAluguel> getObservacaoAluguelList() {
        return observacaoAluguelList;
    }

    public void setObservacaoAluguelList(List<ObservacaoAluguel> observacaoAluguelList) {
        this.observacaoAluguelList = observacaoAluguelList;
    }

    @XmlTransient
    public List<LocatarioAluguel> getLocatarioAluguelList() {
        return locatarioAluguelList;
    }

    public void setLocatarioAluguelList(List<LocatarioAluguel> locatarioAluguelList) {
        this.locatarioAluguelList = locatarioAluguelList;
    }

    @XmlTransient
    public List<PagamentoAluguel> getPagamentoAluguelList() {
        return pagamentoAluguelList;
    }

    public void setPagamentoAluguelList(List<PagamentoAluguel> pagamentoAluguelList) {
        this.pagamentoAluguelList = pagamentoAluguelList;
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
        if (!(object instanceof Aluguel)) {
            return false;
        }
        Aluguel other = (Aluguel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Aluguel[ id=" + id + " ]";
    }


    @Override
    public Aluguel clone() {
        Aluguel novo = new Aluguel(id, diaPagamento, dataInicioContrato, dataFimContrato, valor, dataPrimeiroPagamento, garantia, tipoPagamento);
        novo.imovel = (new Imovel(imovel.getId()));
        novo.locatarioAluguelList = ArrayListCopier.copyList(locatarioAluguelList);
        novo.documentoAluguelList = ArrayListCopier.copyList(documentoAluguelList);
        novo.observacaoAluguelList = ArrayListCopier.copyList(observacaoAluguelList);
        novo.pagamentoAluguelList = ArrayListCopier.copyList(pagamentoAluguelList);
        return novo;

    }

}
