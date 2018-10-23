package kr.idealidea.phonebook.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ContextUtils {
    private final static String prefName = "PhoneBook";

    private final static String USER_TOKEN = "USER_TOKEN";

    public static void setUserToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        pref.edit().putString(USER_TOKEN, token).commit();
    }

    public static String getUserToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString(USER_TOKEN, "");
    }
}
