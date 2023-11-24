public class Env {

    private String username;
    private String password ;
    private int port;
    private String host ;
    private String dbName;

    private String url;


    public Env() {
        username = "root";
        password = "root";
        host = "127.0.0.1";
        port = 3306;
        dbName = "yApp";
        url = "jdbc:mysql://" + host + ":" + port + "/";
    }

    public String getPassword(){
        return this.password;
    }

    public String getUrl(){
        return this.url;
    }

    public String getUsername(){
        return this.username;
    }

    public String getDbName(){
        return this.dbName;
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }    


}
