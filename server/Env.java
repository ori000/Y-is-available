public class Env {
    private String password;
    private String url;
    private String username;

    private void setPassword(){
        this.password = "Arsenic2004";
    }
    public String getPassword(){
        setPassword();
        return this.password;
    }
    private void setUrl(){
        this.url = "jdbc:mysql://localhost:3306/yApp";
    }
    public String getUrl(){
        setUrl();
        return this.url;
    }
    private void setUsername(){
        this.username = "root";
    }
    public String getUsername(){
        setUsername();
        return this.username;
    }
}
