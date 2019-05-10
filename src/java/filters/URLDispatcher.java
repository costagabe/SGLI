package filters;

import controllers.Controller;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabriel Alves
 */

public class URLDispatcher implements Filter {

    private static final boolean DEBUG = false;
    private FilterConfig filterConfig = null;

    public URLDispatcher() {
    }

    private boolean contains(String palavra, String[] sequencias) {
        for (String s : sequencias) {
            if (palavra.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //Deixa passar requisições HTTP para arquivos css, js e img
        if (!contains(req.getRequestURI(), new String[]{"css", "js", "img", "vendor", "fonts","Upload"})) {
            String pageRequest = getPageRequest(req);
            String action = getAction(req);
            int userLevel = 0;
            Class<?> classs;
            Controller controller;

            if (LevelAccess.canEnter(pageRequest, userLevel)) {
                try {
                    log("Método " + action + " da classe " + pageRequest);
                    classs = Class.forName("controllers." + pageRequest);

                    controller = (Controller) classs.newInstance();

                    EntityManagerFactory emf = Persistence.createEntityManagerFactory("SGLPU");
                    action = getAction(req);
                    String page;

                    // Chama o método "action" da classe "pageRequest" enviadas pelo usuário por meio da url
                    page = "/" + (String) (controller.getClass().getMethod(action, new Class[]{HttpServletRequest.class, HttpServletResponse.class, EntityManagerFactory.class}).invoke(controller, req, res, emf));
                    emf.close();

                    //retorna o código da view que a lógica executou
                    request.getRequestDispatcher(page).forward(request, response);

                } catch (ClassNotFoundException ex) {
                    log("Erro ao tentar instanciar a classe " + pageRequest);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(URLDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    log("Erro ao invocar método " + action + " da classe " + pageRequest + " o método lançou uma exceção interna: " + ex.getCause().toString());
                } catch (Exception ex) {
                    log("Ocorreu uma exceção");
                }
            }

        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        doBeforeProcessing(request, response);

        Throwable problem = null;
        if (contains(((HttpServletRequest) request).getRequestURI(), new String[]{"css", "js", "img", "vendor", "fonts","Upload"})) {
            try {
                chain.doFilter(request, response);
            } catch (Throwable t) {
                problem = t;
                t.printStackTrace();
            }
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {
                log("URLDispatcher:Initializing filter");
            }
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("URLDispatcher()");
        }
        StringBuffer sb = new StringBuffer("URLDispatcher(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    private String getPageRequest(HttpServletRequest req) {
        String[] pos = req.getRequestURI().split("/");

        if (req.getParameter("insideSystem") != null) {
            if (pos.length <= 2) {
                return "Sistema";
            }
            if (pos.length > 2) {
                return pos[2];
            }
        } else {
            if (req.getSession().getAttribute("logado") == null) {
                return "Login";
            } else {
                return "Sistema";
            }
        }

        /*if (pos.length <= 2 && req.getSession().getAttribute("logado") == null) {
            return "Login";
        } else if (pos.length <= 2 && req.getSession().getAttribute("logado") != null) {
            return "Sistema";
        } else if (pos.length == 3 && req.getParameter("insideSystem") != null) {
            return pos[2];
        } else if (pos.length == 4 && req.getParameter("insideSystem") != null) {
            req.setAttribute("action", pos[3]);
            return pos[2];
        } else if (req.getParameter("insideSystem") == null && req.getSession().getAttribute("logado") != null) {
            return "Sistema";
        } else if (pos.length == 5 && req.getParameter("insideSystem") != null) {
            req.setAttribute("action", pos[3]);
            req.setAttribute("id", pos[4]);
            return pos[2];
        }*/
        return "Login";
    }

    private String getAction(HttpServletRequest req) {
        if (req.getParameter("action") != null) {
            return req.getParameter("action");
        } else if (req.getAttribute("action") != null) {
            return req.getAttribute("action").toString();
        } else {
            return "execute";
        }
    }

}
