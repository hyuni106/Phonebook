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
import java.util.Locale;

import kr.idealidea.phonebook.data.CallLog;
import kr.idealidea.phonebook.data.Contact;
import kr.idealidea.phonebook.data.Message;
import kr.idealidea.phonebook.utils.AppUtils;
import kr.idealidea.phonebook.utils.ConnectServer;

public class SearchActivity extends BaseActivity {
    String search = "";

    TextView txtvSearchPhone;
    TextView lineSearchTab1;
    TextView lineSearchTab2;
    TextView lineSearchTab3;
    TextView txtvSearchShopName;
    TextView txtvSearchCount;
    FrameLayout frameSearchTab1;
    FrameLayout frameSearchTab2;
    FrameLayout frameSearchTab3;
    LinearLayout layoutSearchList;

    int incoming = 0;
    int outcalling = 0;
    int miss = 0;

    List<Contact> contactList = new ArrayList<>();
    List<CallLog> callLogList = new ArrayList<>();
    List<Message> messagesList = new ArrayList<>();
    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = getIntent().getStringExtra("phone").replaceAll("-", "");
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {
        frameSearchTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSearchTab1.setVisibility(View.VISIBLE);
                lineSearchTab2.setVisibility(View.GONE);
                lineSearchTab3.setVisibility(View.GONE);

                if (contactList.size() > 0) {
                    txtvSearchCount.setText(String.format(Locale.KOREA, "연락처 저장 %d명", contactList.size()));
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
                    txtvSearchCount.setText(String.format(Locale.KOREA, "수신 %d건, 발신 %d건, 부재중 %d건", incoming, outcalling, miss));
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
                    txtvSearchCount.setText(String.format(Locale.KOREA, "문자메시지 %d건", messagesList.size()));
                    drawMessageList();
                } else {
                    layoutSearchList.removeAllViews();
                    getSearchMessage();
                }
            }
        });

    }

    @Override
    public void setValues() {
        txtvSearchPhone.setText(search);
        AppUtils.setRecentNumArrayString(mContext, search);
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
                            if (contactList.size() > 0) {
                                txtvSearchShopName.setText(contactList.get(contactList.size() - 1).getName());
                            } else {
                                txtvSearchShopName.setText("");
                            }
                            txtvSearchCount.setText(String.format(Locale.KOREA, "연락처 저장 %d명", contactList.size()));
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
            txtvPhoneNum.setText(contact.getShop_name());
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
                            if (c.getLog_type().equals("IN")) {
                                incoming++;
                            } else if (c.getLog_type().equals("OUT")) {
                                outcalling++;
                            } else if (c.getLog_type().equals("MISS")) {
                                miss++;
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtvSearchCount.setText(String.format(Locale.KOREA, "수신 %d건, 발신 %d건, 부재중 %d건", incoming, outcalling, miss));
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
            txtvPhoneNum.setText(callLog.getShop_name());
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
                            txtvSearchCount.setText(String.format(Locale.KOREA, "문자메시지 %d건", messagesList.size()));
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
            TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);

            txtvMessage.setText(message.getContent());
            txtvPhoneNum.setText(message.getShop_name());
            txtvTimeStamp.setText(myDateFormat.format(message.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

    @Override
    public void bindViews() {
        txtvSearchPhone = findViewById(R.id.txtvSearchPhone);
        frameSearchTab1 = findViewById(R.id.frameSearchTab1);
        frameSearchTab2 = findViewById(R.id.frameSearchTab2);
        frameSearchTab3 = findViewById(R.id.frameSearchTab3);
        lineSearchTab1 = findViewById(R.id.lineSearchTab1);
        lineSearchTab2 = findViewById(R.id.lineSearchTab2);
        lineSearchTab3 = findViewById(R.id.lineSearchTab3);
        layoutSearchList = findViewById(R.id.layoutSearchList);
        txtvSearchShopName = findViewById(R.id.txtvSearchShopName);
        txtvSearchCount = findViewById(R.id.txtvSearchCount);
    }
}
