package servlets;

import accounts.AccountService;
import accounts.ServiceManager;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class SignInServlet extends HttpServlet {
    private AccountService accountService = ServiceManager.accounts;

    //get logged user profile
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        String cookieName = "idSession";
        if(cookies !=null) {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        String baseURL = getBaseURL(request);
        UserProfile currentUser;
        try{
            currentUser = accountService.getUserBySessionId(cookie.getValue());
        }catch (NullPointerException e){
            getServletContext().getRequestDispatcher("/signin.html").forward(request, response);
            return;
        }
        if(currentUser == null)
        {
            getServletContext().getRequestDispatcher("/signin.html").forward(request, response);
            return;
        }
        response.sendRedirect(baseURL + "file?path=/home/filemanager/" + currentUser.getLogin());
    }

    //sign in
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String baseURL = getBaseURL(request);

        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //TODO
            return;
        }
        UserProfile currentUser;
        try {
            currentUser = accountService.getUserByLogin(login);
        }catch (NullPointerException e){
            response.sendRedirect(baseURL + "signin");
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (currentUser == null || !currentUser.getPass().equals(pass)) {
            response.sendRedirect(baseURL + "signin");
        }
        else{
            Cookie cookie = new Cookie("idSession", request.getSession().getId());
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            accountService.addSession(request.getSession().getId() ,currentUser);
            response.sendRedirect(baseURL + "file?path=/home/filemanager/" + currentUser.getLogin());
        }
    }

    //sign out
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Goodbye!");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

    public String getBaseURL(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        return "http://" + requestURL.getHost() + ":" + requestURL.getPort() +
                request.getContextPath() + "/";
    }
}