package net.gshp.observatoriociudadano.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import net.gshp.observatoriociudadano.contextApp.ContextApp;

/**
 * Created by gnu on 28/02/18.
 */

public class SharePreferenceCustom {

    public static void write(String nameFile, String nameKey, String nameValue) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        preferences.edit().putString(encrypt(nameKey), encrypt(nameValue)).apply();
    }

    public static void write(int idResourceNameFile, int idResourceNameKey, int idResourceNameValue) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(ContextApp.context.getString(idResourceNameFile), Context.MODE_PRIVATE);
        preferences.edit().putString(encrypt(ContextApp.context.getString(idResourceNameKey)), encrypt(ContextApp.context.getString(idResourceNameValue))).apply();
    }

    public static void write(int idResourceNameFile, int idResourceNameKey, String Value) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(ContextApp.context.getString(idResourceNameFile), Context.MODE_PRIVATE);
        preferences.edit().putString(encrypt(ContextApp.context.getString(idResourceNameKey)), encrypt(Value)).apply();
    }

    public static String read(String nameFile, String nameKey, String nameDefault) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        String passEncrypted = preferences.getString(encrypt(nameKey), nameDefault);
        return passEncrypted != null ? decrypt(passEncrypted) : null;
    }

    public static String read(int idResourceNameFile, int idResourceNameKey, String nameDefault) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(ContextApp.context.getString(idResourceNameFile), Context.MODE_PRIVATE);
        String passEncrypted = preferences.getString(encrypt(ContextApp.context.getString(idResourceNameKey)), null);
        Log.e("pass", "passs " + passEncrypted);
        return passEncrypted != null ? decrypt(passEncrypted) : nameDefault;
    }

    public static boolean contains(int idResourceNameFile, int idResourceNameKey) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(ContextApp.context.getString(idResourceNameFile), Context.MODE_PRIVATE);
        return preferences.contains(encrypt(ContextApp.context.getString(idResourceNameKey)));
    }

    public static void remove(int idResourceNameFile, int idResourceNameKey) {
        SharedPreferences preferences = ContextApp.context.getSharedPreferences(ContextApp.context.getString(idResourceNameFile), Context.MODE_PRIVATE);
        preferences.edit().remove(encrypt(ContextApp.context.getString(idResourceNameKey))).apply();
    }


    private static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    private static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
