
package ca.teyssedre.mailmap.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class TestAuthenticator extends Authenticator {
    String user;
    String pw;

    public TestAuthenticator(String username, String password) {
        super();
        this.user = username;
        this.pw = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pw);
    }
}
