package servlets;

import accounts.UserProfile;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.util.Properties;




import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class Config implements ServletContextListener {
    private static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            String url = getDatabaseProperties("h_url");
            String login = getDatabaseProperties("login");
            String password = getDatabaseProperties("password");

            if (sessionFactory == null) {
                try {
                    Configuration configuration = new Configuration();

                    Properties settings = new Properties();
                    settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                    settings.put(Environment.URL, url);
                    settings.put(Environment.USER, login);
                    settings.put(Environment.PASS, password);
                    settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                    settings.put(Environment.SHOW_SQL, "true");
                    settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                    configuration.setProperties(settings);
                    configuration.addAnnotatedClass(UserProfile.class);

                    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties()).build();

                    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public String getDatabaseProperties(String item) throws IOException {
        File file = new File("/home/filemanager/database.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        return properties.getProperty(item);
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}