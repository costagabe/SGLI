/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "pagamento_aluguel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagamentoAluguel.findAll", query = "SELECT p FROM PagamentoAluguel p")
    , @NamedQuery(name = "PagamentoAluguel.findById", query = "SELECT p FROM PagamentoAluguel p WHERE p.id = :id")
    , @NamedQuery(name = "PagamentoAluguel.findByValor", query = "SELECT p FROM PagamentoAluguel p WHERE p.valor = :valor")
    , @NamedQuery(name = "PagamentoAluguel.findByDataEfetuacao", query = "SELECT p FROM PagamentoAluguel p WHERE p.dataEfetuacao = :dataEfetuacao")
    , @NamedQuery(name = "PagamentoAluguel.findByDataRepasse", query = "SELECT p FROM PagamentoAluguel p WHERE p.dataRepasse = :dataRepasse")
    , @NamedQuery(name = "PagamentoAluguel.findByRecibo", query = "SELECT p FROM PagamentoAluguel p WHERE p.recibo = :recibo")})
public class PagamentoAluguel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @Basic(optional = false)
    @Column(name = "data_efetuacao")
    @Temporal(TemporalType.DATE)
    private Date dataEfetuacao;
    @Basic(optional = false)
    @Column(name = "data_repasse")
    @Temporal(TemporalType.DATE)
    private Date dataRepasse;
    @Column(name = "recibo")
    private String recibo;
    @JoinColumn(name = "aluguel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aluguel aluguel;
    @Basic(optional = false)
    @Column(name = "situacao")
    private int situacao;

    public PagamentoAluguel() {
    }

    public PagamentoAluguel(Integer id) {
        this.id = id;
    }

    public PagamentoAluguel(Integer id, double valor, Date dataEfetuacao, Date dataRepasse) {
        this.id = id;
        this.valor = valor;
        this.dataEfetuacao = dataEfetuacao;
        this.dataRepasse = dataRepasse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataEfetuacao() {
        return dataEfetuacao;
    }
    public String getValorStr(){
        int aux1 = (int)(this.valor * 100);
        String aux = aux1+"";
        String valorStr = aux.substring(0, aux.length()-2) + "," + aux.substring(aux.length()-2,aux.length());
        return "R$ " + valorStr;
    }

    public String getDataEfetuacaoString(){
        return new SimpleDateFormat("dd/MM/yyyy").format(dataEfetuacao);
    }
    public String getDataRepasseString(){
        return new SimpleDateFormat("dd/MM/yyyy").format(dataRepasse);
    }
    public void setDataEfetuacao(Date dataEfetuacao) {
        this.dataEfetuacao = dataEfetuacao;
    }

    public Date getDataRepasse() {
        return dataRepasse;
    }

    public void setDataRepasse(Date dataRepasse) {
        this.dataRepasse = dataRepasse;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
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
        if (!(object instanceof PagamentoAluguel)) {
            return false;
        }
        PagamentoAluguel other = (PagamentoAluguel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.PagamentoAluguel[ id=" + id + " ]";
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }
    
}
