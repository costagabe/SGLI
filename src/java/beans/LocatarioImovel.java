/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "locatario_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LocatarioImovel.findAll", query = "SELECT l FROM LocatarioImovel l")
    , @NamedQuery(name = "LocatarioImovel.findById", query = "SELECT l FROM LocatarioImovel l WHERE l.id = :id")})
public class LocatarioImovel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "locatario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pessoa locatario;
    @JoinColumn(name = "imovel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Imovel imovel;

    public LocatarioImovel() {
    }

    public LocatarioImovel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getLocatario() {
        return locatario;
    }

    public void setLocatario(Pessoa locatario) {
        this.locatario = locatario;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
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
        if (!(object instanceof LocatarioImovel)) {
            return false;
        }
        LocatarioImovel other = (LocatarioImovel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.LocatarioImovel[ id=" + id + " ]";
    }
    public LocatarioImovel clone(){
        LocatarioImovel novo = new LocatarioImovel();
        novo.id = id;
        novo.imovel = new Imovel(imovel.getId());
        novo.locatario = new Pessoa(locatario.getId());
        return novo;
    }
}
