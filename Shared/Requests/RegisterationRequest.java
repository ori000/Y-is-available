package Shared.Requests;

import java.io.Serializable;

public class RegisterationRequest implements Serializable{
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String password;
        private String region;
        private String phoneNumber;
    
        public RegisterationRequest(
            String firstName,
            String lastName,
            String username,
            String email,
            String password,
            String region,
            String phoneNumber
        ) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email != null ? email : "";
            this.password = password != null ? password : "";
            this.region = region;
            this.phoneNumber = phoneNumber;
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
    
        public String getPassword() {
            return password;
        }
    
        public String getRegion() {
            return region;
        }
    
        public String getPhoneNumber() {
            return phoneNumber;
        }
}
