package controllers;

import beans.DocumentoAluguel;
import beans.LocatarioAluguel;
import beans.LocatarioImovel;
import beans.ObservacaoAluguel;
import beans.PagamentoAluguel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daos.AluguelJpaController;
import daos.DocumentoAluguelJpaController;
import daos.DocumentoJpaController;
import daos.ImovelJpaController;
import daos.LocatarioAluguelJpaController;
import daos.ObservacaoAluguelJpaController;
import daos.PagamentoAluguelJpaController;
import daos.PessoaJpaController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Aluguel implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verObservacoes(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        req.setAttribute("aluguel", a);
        return "ver-observacoes.jsp";
    }
    public String excluirObservacao(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.ObservacaoAluguel a = new Gson().fromJson(req.getParameter("data"), beans.ObservacaoAluguel.class);
         new ObservacaoAluguelJpaController(emf).destroy(a.getId());
         Modal m = new Modal("Sucesso", "Observação apagada com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }
    
    public String cadastrarObservacao(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        ObservacaoAluguel oa = new Gson().fromJson(req.getParameter("data"), beans.ObservacaoAluguel.class);
        oa.setData(Calendar.getInstance().getTime());
        System.out.println(req.getParameter("data"));
        new ObservacaoAluguelJpaController(emf).create(oa);
        Modal m = new Modal("Sucesso","Observação cadastrada com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }
    public String showDocs(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Aluguel a = gson.fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        req.setAttribute("docs", a.getDocumentoAluguelList());
        req.setAttribute("excluir", "confirmarExcluirDocumentoAluguel");
        return "image-viwer.jsp";
    }
     public String listarLocadores(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);

        AluguelJpaController aluguelDao = new AluguelJpaController(emf);
        a = aluguelDao.findAluguel(a.getId());
        List<beans.Pessoa> locadores = new ArrayList<>();

        for (LocatarioImovel li : a.getImovel().getLocatarioImovelList()) {
            locadores.add(li.getLocatario().clone());
        }
        req.setAttribute("json", new Gson().toJson(locadores));
        return "Json";
    }
      public String listarLocatarios(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);

        AluguelJpaController aluguelDao = new AluguelJpaController(emf);
        a = aluguelDao.findAluguel(a.getId());
        List<beans.Pessoa> locadores = new ArrayList<>();

        for (LocatarioAluguel li : a.getLocatarioAluguelList()) {
            locadores.add(li.getLocatario().clone());
        }
        req.setAttribute("json", new Gson().toJson(locadores));
        return "Json";
    }

    public String listarImovel(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Aluguel a = gson.fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        JsonArray ar = new JsonArray();
        beans.Imovel imovel = a.getImovel().clone();
        imovel.setEndereco(a.getImovel().getEndereco().clone());
        ar.add(gson.toJsonTree(imovel));
        req.setAttribute("json", ar);

        return "Json";
    }
    public String excluirPagamento(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        PagamentoAluguel pa = new Gson().fromJson(req.getParameter("data"), PagamentoAluguel.class);
        new PagamentoAluguelJpaController(emf).destroy(pa.getId());
        Modal m = new Modal("Sucesso","Pagamento excluido com sucesso");
        req.setAttribute("json", m.getJson());
        return "Json";
    }
    public String verPagamentos(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        req.setAttribute("aluguel", a);
        return "pagamento-aluguel.jsp";
    }
    public String cadastrarPagamento(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        PagamentoAluguel pa = new Gson().fromJson(req.getParameter("data"), PagamentoAluguel.class);
        
        new PagamentoAluguelJpaController(emf).create(pa);
        Modal m = new Modal("Sucesso","Pagamento realizado com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }
    public String listarLocatariosAluguel(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        List<beans.Pessoa> pessoas = new ArrayList<>();
        for(LocatarioAluguel la : a.getLocatarioAluguelList()){
            pessoas.add(la.getLocatario());
        }
        req.setAttribute("pessoas", pessoas);
        return "listar-pessoas-aluguel.jsp";
    }
    public String listarLocadoresAluguel(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);
        a = new AluguelJpaController(emf).findAluguel(a.getId());
        List<beans.Pessoa> pessoas = new ArrayList<>();
        for(LocatarioImovel la : a.getImovel().getLocatarioImovelList()){
            pessoas.add(la.getLocatario());
        }
        req.setAttribute("pessoas", pessoas);
        return "listar-pessoas-aluguel.jsp";
    }
    public String Listar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        int pagina = 1;

        List<beans.Aluguel> alugueis = new ArrayList<>();
        AluguelJpaController aluguelDao = new AluguelJpaController(emf);
        int paginas = (int) (aluguelDao.getAluguelCount() / 10) + 1;
        for (beans.Aluguel a : aluguelDao.findAluguelEntities(10, 0)) {
            alugueis.add(a.clone());
        }
        req.setAttribute("paginas", paginas);
        req.setAttribute("alugueis", gson.toJson(alugueis));
        return "listar-alugueis.jsp";
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        java.lang.System.out.println(req.getParameter("data"));
        beans.Aluguel a = new Gson().fromJson(req.getParameter("data"), beans.Aluguel.class);

        AluguelJpaController aluguelDao = new AluguelJpaController(emf);
        DocumentoAluguelJpaController documentoDao = new DocumentoAluguelJpaController(emf);
        LocatarioAluguelJpaController locatarioDao = new LocatarioAluguelJpaController(emf);

        List<DocumentoAluguel> documentos = a.getDocumentoAluguelList();
        List<LocatarioAluguel> locatarios = a.getLocatarioAluguelList();

        a.setDocumentoAluguelList(new ArrayList<>());
        a.setLocatarioAluguelList(new ArrayList<>());

        aluguelDao.create(a);

        documentos.forEach((l) -> {
            l.setAluguel(a);
            documentoDao.create(l);
        });
        locatarios.forEach((l) -> {
            l.setAluguel(a);
            locatarioDao.create(l);
        });

        Modal m = new Modal("Sucesso", "Alguel cadastrado com sucesso");
        JsonObject obj = new JsonObject();
        obj.addProperty("code", "1");
        obj.add("modal", new Gson().toJsonTree(m, Modal.class));
        req.setAttribute("json", obj);
        return "Json";
    }

    public String cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        DocumentoJpaController docDao = new DocumentoJpaController(emf);
        req.setAttribute("tiposDocumentos", docDao.findDocumentoEntities(3));
        return "novo-aluguel.jsp";
    }

}
