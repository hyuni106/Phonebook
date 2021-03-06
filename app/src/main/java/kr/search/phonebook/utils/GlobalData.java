package kr.search.phonebook.utils;

import android.provider.CallLog;

import kr.search.phonebook.data.Period;
import kr.search.phonebook.data.User;

public class GlobalData {
    public static User loginUser = new User();
    public static Period userPeriod = new Period();

    final static public String[] CALL_PROJECTION = { CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,        CallLog.Calls.DURATION };

}
