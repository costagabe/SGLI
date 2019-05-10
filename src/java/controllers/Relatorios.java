package controllers;

import beans.PagamentoAluguel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daos.PagamentoAluguelJpaController;
import daos.PessoaJpaController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriel Alves
 */
public class Relatorios implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String rendimentosLocador(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        req.setAttribute("tipo", "gerarRelatorioRendimentosLocador");
        req.setAttribute("str", "Locador");
        return "gerador-relatorio-rendimentos-locador.jsp";
    }

    public String rendimentosLocatario(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        req.setAttribute("tipo", "gerarRelatorioRendimentosLocatario");
        req.setAttribute("str", "Locatário");
        return "gerador-relatorio-rendimentos-locador.jsp";
    }

    public String gerarRelatorioRendimentosLocatario(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Pessoa p = gson.fromJson(gson.fromJson(req.getParameter("data"), JsonObject.class).get("locador").getAsJsonObject(), beans.Pessoa.class);
        String dataInicio;
        String dataFim;
        dataInicio = gson.fromJson(req.getParameter("data"), JsonObject.class).get("dataInicio").getAsString();

        dataFim = gson.fromJson(req.getParameter("data"), JsonObject.class).get("dataFim").getAsString();

        Date data1 = new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio);
        Date data2 = new SimpleDateFormat("yyyy-MM-dd").parse(dataFim);
        List<PagamentoAluguel> pagamentos = new PagamentoAluguelJpaController(emf).relatorioLocatario(p, data1, data2);
        double total = 0;

        List<beans.Pessoa> locadores = new ArrayList<>();
        List<beans.Pessoa> locatarios = new ArrayList<>();
        for (PagamentoAluguel pa : pagamentos) {

            total += pa.getValor();
        }
        PagamentoAluguel aux = new PagamentoAluguel();
        aux.setValor(total);
        req.setAttribute("total", aux.getValorStr());

        req.setAttribute("pagamentos", pagamentos);
        return "relatorio-rendimentos-locador.jsp";
    }

    public String gerarRelatorioRendimentosLocador(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        System.out.println(req.getParameter("data"));
        beans.Pessoa p = gson.fromJson(gson.fromJson(req.getParameter("data"), JsonObject.class).get("locador").getAsJsonObject(), beans.Pessoa.class);
        String dataInicio;
        String dataFim;
        System.out.println(req.getParameter("data"));
        dataInicio = gson.fromJson(req.getParameter("data"), JsonObject.class).get("dataInicio").getAsString();

        dataFim = gson.fromJson(req.getParameter("data"), JsonObject.class).get("dataFim").getAsString();

        Date data1 = new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio);
        Date data2 = new SimpleDateFormat("yyyy-MM-dd").parse(dataFim);
        List<PagamentoAluguel> pagamentos = new PagamentoAluguelJpaController(emf).relatorioLocador(p, data1, data2);
        double total = 0;
        for (PagamentoAluguel pa : pagamentos) {
            pa.setValor(pa.getValor() * 0.9);
            total += pa.getValor();
        }

        PagamentoAluguel aux = new PagamentoAluguel();
        aux.setValor(total);
        req.setAttribute("total", aux.getValorStr());
        req.setAttribute("pagamentos", pagamentos);
        return "relatorio-rendimentos-locador.jsp";
    }
}
