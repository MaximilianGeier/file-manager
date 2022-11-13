package accounts;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="clients")
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    public long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Column(name = "login", unique = true, updatable = false)
    private String login;
    public String getLogin() {
        return login;
    }

    @Column(name = "password")
    private String password;
    public String getPass() {
        return password;
    }

    @Column(name = "email")
    private String email;
    public String getEmail() {
        return email;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfile(){

    }

    public UserProfile(long id, String login, String password, String email) {
        this.setId(id);
        this.setLogin(login);
        this.setPass(password);
        this.setEmail(email);
    }

    public UserProfile(String login, String password, String email) {
        this.setId((long) -1);
        this.setLogin(login);
        this.setPass(password);
        this.setEmail(email);
    }
}