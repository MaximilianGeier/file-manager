import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        final File path = new File(params.get("path")[0]);
        MyFile[] folders = listFoldersForFolder(path);
        MyFile[] files = listFilesForFolder(path);
        //current date
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        req.setAttribute("currentDate", dateFormat.format(date).toString());

        req.setAttribute("files", files);
        req.setAttribute("folders", folders);
        req.setAttribute("currentUrl",req.getRequestURI() + "?path=" + path.toString());
        req.setAttribute("currentPath",path.toString());
        req.setAttribute("downloadUrl","http://localhost:8080" + "/web-application/download/");
        req.setAttribute("url","http://localhost:8080" + req.getRequestURI());
        req.setAttribute("parentPath", path.getParent());
        req.getRequestDispatcher("MainPage.jsp").forward(req, resp);
    }

    public MyFile[] listFilesForFolder(final File path) throws IOException {
        ArrayList<MyFile> allFiles = new ArrayList<>();
        File[] files = path.listFiles();
        for(File file : files){
            if(!file.isDirectory()){
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                allFiles.add(new MyFile(file.getName(), df.format(attr.creationTime().toMillis()),
                        Long.toString(attr.size()), file.getPath()));
            }
        }
        return allFiles.toArray(new MyFile[0]);
    }

    public MyFile[] listFoldersForFolder(final File path) throws IOException {
        File[] files = path.listFiles();
        ArrayList<MyFile> allFolders = new ArrayList<>();
        for(File file : files){
            if(file.isDirectory()){
                BasicFileAttributes attr = Files.readAttributes(path.toPath(), BasicFileAttributes.class);
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                allFolders.add(new MyFile(file.getName(), df.format(attr.creationTime().toMillis()),
                        "___", file.getPath()));
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