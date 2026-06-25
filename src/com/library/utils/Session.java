package com.library.utils;

import com.library.services.DTOs.AdminListDTO;

public class Session {
    private static AdminListDTO currentAdmin;

    public static void set(AdminListDTO admin){
        currentAdmin = admin;
    }

    public static AdminListDTO get(){
        return currentAdmin;
    }

    public static void clear(){
        currentAdmin = null;
    }

    public static boolean isLoggedIn(){
        return currentAdmin != null;
    }
}
