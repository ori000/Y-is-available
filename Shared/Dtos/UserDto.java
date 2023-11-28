package Shared.Dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// UserDTO
public class UserDto implements Serializable{
    public static int port_counter = 30;

    public int userId;
    public String firstName;
    public String lastName;
    public String username;
    public String email;
    public String password;
    public String region;
    public String phoneNumber;
    public List<PostDto> posts; // Assuming a user can have multiple posts
    public int port = port_counter++;
    
    public UserDto(
        int userId,
        String firstName,
        String lastName,
        String username,
        String email,
        String password,
        String region,
        String phoneNumber,
        List<PostDto> posts
    ) {
        this.userId = userId != -1 ? userId : -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password != null ? password : "";
        this.region = region;
        this.phoneNumber = phoneNumber;
        this.posts = posts != null ? posts : new ArrayList<PostDto>();
    } // Default constructor

    //getters
    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getRegion(){
        return region;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public List<PostDto> getPosts(){
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts != null ? posts : this.posts;
    }
}