package vay.enterwind.auto2000samarinda.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pubnub.api.PubNub;

import java.util.Arrays;
import java.util.HashMap;

import vay.enterwind.auto2000samarinda.LoginActivity;
import vay.enterwind.auto2000samarinda.pubnub.Constants;

/**
 * Created by novay on 04/03/18.
 */

public class AuthManagement {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPref";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AKSES = "akses";

    public static final String KEY_TELEPON = "telepon";
    public static final String KEY_JABATAN = "jabatan";
    public static final String KEY_FOTO = "foto";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NPK = "npk";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_REGIONAL = "regional";
    public static final String KEY_SUPERVISOR = "supervisor";

    public AuthManagement(Context context){
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String nama, String email, String id, String akses,
                                   String telepon, String jabatan, String foto, String password,
                                   String npk, String alamat, String regional, String supervisor) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AKSES, akses);

        editor.putString(KEY_TELEPON, telepon);
        editor.putString(KEY_JABATAN, jabatan);
        editor.putString(KEY_FOTO, foto);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_NPK, npk);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_REGIONAL, regional);
        editor.putString(KEY_SUPERVISOR, supervisor);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(mContext, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NAMA, pref.getString(KEY_NAMA, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_AKSES, pref.getString(KEY_AKSES, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_NPK, pref.getString(KEY_NPK, null));
        user.put(KEY_ALAMAT, pref.getString(KEY_ALAMAT, null));
        user.put(KEY_FOTO, pref.getString(KEY_FOTO, null));
        user.put(KEY_JABATAN, pref.getString(KEY_JABATAN, null));
        user.put(KEY_TELEPON, pref.getString(KEY_TELEPON, null));
        user.put(KEY_REGIONAL, pref.getString(KEY_REGIONAL, null));
        user.put(KEY_SUPERVISOR, pref.getString(KEY_SUPERVISOR, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void updateProfil(String nama, String telepon, String npk, String alamat) {
        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_TELEPON, telepon);
        editor.putString(KEY_NPK, npk);
        editor.putString(KEY_ALAMAT, alamat);
        editor.commit();
    }

    public void updatePassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

}
