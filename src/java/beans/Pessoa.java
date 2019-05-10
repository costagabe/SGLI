/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.text.DateFormat;
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
import org.eclipse.persistence.annotations.CascadeOnDelete;
import utils.ArrayListCopier;

/**
 *
 * @author Gabriel Alves
 */
@Entity
@Table(name = "pessoa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p")
    , @NamedQuery(name = "Pessoa.findById", query = "SELECT p FROM Pessoa p WHERE p.id = :id")
    , @NamedQuery(name = "Pessoa.findByNome", query = "SELECT p FROM Pessoa p WHERE LOWER(p.nome) = LOWER(:nome)")
    , @NamedQuery(name = "Pessoa.findByEmail", query = "SELECT p FROM Pessoa p WHERE p.email = :email")
    , @NamedQuery(name = "Pessoa.findByTelefone", query = "SELECT p FROM Pessoa p WHERE p.telefone = :telefone")
    , @NamedQuery(name = "Pessoa.findByDataNascimento", query = "SELECT p FROM Pessoa p WHERE p.dataNascimento = :dataNascimento")})
public class Pessoa implements Serializable {

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
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "telefone")
    private String telefone;
    @Basic(optional = false)
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @JoinColumn(name = "endereco", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Endereco endereco;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "locatario")
    @CascadeOnDelete
    private List<LocatarioImovel> locatarioImovelList = new ArrayList<>();
    @CascadeOnDelete
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pessoa", orphanRemoval = true)
    private List<DocumentoPessoa> documentoPessoaList = new ArrayList<>();
    @CascadeOnDelete
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locatario", orphanRemoval = true)
    private List<LocatarioAluguel> locatarioAluguelList = new ArrayList<>();
    private transient int qtdDocs;

    public Pessoa() {
    }

    public Pessoa(Integer id) {
        this.id = id;
    }

    public Pessoa(Integer id, String nome, String email, String telefone, Date dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    public Integer getId() {
        return id;
    }

    public String getDataNascimentoString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(dataNascimento);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @XmlTransient
    public List<LocatarioImovel> getLocatarioImovelList() {
        return locatarioImovelList;
    }

    public void setLocatarioImovelList(List<LocatarioImovel> locatarioImovelList) {
        this.locatarioImovelList = locatarioImovelList;
    }

    @XmlTransient
    public List<DocumentoPessoa> getDocumentoPessoaList() {
        return documentoPessoaList;
    }

    public void setDocumentoPessoaList(List<DocumentoPessoa> documentoPessoaList) {
        this.documentoPessoaList = documentoPessoaList;
    }

    @XmlTransient
    public List<LocatarioAluguel> getLocatarioAluguelList() {
        return locatarioAluguelList;
    }

    public void setLocatarioAluguelList(List<LocatarioAluguel> locatarioAluguelList) {
        this.locatarioAluguelList = locatarioAluguelList;
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
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Pessoa clone() {
        Pessoa nova = new Pessoa();
        nova.dataNascimento = dataNascimento;
        nova.email = email;
        nova.endereco = endereco;
        nova.id = id;
        nova.nome = nome;
        nova.telefone = telefone;
        nova.documentoPessoaList = ArrayListCopier.copyList(documentoPessoaList);
        nova.locatarioAluguelList = ArrayListCopier.copyList(locatarioAluguelList);
        nova.locatarioImovelList = ArrayListCopier.copyList(locatarioImovelList);
        return nova;
    }

    @Override
    public String toString() {
        return "beans.Pessoa[ id=" + id + " ]";
    }

    public int getQtdDocs() {
        return qtdDocs;
    }

    public void setQtdDocs(int qtdDocs) {
        this.qtdDocs = qtdDocs;
    }

}
