package servlets;

import accounts.AccountService;
import accounts.ServiceManager;
import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService = ServiceManager.accounts;

    //get logged user profile
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/signup.html").forward(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String email = request.getParameter("email");

        if(login.length() == 0 || pass.length() == 0 || email.length() == 0){
            getServletContext().getRequestDispatcher("/signup.html").forward(request, response);
            return;
        }

        File userDir = new File("/home/filemanager/"+login);
        boolean isCreated = userDir.mkdir();

        try {
            if(accountService.getUserByLogin(login) == null && isCreated){
                UserProfile user = new UserProfile(login, pass, email);
                try {
                    accountService.addNewUser(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                accountService.addSession(request.getSession().getId(), accountService.getUserByLogin(user.getLogin()));

                //set coockie
                Cookie cookie = new Cookie("idSession", request.getSession().getId());
                cookie.setMaxAge(-1);
                response.addCookie(cookie);

                response.sendRedirect(request.getContextPath() + "/file?path=/home/filemanager/" + user.getLogin());
            }
            else{
                getServletContext().getRequestDispatcher("/signup.html").forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}