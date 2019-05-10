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
@Table(name = "documento_aluguel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoAluguel.findAll", query = "SELECT d FROM DocumentoAluguel d")
    , @NamedQuery(name = "DocumentoAluguel.findById", query = "SELECT d FROM DocumentoAluguel d WHERE d.id = :id")})
public class DocumentoAluguel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Documento documento;
    @JoinColumn(name = "aluguel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Aluguel aluguel;
    @Basic(optional = false)
    @Column(name = "foto")
    private String foto;

    public DocumentoAluguel() {
    }

    public DocumentoAluguel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
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
        if (!(object instanceof DocumentoAluguel)) {
            return false;
        }
        DocumentoAluguel other = (DocumentoAluguel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.DocumentoAluguel[ id=" + id + " ]";
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public DocumentoAluguel clone(){
        DocumentoAluguel novo = new DocumentoAluguel();
        novo.aluguel = new Aluguel(aluguel.getId());
        novo.documento = new Documento(documento.getId());
        novo.foto = foto;
        novo.id = id;
        return novo;
    }
    public boolean isIsFoto(){
        return false;
    }
}
