package com.myapp.jlam.motivationalbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref
{
    private Context context;

    public SharedPref(Context context)
    {
        this.context = context;
    }

    public void storeInt(String key, int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(key, MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();

        printSavedInt(key);
    }

    public int retrieveInt(String key)
    {
        SharedPreferences myPrefs;
        myPrefs = context.getSharedPreferences(key, MODE_PRIVATE);
        int integer;
        integer = myPrefs.getInt(key, 0);
        return integer;
    }


    public void storeString(String key, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(key, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

        printSavedString(key);
    }

    public String retrieveString(String key)
    {
        SharedPreferences myPrefs;
        myPrefs = context.getSharedPreferences(key, MODE_PRIVATE);
        return myPrefs.getString(key, null);
    }


    public void storeBoolean(String key, Boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(key, MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();

        printSavedBoolean(key);
    }

    public Boolean retrieveBoolean(String key)
    {
        SharedPreferences myPrefs;
        myPrefs = context.getSharedPreferences(key, MODE_PRIVATE);
        return myPrefs.getBoolean(key, false);
    }


    private void printSavedBoolean(String key)
    {
        String s = "saved: " + key + "::: " + retrieveBoolean(key);
//        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//        Log.d("printInt: ", s);
    }



    private void printSavedInt(String key)
    {
        String s = "saved: " + key + "::: " + retrieveInt(key);
//        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
      Log.d("printInt: ", s);
    }

    private void printSavedString(String key)
    {
        String s = "saved: " + key + "::: " + retrieveString(key);
       // Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        Log.d("printInt: ", s);
    }

}
