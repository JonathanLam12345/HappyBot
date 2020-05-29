package com.myapp.jlam.motivationalbot;

import com.google.firebase.database.IgnoreExtraProperties;

// https://firebase.google.com/docs/database/android/read-and-write

@IgnoreExtraProperties
public class UserProfile
{
    public String displayName;
    public String email;
    public String image;

    public UserProfile()
    {
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getEmail()
    {
        return email;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getImage()
    {
        return image;
    }


}
