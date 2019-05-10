/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.GrupoJpaController;
import daos.UsuarioJpaController;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.MD5;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Usuario implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String Cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        List<beans.Grupo> grupos = new GrupoJpaController(emf).findGrupoEntities();
        req.setAttribute("grupos", grupos);
        return "cadastro-usuario.jsp";
    }

    public String gerenciarConta(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "gerenciar-conta.jsp";
    }
    public String mudarSenha(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String senha = new Gson().fromJson(req.getParameter("data"), JsonObject.class).get("senha").toString().replace("\"", "");
        beans.Usuario logado = (beans.Usuario) req.getSession().getAttribute("logado");
        logado.setSenha(MD5.md5(senha));
        new UsuarioJpaController(emf).edit(logado);
        Modal m = new Modal("Sucesso", "Sua senha foi alterada com sucesso.");
        req.setAttribute("json", new Gson().toJson(m));
        return "Json";
    }


    public String mudarFoto(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String novaFoto = new Gson().fromJson(req.getParameter("data"), JsonObject.class).get("foto").toString().replace("\"", "");
        beans.Usuario logado = (beans.Usuario) req.getSession().getAttribute("logado");
        logado.setFoto(novaFoto);
        new UsuarioJpaController(emf).edit(logado);
        Modal m = new Modal("Sucesso", "Sua foto de perfil foi alterada, basta atualizar a página agora.");
        req.setAttribute("json", new Gson().toJson(m));
        return "Json";
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Usuario u = new Gson().fromJson(req.getParameter("data"), beans.Usuario.class);
        u.setSenha(MD5.md5("123456"));
        u.setFoto("user.png");
        new UsuarioJpaController(emf).create(u);
        Modal m = new Modal("Sucesso", "Usuário cadastrado com sucesso. Lembre-se, a senha padrão é 123456, mas, ao entrar no sistema a primera vez, será pedido para mudá-la.");
        req.setAttribute("json", new Gson().toJson(m));
        return "Json";
    }

}
