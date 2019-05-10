package servlets;

import beans.Usuario;
import com.google.gson.JsonObject;
import controllers.Controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
@WebServlet(name = "Sistema", urlPatterns = {"/System"})
public class System extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            long inicio = java.lang.System.currentTimeMillis();
            long decorrido = 0;
            if (request.getSession().getAttribute("logado") != null) {

                String page = request.getParameter("page");
                String action = request.getParameter("action");
                if (page == null) {
                    page = "Sistema";
                    action = "execute";
                }
                if (podeAcessar((Usuario) request.getSession().getAttribute("logado"), page, action)) {

                    log("Método " + action + " da classe " + page);
                    Class<?> classs;
                    Controller controller;
                    classs = Class.forName("controllers." + page);
                    controller = (Controller) classs.newInstance();
                    EntityManagerFactory emf = (EntityManagerFactory) request.getSession().getAttribute("emf");//Persistence.createEntityManagerFactory("SGLPU"); //
                    if (emf == null) {
                        emf = Persistence.createEntityManagerFactory("SGLPU");
                        request.getSession().setAttribute("emf", emf);
                    }
                    // Chama o método "action" da classe "pageRequest" enviadas pelo usuário por meio da url
                    page = "/" + (String) (controller.getClass().getMethod(action, new Class[]{HttpServletRequest.class, HttpServletResponse.class, EntityManagerFactory.class}).invoke(controller, request, response, emf));

                    // emf.close();
                    decorrido = java.lang.System.currentTimeMillis() - inicio;
                    log("tempo decorrido " + decorrido);
                    //retorna o código da view que a lógica executou
                    request.getRequestDispatcher(page).forward(request, response);
                } else {
                    out.println("<h1 style='color:red'>Acesso negado!</h1>");
                }
            } else {
                response.sendRedirect("Login");
                out.println("Acesso negado");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(System.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean podeAcessar(Usuario usuario, String page, String action) {
        List<utils.Permissao> permissoesLivres = utils.Permissoes.getInstance().getPermissoesLivres();
        for (utils.Permissao p : permissoesLivres) {
            if (p.getClassePermissao().equals(page) && acaoValida(action, p.getAcoes())) {
                return true;
            }
        }

        List<utils.Permissao> permissoes = utils.Permissoes.getInstance().getPermissoes();
        for (beans.Permissoes p : usuario.getGrupo().getPermissoesList()) {

            int posPermissao = Integer.parseInt(p.getPermissao());
            if (permissoes.get(posPermissao).getClassePermissao().equals(page) && acaoValida(action, permissoes.get(posPermissao).getAcoes())) {
                return true;
            }

        }
        return false;
    }

    private boolean acaoValida(String action, String[] acoes) {
        for (String s : acoes) {
            if (s.equals(action)) {
                return true;
            }
        }
        return false;
    }

}
