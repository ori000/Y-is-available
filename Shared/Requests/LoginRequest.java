package Shared.Requests;

import java.io.Serializable;

public class LoginRequest implements Serializable{
    private String email;
    private String password;

    public LoginRequest(String username, String password) {
        this.email = username != null ? username : "";
        this.password = password != null ? password : "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}