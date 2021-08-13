package com.quick.receivingtransaction;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.util.HashMap;

class ManagerSessionUserOracle {
    private SharedPreferences pref;
    private static final String PREF_NAME = "session";
    private final String KEY_IS_LOGIN = "isLogin";

    final String KEY_USERNAME = "username";
    final String KEY_PASSWORD = "password";
    final String KEY_USEID = "user_id";

    final String KEY_cName = "cName";
    final String KEY_cPort = "cPort";
    final String KEY_cSid = "cSid";
    final String KEY_cUsername = "cUsername";
    final String KEY_cPassword = "cPassword";

    Context mContext;

    ManagerSessionUserOracle(Context context){
        mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME,0);//PrivateMode
    }

    void createUserSession(String username, String password, String userid,
                           String cName, String cPort, String cSid, String cUsername, String cPassword){
        SharedPreferences.Editor edit;
        edit = pref.edit();

        edit.putBoolean(KEY_IS_LOGIN,true);
        edit.putString(KEY_USERNAME, username);
        edit.putString(KEY_PASSWORD, password);
        edit.putString(KEY_USEID, userid);

        edit.putString(KEY_cName, cName);
        edit.putString(KEY_cPort, cPort);
        edit.putString(KEY_cSid, cSid);
        edit.putString(KEY_cUsername, cUsername);
        edit.putString(KEY_cPassword, cPassword);

        edit.apply();
    }

    HashMap<String, String> getUserData(){
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_USERNAME, pref.getString(KEY_USERNAME,null));
        userData.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD,null));
        userData.put(KEY_USEID, pref.getString(KEY_USEID,null));

        userData.put(KEY_cName, pref.getString(KEY_cName,null));
        userData.put(KEY_cPort, pref.getString(KEY_cPort,null));
        userData.put(KEY_cSid, pref.getString(KEY_cSid,null));
        userData.put(KEY_cUsername, pref.getString(KEY_cUsername,null));
        userData.put(KEY_cPassword, pref.getString(KEY_cPassword,null));

        return userData;
    }

    String getSID(){
        return pref.getString(KEY_cSid,"");
    }

    String getUser(){
        return pref.getString(KEY_USERNAME,"");
    }

    String getUserId()
    {
        return pref.getString(KEY_USEID,null);
    }

    Boolean isUserLogin(){
        return pref.getBoolean(KEY_IS_LOGIN,false);
    }

    public void logoutUser(){
        pref.edit().clear().apply();
    }

    Connection connectDb(){
        HashMap<String, String> userData = getUserData();
        String cName = userData.get(KEY_cName);
        String cPort = userData.get(KEY_cPort);
        String cSid = userData.get(KEY_cSid);
        String cUsername = userData.get(KEY_cUsername);
        String cPassword = userData.get(KEY_cPassword);

        return new Koneksi().getConnection(cName,cPort,cSid,cUsername,cPassword);
    }
}
