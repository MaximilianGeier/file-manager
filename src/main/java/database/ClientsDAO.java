package database;

import accounts.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import servlets.Config;

public class ClientsDAO {
    public void saveProfile(UserProfile profile) {
        try {
            Session session = Config.getSession();
            Transaction transaction = session.beginTransaction();
            long id = (Long)session.save(profile);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public UserProfile getProfile(String login) {
        Session session = Config.getSession();
        Criteria criteria = session.createCriteria(UserProfile.class);
        UserProfile profile = ((UserProfile) criteria.add(Restrictions.eq("login", login)).uniqueResult());
        return profile;
    }
}
