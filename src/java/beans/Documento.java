/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import utils.ArrayListCopier;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d")
    , @NamedQuery(name = "Documento.findById", query = "SELECT d FROM Documento d WHERE d.id = :id")
    , @NamedQuery(name = "Documento.findByNome", query = "SELECT d FROM Documento d WHERE d.nome = :nome")
    , @NamedQuery(name = "Documento.findByTipo", query = "SELECT d FROM Documento d WHERE d.tipo = :tipo")})
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "tipo")
    private int tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private  List<DocumentoAluguel> documentoAluguelList = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private  List<DocumentoPessoa> documentoPessoaList = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private  List<DocumentoImovel> documentoImovelList = new LinkedList<>();

    public Documento() {
    }

    public Documento(Integer id) {
        this.id = id;
    }

    public Documento(Integer id, String nome, int tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Documento(String nome, int tipo) {
        this.nome = nome;
        this.tipo = tipo;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<DocumentoAluguel> getDocumentoAluguelList() {
        return documentoAluguelList;
    }

    public void setDocumentoAluguelList(List<DocumentoAluguel> documentoAluguelList) {
        this.documentoAluguelList = documentoAluguelList;
    }

    @XmlTransient
    public List<DocumentoPessoa> getDocumentoPessoaList() {
        return documentoPessoaList;
    }

    public void setDocumentoPessoaList(List<DocumentoPessoa> documentoPessoaList) {
        this.documentoPessoaList = documentoPessoaList;
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
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Documento[ id=" + id + " ]";
    }
    public Documento clone(){
        Documento novo = new Documento();
        novo.id = id;
        novo.nome = nome;
        novo.tipo = tipo;
        novo.documentoAluguelList = ArrayListCopier.copyList(documentoAluguelList);
        novo.documentoImovelList = ArrayListCopier.copyList(documentoImovelList);
        novo.documentoPessoaList= ArrayListCopier.copyList(documentoPessoaList);
        
        return novo;
        
    }
}
