package com.myapp.jlam.motivationalbot;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.myapp.jlam.motivationalbot.database.UserInfoDatabase;

import java.util.Arrays;
import java.util.List;


public class Analytics
{

    //content_type: Name of the event
    public Analytics(Context context, String content_type, String eventName)
    {
        List<String> developersList = Arrays.asList("jonathanlam1538@gmail.com");

        FirebaseAnalytics firebaseAnalytics;
        UserInfoDatabase userInfoDatabase = new UserInfoDatabase(context);

//        if (developersList.contains(userInfoDatabase.getEmail().toLowerCase()))
//        {
//            //do nothing.
//        }
//        else
//        {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle_analytics = new Bundle();   //a dummy analytic field for testing which consist of a counter when ever the user access the main page.
        bundle_analytics.putString(FirebaseAnalytics.Param.ITEM_ID, "ITEM_ID: APP VERSION: "+ BuildConfig.VERSION_NAME);
        bundle_analytics.putString(FirebaseAnalytics.Param.ITEM_NAME, "ITEM_NAME: " + userInfoDatabase.getEmail());
        bundle_analytics.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content_type);
        firebaseAnalytics.logEvent(eventName, bundle_analytics);
        //  firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle_analytics);
//        }
    }
}
