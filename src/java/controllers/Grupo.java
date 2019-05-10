/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.GrupoJpaController;
import daos.PermissoesJpaController;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Grupo implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Grupo g = new Gson().fromJson(req.getParameter("data"), beans.Grupo.class);
        JsonObject obj;
        GrupoJpaController grupoDao = new GrupoJpaController(emf);
        PermissoesJpaController permissoesDao = new PermissoesJpaController(emf);

        grupoDao.create(g);
        for (int i : g.getPermissoes()) {
            beans.Permissoes p = new beans.Permissoes();
            p.setGrupo(g);
            p.setPermissao("" + i);
            permissoesDao.create(p);

        }
        Modal m = new Modal("Sucesso", "Grupo criado com sucesso!");
        obj = (JsonObject) new Gson().toJsonTree(m, Modal.class);
        req.setAttribute("json", obj);
        return "Json";
    }

    public String Cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        req.setAttribute("permissoes", utils.Permissoes.getInstance().getPermissoes());
        return "cadastro-grupo.jsp";
    }

}
