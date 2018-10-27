package kr.idealidea.phonebook;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.idealidea.phonebook.data.CallLog;
import kr.idealidea.phonebook.data.Contact;
import kr.idealidea.phonebook.data.Message;
import kr.idealidea.phonebook.utils.ConnectServer;

public class SearchActivity extends AppCompatActivity {
    String search = "";

    TextView txtvSearchPhone;
    TextView lineSearchTab1;
    TextView lineSearchTab2;
    TextView lineSearchTab3;
    FrameLayout frameSearchTab1;
    FrameLayout frameSearchTab2;
    FrameLayout frameSearchTab3;
    LinearLayout layoutSearchList;

    List<Contact> contactList = new ArrayList<>();
    List<CallLog> callLogList = new ArrayList<>();
    List<Message> messagesList = new ArrayList<>();
    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = getIntent().getStringExtra("phone").replaceAll("-", "");

        txtvSearchPhone = findViewById(R.id.txtvSearchPhone);
        frameSearchTab1 = findViewById(R.id.frameSearchTab1);
        frameSearchTab2 = findViewById(R.id.frameSearchTab2);
        frameSearchTab3 = findViewById(R.id.frameSearchTab3);
        lineSearchTab1 = findViewById(R.id.lineSearchTab1);
        lineSearchTab2 = findViewById(R.id.lineSearchTab2);
        lineSearchTab3 = findViewById(R.id.lineSearchTab3);
        layoutSearchList = findViewById(R.id.layoutSearchList);

        txtvSearchPhone.setText(search);

        frameSearchTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSearchTab1.setVisibility(View.VISIBLE);
                lineSearchTab2.setVisibility(View.GONE);
                lineSearchTab3.setVisibility(View.GONE);

                if (contactList.size() > 0) {
                    drawCallList();
                } else {
                    layoutSearchList.removeAllViews();
                    getSearchContact();
                }
            }
        });

        frameSearchTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSearchTab2.setVisibility(View.VISIBLE);
                lineSearchTab1.setVisibility(View.GONE);
                lineSearchTab3.setVisibility(View.GONE);

                if (callLogList.size() > 0) {
                    drawCallLogList();
                } else {
                    layoutSearchList.removeAllViews();
                    getSearchCallLog();
                }
            }
        });

        frameSearchTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSearchTab3.setVisibility(View.VISIBLE);
                lineSearchTab2.setVisibility(View.GONE);
                lineSearchTab1.setVisibility(View.GONE);

                if (messagesList.size() > 0) {
                    drawMessageList();
                } else {
                    layoutSearchList.removeAllViews();
                    getSearchMessage();
                }
            }
        });

        getSearchContact();
    }

    private void getSearchContact() {
        ConnectServer.getRequestAllContact(SearchActivity.this, search, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    if (json.getInt("code") == 200) {
                        contactList.clear();
                        JSONArray contacts = json.getJSONObject("data").getJSONArray("contacts");
                        for (int i=0; i<contacts.length(); i++) {
                            Contact c = Contact.getContactFromJson(contacts.getJSONObject(i));
                            contactList.add(c);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawCallList();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void drawCallList() {
        layoutSearchList.removeAllViews();
        final LayoutInflater inf = LayoutInflater.from(this);

        for (Contact contact : contactList) {
            final View v = inf.inflate(R.layout.search_contact_list_item, null);
            TextView txtvName = v.findViewById(R.id.txtvName);
            TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);
            TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);

            txtvName.setText("저장명 : " + contact.getName());
            txtvTimeStamp.setText(myDateFormat.format(contact.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

    private void getSearchCallLog() {
        ConnectServer.getRequestAllCallLogs(SearchActivity.this, search, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    if (json.getInt("code") == 200) {
                        callLogList.clear();
                        JSONArray contacts = json.getJSONObject("data").getJSONArray("call_logs");
                        for (int i=0; i<contacts.length(); i++) {
                            CallLog c = CallLog.getCallLogsFromJson(contacts.getJSONObject(i));
                            callLogList.add(c);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawCallLogList();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void drawCallLogList() {
        layoutSearchList.removeAllViews();
        final LayoutInflater inf = LayoutInflater.from(this);

        for (CallLog callLog : callLogList) {
            final View v = inf.inflate(R.layout.search_call_log_list_item, null);
            TextView txtvType = v.findViewById(R.id.txtvType);
            TextView txtvDuration = v.findViewById(R.id.txtvDuration);
            TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);
            TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);

            txtvType.setText(callLog.getLog_type());
            txtvDuration.setText(callLog.getTime());
            txtvTimeStamp.setText(myDateFormat.format(callLog.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

    private void getSearchMessage() {
        ConnectServer.getRequestAllMessage(SearchActivity.this, search, new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    if (json.getInt("code") == 200) {
                        messagesList.clear();
                        JSONArray contacts = json.getJSONObject("data").getJSONArray("messages");
                        for (int i=0; i<contacts.length(); i++) {
                            Message c = Message.getContactFromJson(contacts.getJSONObject(i));
                            messagesList.add(c);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawMessageList();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void drawMessageList() {
        layoutSearchList.removeAllViews();
        final LayoutInflater inf = LayoutInflater.from(this);

        for (Message message : messagesList) {
            final View v = inf.inflate(R.layout.search_message_list_item, null);
            TextView txtvMessage = v.findViewById(R.id.txtvMessage);
            TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);

            txtvMessage.setText(message.getContent());
            txtvTimeStamp.setText(myDateFormat.format(message.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

}