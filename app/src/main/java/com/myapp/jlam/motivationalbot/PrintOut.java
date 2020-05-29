package com.myapp.jlam.motivationalbot;

import android.content.Context;
import android.util.Log;

public class PrintOut
{
    private Context context;

    public PrintOut(Context context)
    {
        this.context = context;
    }

    public void printThis(String message)
    {
        Log.d(context.toString() + ":::::::::::", message);
       // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
