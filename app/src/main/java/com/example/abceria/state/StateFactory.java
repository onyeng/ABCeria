package com.example.abceria.state;

public class StateFactory {
    private static UserState userState = null;

    public static UserState getUserStateInstance(){
        if(userState == null){
            userState = new UserState();
        }
        return userState;
    }

    public static void destroy(){
        userState = null;
    }
}
