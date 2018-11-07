package kr.idealidea.phonebook.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ContextUtils {
    private final static String prefName = "PhoneBook";

    private final static String USER_TOKEN = "USER_TOKEN";
    private final static String START_APP = "START_APP";

    private final static String LAST_SAVE_DATE = "LAST_SAVE_DATE";
    private final static String RECENT_SEARCH_NUM = "RECENT_SEARCH_NUM";

    public static void setUserToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        pref.edit().putString(USER_TOKEN, token).commit();
    }

    public static String getUserToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString(USER_TOKEN, "");
    }

    public static void setFirstStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        pref.edit().putString(START_APP, "1").commit();
    }

    public static String isFirstStart(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString(START_APP, "");
    }


    public static void setLastSaveDate(Context context, Long milliTime) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        pref.edit().putLong(LAST_SAVE_DATE, milliTime).commit();
    }

    public static Long getLastSaveDate(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getLong(LAST_SAVE_DATE, 0);
    }

    public static void setRecentSearchNum(Context context, String numArray) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        pref.edit().putString(RECENT_SEARCH_NUM, numArray).commit();
    }

    public static String getRecentSearchNum(Context context) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString(RECENT_SEARCH_NUM, "");
    }
}
