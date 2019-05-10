/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.DocumentoPessoa;
import beans.LocatarioAluguel;
import beans.LocatarioImovel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Alves
 */
public class Copier {
    public static List<DocumentoPessoa> copyDocumentoPessoaList(List<DocumentoPessoa> documentoPessoaList) {
        List<DocumentoPessoa> ret = new ArrayList<>();
        for (DocumentoPessoa dp : documentoPessoaList) {
            DocumentoPessoa novo = new DocumentoPessoa();
            novo.setFoto(dp.getFoto());
            novo.setId(dp.getId());
            beans.Documento novoDoc = new beans.Documento();
            novoDoc.setNome(dp.getDocumento().getNome());
            novoDoc.setTipo(dp.getDocumento().getTipo());
            novoDoc.setId(dp.getDocumento().getId());
            novo.setDocumento(novoDoc);
            ret.add(novo);
        }
        return ret;
    }

    public static List<LocatarioAluguel> copyLocatarioAluguelList(List<LocatarioAluguel> locatarioAluguelList) {
        List<LocatarioAluguel> ret = new ArrayList<>();
        for (LocatarioAluguel la : locatarioAluguelList) {
            LocatarioAluguel novo = new LocatarioAluguel();
            beans.Aluguel novoAluguel = new beans.Aluguel();
            novoAluguel.setDataFimContrato(la.getAluguel().getDataFimContrato());
            novoAluguel.setDataInicioContrato(la.getAluguel().getDataInicioContrato());
            novoAluguel.setDataPrimeiroPagamento(la.getAluguel().getDataPrimeiroPagamento());
            novoAluguel.setDiaPagamento(la.getAluguel().getDiaPagamento());
            novoAluguel.setId(la.getId());
            novo.setAluguel(novoAluguel);
            ret.add(novo);
        }
        return ret;
    }

    public static List<LocatarioImovel> copyImovelList(List<LocatarioImovel> locatarioImovelList) {
        List<LocatarioImovel> ret = new ArrayList<>();
        for (LocatarioImovel li : locatarioImovelList) {
            LocatarioImovel novo = new LocatarioImovel();

            novo.setId(li.getId());
            beans.Imovel novoImovel = new beans.Imovel();
            novoImovel.setId(li.getImovel().getId());
            novo.setImovel(novoImovel);
            ret.add(novo);
        }
        return ret;
    }
}
