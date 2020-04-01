package com.example.unittesting2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

////Retrieve Application Context Anywhere In Java Class.
//Then you can use below code to get this android app context object anywhere in your java util class and use it when you need.


public class DefaultContext extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    public static Context getAppContext() {
        return appContext;
    }






}
