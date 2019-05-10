package controllers;

import beans.Usuario;
import com.google.gson.JsonObject;
import daos.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.MD5;
import utils.Modal;

/**
 *
 * @author Gabriel
 */
public class Login implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "login.jsp";
    }

    public String logar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("SLGPU");
        String user = req.getParameter("user");
        String password = MD5.md5(req.getParameter("password"));
        Usuario logado = new UsuarioJpaController(emf2).login(user, password);

        if (logado == null) {
            //  Modal m = new Modal("Usuário inválido", "O usuário digitado não existe ou a senha é invalida");
            //  req.setAttribute("modal", m.getJson());
            JsonObject obj = new JsonObject();
            obj.addProperty("url", "login.jsp");
            req.setAttribute("json", obj);
            return "Json";
        } else {
            JsonObject obj = new JsonObject();
            obj.addProperty("url", "index.jsp");
            req.setAttribute("json", obj);
            
            java.lang.System.out.println(emf2 + "--");
            req.getSession().setAttribute("emf", emf2);
            req.getSession().setAttribute("logado", user);
            return "Json";
        }

    }

}
