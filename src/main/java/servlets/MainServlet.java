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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class MainServlet extends HttpServlet {
    private AccountService accountService = ServiceManager.accounts;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseURL = getBaseURL(req);


        //user controller
        Cookie[] cookies = req.getCookies();
        String cookieName = "idSession";
        Cookie cookie = null;
        if(cookies !=null) {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        UserProfile currentUser;

        try{
            currentUser = accountService.getUserBySessionId(cookie.getValue());
        }catch (NullPointerException e){
            resp.sendRedirect(baseURL + "signin");
            return;
        }

        if(req.getRequestURI().equals(req.getContextPath() + "/logout")){
            accountService.deleteSession(cookie.getValue());
            resp.sendRedirect(baseURL + "signin");
            return;
        }


        Map<String, String[]> params = req.getParameterMap();
        final File path = new File(params.get("path")[0]);
        MyFile[] folders = listFoldersForFolder(path);
        MyFile[] files = listFilesForFolder(path);
        if(! path.getPath().toString().startsWith("/home/filemanager/" + currentUser.getLogin())){
            resp.sendRedirect(baseURL + "file?path=/home/filemanager/" + currentUser.getLogin());
            return;
        }


        //

        //current date
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        req.setAttribute("currentDate", dateFormat.format(date).toString());



        req.setAttribute("files", files);
        req.setAttribute("folders", folders);
        req.setAttribute("currentUrl",baseURL + "file?path=" + path.toString().replace('\\', '/'));
        req.setAttribute("currentPath",path.toString());
        req.setAttribute("downloadUrl",baseURL + "download/");
        req.setAttribute("url",baseURL);
        req.setAttribute("parentPath", path.getParent().replace('\\', '/'));
        req.getRequestDispatcher("MainPage.jsp").forward(req, resp);
    }

    public String getBaseURL(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        return "http://" + requestURL.getHost() + ":" + requestURL.getPort() +
                request.getContextPath() + "/";
    }

    public MyFile[] listFilesForFolder(final File path) throws IOException {
        ArrayList<MyFile> allFiles = new ArrayList<>();
        File[] files = path.listFiles();
        if(files != null && files.length != 0){
            for(File file : files){
                if(!file.isDirectory()){
                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    allFiles.add(new MyFile(file.getName(), df.format(attr.creationTime().toMillis()),
                            Long.toString(attr.size()), file.getPath()));
                }
            }
        }
        return allFiles.toArray(new MyFile[0]);
    }

    public MyFile[] listFoldersForFolder(final File path) throws IOException {
        File[] files = path.listFiles();
        ArrayList<MyFile> allFolders = new ArrayList<>();
        if(files != null && files.length != 0){
            for(File file : files){
                if(file.isDirectory()){
                    BasicFileAttributes attr = Files.readAttributes(path.toPath(), BasicFileAttributes.class);
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    allFolders.add(new MyFile(file.getName(), df.format(attr.creationTime().toMillis()),
                            "___", file.getPath()));
                }
            }
        }
        return allFolders.toArray(new MyFile[0]);
    }

    public class MyFile{
        private String Name;
        private String CreationTime;
        private String Size;
        private String Path;
        public MyFile(String name, String creationTime, String size, String path){
            this.Name = name;
            this.CreationTime = creationTime;
            this.Size = size;
            this.Path = path;
        }

        public String getName() {
            return Name;
        }

        public String getCreationTime() {
            return CreationTime;
        }

        public String getSize() {
            return Size;
        }

        public String getPath() {
            return Path;
        }
    }
}