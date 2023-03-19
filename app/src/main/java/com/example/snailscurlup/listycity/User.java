package com.example.snailscurlup.listycity;

public class User {
    private String username;
    private String email;
    private String phone;


    User(String username, String email, String phone){
        this.username=username;
        this.email=email;
        this.phone=phone;
    }

    String getUsername(){
        return this.username;
    }
    String getEmail(){
        return this.email;
    }
    String getPhone(){
        return this.phone;
    }
}
