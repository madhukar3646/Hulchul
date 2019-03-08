package com.app.hulchul.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sine90 on 9/8/2017.
 */

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "hulchul";
    public static final String USERID="userid";
    public static final String NAME="name";
    public static final String DEVICETOKEN="devicetoken";
    public static final String DEVICETYPE="devicetype";
    public static final String LOGIN_TYPE="logintype";
    public static final String EMAIL="email";
    public static final String MOBILE="mobile";
    public static final String ISLOGIN="islogin";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setValuetoPreference(String key, String value)
    {
        editor.putString(key,value);
        editor.commit();
    }

    public String getValueFromPreference(String key)
    {
        return pref.getString(key,null);
    }

    public void setBooleanValuetoPreference(String key, boolean value)
    {
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBooleanValueFromPreference(String key)
    {
        return pref.getBoolean(key,false);
    }
    public void setIntegerValuetoPreference(String key, int value)
    {
        editor.putInt(key,value);
        editor.commit();
    }

    public int getIntegerValueFromPreference(String key)
    {
        return pref.getInt(key,0);
    }

    public void logoutUser(){

        editor.remove(USERID);
        editor.remove(ISLOGIN);
        editor.commit();
        /*String devicetoken=getValueFromPreference(SessionManagement.DEVICETOKEN);
        editor.clear();
        editor.commit();
        setValuetoPreference(SessionManagement.DEVICETOKEN,devicetoken);*/
    }
}
