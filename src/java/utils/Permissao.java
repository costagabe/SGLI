/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Gabriel Alves
 */
public class Permissao {

    public Permissao(String nome, String classe, String[] acoes) {
        this.nomePermissao = nome;
        this.classePermissao = classe;
        this.acoes = acoes;
    }
    private String nomePermissao;
    private String classePermissao;
    private String acoes[];

    public String getNomePermissao() {
        return nomePermissao;
    }

    public void setNomePermissao(String nomePermissao) {
        this.nomePermissao = nomePermissao;
    }

    public String getClassePermissao() {
        return classePermissao;
    }

    public void setClassePermissao(String classePermissao) {
        this.classePermissao = classePermissao;
    }

    public String[] getAcoes() {
        return acoes;
    }

    public void setAcoes(String[] acoes) {
        this.acoes = acoes;
    }

}
