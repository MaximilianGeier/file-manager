package accounts;

import servlets.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final Map<String, UserProfile> sessionIdToProfile;

    public AccountService() {
        sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(UserProfile userProfile) throws SQLException {
        System.out.println("добавление пользователя");
        Config.doUpdate("insert into manager values('" + userProfile.getLogin() +
                "', '" + userProfile.getPass() + "', '" + userProfile.getEmail() + "');");
    }

    public UserProfile getUserByLogin(String login) throws SQLException {
        UserProfile user = null;
        ResultSet resultSet = null;
        try {
            resultSet = Config.doSelect("select * from manager where login = '" + login +"';");
        }catch (SQLException e){

        }
        while(true){
            if (!resultSet.next()) break;
            try {
                user = new UserProfile(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
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