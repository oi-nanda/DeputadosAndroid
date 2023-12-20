package com.example.trabalhoandroid;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigBD {
    private static FirebaseAuth auth;

    public static FirebaseAuth FirebaseAutenticacao(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}