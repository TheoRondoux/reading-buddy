package com.example.myonlinebookself;

import android.app.Application;
import android.content.Context;

public class MOBApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate(){
        super.onCreate();

        // Keep a reference to the application context
        sContext = getApplicationContext();
    }

    // Used to access Context anywhere within the app
    public static Context getContext() {
        return sContext;
    }

}

