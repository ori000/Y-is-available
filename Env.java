public class Env {
    private String password;
    private String url;
    private String username;

    public Env() {
        password = null;
        url = null;
        username = null;
    }
    
    private void setPassword(){
        this.password = "networks";
    }

    public String getPassword(){
        setPassword();
        return this.password;
    }

    private void setUrl(){
        this.url = "jdbc:mysql://127.0.0.1:3306/?user=root";
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
