package com.myapp.jlam.motivationalbot.database;

import android.content.Context;
import com.myapp.jlam.motivationalbot.InAppDatabase;

/*
This class stores and retrieves the current's user info into the app's database.
*/
public class UserInfoDatabase
{
    Context context;
    InAppDatabase inAppDatabase_userInfo;

    /*
    display name
    first name
    email
    image_URL
     */

    public UserInfoDatabase(Context c)
    {
        context = c;
        inAppDatabase_userInfo = new InAppDatabase(context, "ULnfJbcMdjZfoh8GbxMshJ");
    }


    public String getDisplayName()
    {
        return inAppDatabase_userInfo.getData("displayName");
    }

    public void setDisplayName(String displayName)
    {
        inAppDatabase_userInfo.storeData("displayName", displayName);
    }

    public String getFirstName()
    {
        return inAppDatabase_userInfo.getData("firstName");
    }

    public void setFirstName(String firstName)
    {
        inAppDatabase_userInfo.storeData("firstName", firstName);
    }


    public String getEmail()
    {
        return inAppDatabase_userInfo.getData("email");
    }

    public void setEmail(String email)
    {
        inAppDatabase_userInfo.storeData("email", email);
    }



    public void setImageURL(String imageURL)
    {
        inAppDatabase_userInfo.storeData("imageURL", imageURL);
    }

    public String getProfileImageURL()
    {
        return inAppDatabase_userInfo.getData("profile_image_URL");
    }


}
