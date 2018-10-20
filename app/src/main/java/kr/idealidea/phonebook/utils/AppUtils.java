package kr.idealidea.phonebook.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
}
