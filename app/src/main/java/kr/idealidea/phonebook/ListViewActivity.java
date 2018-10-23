package kr.idealidea.phonebook;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.idealidea.phonebook.utils.ConnectServer;

public class ListViewActivity extends AppCompatActivity {
    public static final String MESSAGE_TYPE_INBOX = "1";
    public static final String MESSAGE_TYPE_SENT = "2";
    public static final String MESSAGE_TYPE_CONVERSATIONS = "3";
    public static final String MESSAGE_TYPE_NEW = "new";

    final static private String[] CALL_PROJECTION = { CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,        CallLog.Calls.DURATION };

    private static final String TAG = "Victor-Manage_Clique";

    List<String> callLogs = new ArrayList<>();

    String intent = "";
    LinearLayout layoutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        intent = getIntent().getStringExtra("intent");
        layoutListView = findViewById(R.id.layoutListView);

        if (intent.equals("book")) {
            contacts();
        } else if (intent.equals("log")) {
            callLog();
        } else if (intent.equals("message")) {
            readSMSMessage();
        }
    }

    public void putCallLogs() {
        ConnectServer.putRequestCallLog(this, callLogs, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {

            }
        });
    }

    public void putMessage() {
        ConnectServer.putRequestMessage(this, callLogs, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {

            }
        });
    }

    public void putContact() {
        ConnectServer.putRequestContacts(this, callLogs, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {

            }
        });
    }

    /**
     * 주소록 정보 가져오기.
     */
    public void contacts(){
        Cursor cursor = managedQuery(
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

        final LayoutInflater inf = LayoutInflater.from(this);
        layoutListView.removeAllViews();
        callLogs.clear();

        while (cursor.moveToNext()){
            try {
                String v_id = cursor.getString(0);
                String v_display_name = cursor.getString(1);
                String v_phone = contactsPhone(v_id);
                String updateTime = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                System.out.println("id = " + v_id);
                System.out.println("display_name = " + v_display_name);
                System.out.println("phone = " + v_phone);
                System.out.println("updateTime = " + updateTime);

                String contact = v_display_name + "|" + v_phone + "|" + updateTime;
                callLogs.add(contact);

                final View v = inf.inflate(R.layout.row_phone_book, null);
                TextView txtvName = v.findViewById(R.id.txtvName);
                TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);
                TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);

                txtvName.setText(v_display_name);
                txtvPhoneNum.setText(v_phone);
                txtvTimeStamp.setText(timeToString(Long.parseLong(updateTime)));

                layoutListView.addView(v);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        putContact();
        cursor.close();
    }

    /**
     * 주소록 상세정보(전화번호) 가져오기.
     */
    public String contactsPhone(String p_id){
        String reuslt = null;

        if(p_id==null || p_id.trim().equals("")){
            return reuslt;
        }

        Cursor cursor = managedQuery(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (cursor.moveToNext()){
            try {
                reuslt = cursor.getString(0);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();

        return reuslt;
    }

    public int readSMSMessage() {
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(allMessage, new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, "date DESC");

        String string = "";
        int count = 0;

        final LayoutInflater inf = LayoutInflater.from(this);
        layoutListView.removeAllViews();
        callLogs.clear();

        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);

            string = String.format("address:%s, timestamp:%d, body:%s", address, timestamp, body);

            Log.d("heylee", string);

            String contact = address + "|" + body + "|" + timeToString(timestamp);
            callLogs.add(contact);

            final View v = inf.inflate(R.layout.row_phone_book, null);
            TextView txtvName = v.findViewById(R.id.txtvName);
            TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);
            TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);
            TextView txtvContent = v.findViewById(R.id.txtvContent);

            txtvContent.setVisibility(View.VISIBLE);

            txtvName.setText(address);
            txtvPhoneNum.setText(address);
            txtvTimeStamp.setText(timeToString(timestamp));
            txtvContent.setText(body);

            layoutListView.addView(v);
        }
        putMessage();
        return 0;
    }

    private Cursor getCallHistoryCursor(Context context) {
        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        return cursor;
    }

    private void callLog() {
        int callcount = 0;
        String callname = "";
        String calltype = "";
        String calllog = "";
        String callDuration = "";
        Cursor curCallLog = getCallHistoryCursor(this);
        Log.i( TAG , "processSend() - 1");
        // Log.i( TAG , "curCallLog: " + curCallLog.getCount());
        if (curCallLog.moveToFirst() && curCallLog.getCount() > 0) {

            try {
                final LayoutInflater inf = LayoutInflater.from(this);
                layoutListView.removeAllViews();
                callLogs.clear();
                int i = 0;

                while (i < 499) {
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

                    if (curCallLog.getString(curCallLog
                            .getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
                        callname = "NoName";
                    } else {
                        callname = curCallLog.getString(curCallLog
                                .getColumnIndex(CallLog.Calls.CACHED_NAME));
                    }

                    callDuration = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.DURATION));

                    sb.append(timeToString(curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE))));
                    sb.append("\t").append(calltype);
                    sb.append("\t").append(callname);
                    sb.append("\t").append(curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.NUMBER)));
                    sb.append("\t").append(callDuration);
                    curCallLog.moveToNext();

                    String backupData = sb.toString();

                    callcount++;
                    Log.i("call history[", sb.toString());

                    String phone = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.NUMBER));
                    String createTime = timeToString(curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE)));

                    String list = callname + "|" + calltype + "|" + phone + "|" + getStringTime(Integer.parseInt(callDuration)) + "|" + createTime;
                    callLogs.add(list);

                    final View v = inf.inflate(R.layout.row_phone_book, null);
                    TextView txtvName = v.findViewById(R.id.txtvName);
                    TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);
                    TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);
                    TextView txtvDuration = v.findViewById(R.id.txtvDuration);

                    txtvDuration.setVisibility(View.VISIBLE);
                    txtvName.setText(callname + " (" + calltype + ")");
                    txtvPhoneNum.setText(phone);
                    txtvTimeStamp.setText(timeToString(curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE))));
                    txtvDuration.setText(getStringTime(Integer.parseInt(callDuration)));

                    layoutListView.addView(v);
                    i++;
                }
                putCallLogs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String timeToString(Long time) {
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
    }}
