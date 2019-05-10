package controllers;

import com.google.gson.Gson;
import daos.DocumentoJpaController;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Documento implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String Cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "cadastro-documento.jsp";
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Documento d = new Gson().fromJson(req.getParameter("data"), beans.Documento.class);
        DocumentoJpaController documentoDao = new DocumentoJpaController(emf);
        Modal m;
        beans.Documento nomeIgual = documentoDao.findByNomeAndTipo(d.getNome(),d.getTipo());
        if (nomeIgual == null) {
            documentoDao.create(d);
            m = new Modal("Sucesso", "Cadastro realizado com sucesso!");
        } else {
            m = new Modal("Erro", "Já existe um documento com esse nome!");
        }
        req.setAttribute("json", new Gson().toJson(m));

        return "Json";
    }
}
