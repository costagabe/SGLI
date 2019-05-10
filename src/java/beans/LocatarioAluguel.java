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
@Table(name = "locatario_aluguel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LocatarioAluguel.findAll", query = "SELECT l FROM LocatarioAluguel l")
    , @NamedQuery(name = "LocatarioAluguel.findById", query = "SELECT l FROM LocatarioAluguel l WHERE l.id = :id")})
public class LocatarioAluguel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "locatario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pessoa locatario;
    @JoinColumn(name = "aluguel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aluguel aluguel;

    public LocatarioAluguel() {
    }

    public LocatarioAluguel(Integer id) {
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
        if (!(object instanceof LocatarioAluguel)) {
            return false;
        }
        LocatarioAluguel other = (LocatarioAluguel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.LocatarioAluguel[ id=" + id + " ]";
    }
    
    public LocatarioAluguel clone(){
        LocatarioAluguel novo = new LocatarioAluguel();
        novo.aluguel = aluguel.clone();
        novo.id=id;
        novo.locatario = locatario.clone();
        return novo;
    }
    
}
