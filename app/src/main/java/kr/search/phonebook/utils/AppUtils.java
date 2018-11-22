package kr.search.phonebook.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import kr.search.phonebook.data.Recent;

public class AppUtils {
    /**
     * 밀리초를 시간문자열 반환
     * @param mills   : 밀리초
     * @param pattern  : 패턴 문자열. "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String millis2Time(long mills, String pattern){
        String result = null;

        if(pattern==null || pattern.trim().equals("")){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        if(mills > 0){
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
            result = (String) formatter.format(new Timestamp(mills));
        }

        return result;
    }

    /**
     * 밀리초를 시간문자열 반환
     * @param mills   : 밀리초
     * @return
     */
    public static String millis2Time(long mills){
        return millis2Time(mills, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 날짜 문자열을 밀리초로 반환.
     * @param date   : "2017-01-01 01:01:01"
     * @return
     */
    public static long Date2Mill(String date) {
        return Date2Mill(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 날짜 문자열을 밀리초로 반환.
     * @param date   : "2017-01-01 01:01:01"
     * @param pattern  : "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long Date2Mill(String date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
        Date trans_date = null;

        if(pattern==null || pattern.trim().equals("")){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trans_date.getTime();
    }


    public static String timeToString(Long time) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleFormat.format(new Date(time));
        return date;
    }

    static public String getStringTime(int time) {
        String res = null;
        int hour = 0, min = 0, sec = 0;
        if (time > 0) {
            hour = time / 3600;
            time %= 3600;
            min = time / 60;
            sec = time % 60;
        }
        res = String.format("%02d:%02d:%02d", hour, min, sec);
        return res;
    }

    public static void setRecentNumArrayString(Context context, Recent newData) {
        String numString = ContextUtils.getRecentSearchNum(context);

        List<Recent> recents = new ArrayList<>();
        JSONObject list = makeJsonObject(numString);
        if (list != null) {
            try {
                JSONArray data = list.getJSONArray("jsonArray");

                for (int i = data.length() - 1; i >= 0; i--) {
                    Recent r = Recent.getRecentFromJson(data.getJSONObject(i));
                    recents.add(r);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!numString.contains(newData.getNum())) {
            recents.add(0, newData);
        }
        String jsonString = makeDeliveryUrlJsonObject(recents);
        Log.d("jsonArray", jsonString);

        ContextUtils.setRecentSearchNum(context, jsonString);
    }

    public static List<Recent> getRecentNumList(Context context) {
        String numString = ContextUtils.getRecentSearchNum(context);
        List<Recent> recents = new ArrayList<>();
        JSONObject list = makeJsonObject(numString);
        if (list != null) {
            Log.d("jsonArray", list.toString());
            try {
                JSONArray data = list.getJSONArray("jsonArray");

                for (int i = data.length() - 1; i >= 0; i--) {
                    Recent r = Recent.getRecentFromJson(data.getJSONObject(i));
                    recents.add(r);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recents;
    }

    public static String makeDeliveryUrlJsonObject(List<Recent> list) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();
            for (int i = list.size() - 1; i >= 0; i--) {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("num", list.get(i).getNum());
                sObject.put("name", list.get(i).getName());
                sObject.put("shop_name", list.get(i).getShop_name());
                sObject.put("count", list.get(i).getCount());
                jArray.put(sObject);
            }
            obj.put("jsonArray", jArray);//배열을 넣음

            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static JSONObject makeJsonObject(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String makePhoneNumber(String phoneNumber) {
        String regEx = "(\\d{2,3})(\\d{3,4})(\\d{4})";

        if(!Pattern.matches(regEx, phoneNumber)) return phoneNumber;

        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }
}
