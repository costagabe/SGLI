package controllers;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    public abstract String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception;

}
