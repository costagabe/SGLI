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
@Table(name = "foto_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FotoImovel.findAll", query = "SELECT f FROM FotoImovel f")
    , @NamedQuery(name = "FotoImovel.findById", query = "SELECT f FROM FotoImovel f WHERE f.id = :id")
    , @NamedQuery(name = "FotoImovel.findByFoto", query = "SELECT f FROM FotoImovel f WHERE f.foto = :foto")})
public class FotoImovel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "foto")
    private String foto;
    @JoinColumn(name = "imovel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Imovel imovel;

    public FotoImovel() {
    }

    public FotoImovel(Integer id) {
        this.id = id;
    }

    public FotoImovel(Integer id, String foto) {
        this.id = id;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
        if (!(object instanceof FotoImovel)) {
            return false;
        }
        FotoImovel other = (FotoImovel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.FotoImovel[ id=" + id + " ]";
    }
    
    public boolean isIsFoto(){
        return true;
    }
    
}
