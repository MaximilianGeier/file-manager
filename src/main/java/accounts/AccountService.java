package accounts;

import database.ClientsDAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, UserProfile> sessionIdToProfile;

    public AccountService() {
        sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(UserProfile userProfile) throws SQLException {
        ClientsDAO clientsDAO = new ClientsDAO();
        clientsDAO.saveProfile(userProfile);
    }

    public UserProfile getUserByLogin(String login) throws SQLException {
        ClientsDAO clients = new ClientsDAO();
        UserProfile profile = clients.getProfile(login);
        return profile;
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}