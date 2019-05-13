package hillel.calc.mailer;

public class ConfMailer {
    private String host;
    private String port;
    private String user;
    private String password;
    private boolean useSSL;

    public ConfMailer(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public ConfMailer(String host, String port, String user, String password, boolean useSSL) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.useSSL = useSSL;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseSSL() {
        return useSSL;
    }
}
