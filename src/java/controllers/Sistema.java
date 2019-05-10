package controllers;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriel Alves
 */
public class Sistema implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "index.jsp";
    }

}
