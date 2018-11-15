package kr.idealidea.phonebook.utils;

import android.content.Context;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

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

    public static void setRecentNumArrayString(Context context, String num) {
        String numString = ContextUtils.getRecentSearchNum(context);

        if (numString.contains("/")) {
            if (numString.contains(num)) {
                if (numString.contains(num + "/")) {
                    numString = numString.replace(num + "/", "");
                } else {
                    numString = numString.replace("/" + num, "");
                }
            }
            numString = numString + "/" + num;
        } else if (numString.equals("")) {
            numString = num;
        } else {
            numString = numString + "/" + num;
        }

        if (numString.contains("//")) {
            numString = numString.replace("//", "/");
        }

        ContextUtils.setRecentSearchNum(context, numString);
    }

    public static List<String> getRecentNumList(Context context) {
        List<String> numList = new ArrayList<>();

        String numString = ContextUtils.getRecentSearchNum(context);
        if (numString.contains("//")) {
            numString = numString.replace("//", "/");
        }
        Log.d("numString", numString);

        if (numString.contains("/")) {
            String[] numArray = numString.split("/");
            for (int i = numArray.length - 1; i >= 0; i--) {
                numList.add(numArray[i]);
            }
        } else {
            numList.add(numString);
        }

        return numList;
    }

    public static String makePhoneNumber(String phoneNumber) {
        String regEx = "(\\d{2,3})(\\d{3,4})(\\d{4})";

        if(!Pattern.matches(regEx, phoneNumber)) return phoneNumber;

        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }
}
