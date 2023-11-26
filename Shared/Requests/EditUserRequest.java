package Shared.Requests;

import java.io.Serializable;

public class EditUserRequest implements Serializable{
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String region;
    private String phoneNumber;
    private String email;

    public EditUserRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String region,
        String phoneNumber,
        String email
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.region = region;
        this.phoneNumber = phoneNumber;
        this.email = email != null ? email : "";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getRegion(){
        return region;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
