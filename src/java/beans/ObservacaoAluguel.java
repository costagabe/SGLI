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
import javax.persistence.Lob;
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
@Table(name = "observacao_aluguel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacaoAluguel.findAll", query = "SELECT o FROM ObservacaoAluguel o")
    , @NamedQuery(name = "ObservacaoAluguel.findById", query = "SELECT o FROM ObservacaoAluguel o WHERE o.id = :id")
    , @NamedQuery(name = "ObservacaoAluguel.findByData", query = "SELECT o FROM ObservacaoAluguel o WHERE o.data = :data")})
public class ObservacaoAluguel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "observacao")
    private String observacao;
    @Basic(optional = false)
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "aluguel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aluguel aluguel;

    public ObservacaoAluguel() {
    }
    public String getDataStr(){
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }

    public ObservacaoAluguel(Integer id) {
        this.id = id;
    }

    public ObservacaoAluguel(Integer id, String observacao, Date data) {
        this.id = id;
        this.observacao = observacao;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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
        if (!(object instanceof ObservacaoAluguel)) {
            return false;
        }
        ObservacaoAluguel other = (ObservacaoAluguel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.ObservacaoAluguel[ id=" + id + " ]";
    }
    
}
