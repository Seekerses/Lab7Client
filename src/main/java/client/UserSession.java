package client;

public class UserSession {

    private static String login;
    private static String password;

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        UserSession.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserSession.password = password;
    }
}
