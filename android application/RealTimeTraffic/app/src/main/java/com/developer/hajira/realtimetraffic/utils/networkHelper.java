package com.developer.hajira.realtimetraffic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class networkHelper {
	
    public static boolean isNetworkAvailable(Context context){
		
		
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            @SuppressLint("MissingPermission") NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }catch(Exception e){
           e.printStackTrace();
           return false;
        }
    }
}
