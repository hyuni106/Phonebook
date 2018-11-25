package kr.search.phonebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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

import kr.search.phonebook.data.CallLog;
import kr.search.phonebook.data.Contact;
import kr.search.phonebook.data.Message;
import kr.search.phonebook.data.Recent;
import kr.search.phonebook.utils.AppUtils;
import kr.search.phonebook.utils.ConnectServer;
import kr.search.phonebook.utils.GlobalData;

public class SearchActivity extends BaseActivity {
    Recent search = new Recent();

    TextView txtvSearchPhone;
    TextView lineSearchTab1;
    TextView lineSearchTab2;
    TextView lineSearchTab3;
    TextView txtvSearchShopName;
    TextView txtvSearchCount;
    TextView txtvSearchTab1;
    TextView txtvSearchTab2;
    TextView txtvSearchTab3;
    FrameLayout frameSearchTab1;
    FrameLayout frameSearchTab2;
    FrameLayout frameSearchTab3;
    LinearLayout layoutSearchList;

    int incoming = 0;
    int outcalling = 0;
    int miss = 0;
    int unknown = 0;

    List<Contact> contactList = new ArrayList<>();
    List<CallLog> callLogList = new ArrayList<>();
    List<Message> messagesList = new ArrayList<>();
    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        String num = getIntent().getStringExtra("phone").replaceAll("-", "");
        search.setNum(num);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {
        frameSearchTab1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                lineSearchTab1.setBackgroundColor(getResources().getColor(R.color.new_green));
                lineSearchTab2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                lineSearchTab3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab1.setTextColor(getResources().getColor(R.color.new_green));
                txtvSearchTab2.setTextColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab3.setTextColor(getResources().getColor(R.color.light_gray));

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
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                lineSearchTab2.setBackgroundColor(getResources().getColor(R.color.new_green));
                lineSearchTab1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                lineSearchTab3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab2.setTextColor(getResources().getColor(R.color.new_green));
                txtvSearchTab1.setTextColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab3.setTextColor(getResources().getColor(R.color.light_gray));

                if (callLogList.size() > 0) {
                    txtvSearchCount.setText(String.format(Locale.KOREA, "수신 %d건, 발신 %d건, 부재중 %d건, 알수없음 %d건", incoming, outcalling, miss, unknown));
                    drawCallLogList();
                } else {
                    layoutSearchList.removeAllViews();
                    getSearchCallLog();
                }
            }
        });

        frameSearchTab3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                lineSearchTab3.setBackgroundColor(getResources().getColor(R.color.new_green));
                lineSearchTab2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                lineSearchTab1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab3.setTextColor(getResources().getColor(R.color.new_green));
                txtvSearchTab2.setTextColor(getResources().getColor(R.color.light_gray));
                txtvSearchTab1.setTextColor(getResources().getColor(R.color.light_gray));

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (GlobalData.loginUser.isAdmin()) {
            frameSearchTab2.setVisibility(View.VISIBLE);
            frameSearchTab3.setVisibility(View.VISIBLE);
        } else {
            frameSearchTab2.setVisibility(View.GONE);
            frameSearchTab3.setVisibility(View.GONE);
        }

        txtvSearchPhone.setText(AppUtils.makePhoneNumber(search.getNum()));
        getSearchContact();
    }

    private void getSearchContact() {
        ConnectServer.getRequestAllContact(SearchActivity.this, search.getNum(), new ConnectServer.JsonResponseHandler() {
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
                                search.setShop_name(contactList.get(contactList.size() - 1).getShop_name());
                            } else {
                                txtvSearchShopName.setText("저장 내역 없음");
                                search.setShop_name("저장 내역 없음");
                            }
                            search.setCount(contactList.size());
                            search.setName(txtvSearchShopName.getText().toString());
                            AppUtils.setRecentNumArrayString(mContext, search);
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

            txtvName.setText(contact.getName());
            txtvPhoneNum.setText(contact.getShop_name());
            txtvTimeStamp.setText(myDateFormat.format(contact.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

    private void getSearchCallLog() {
        ConnectServer.getRequestAllCallLogs(SearchActivity.this, search.getNum(), new ConnectServer.JsonResponseHandler() {
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
                            } else {
                                unknown++;
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
            TextView txtvCallLogType = v.findViewById(R.id.txtvCallLogType);

            String type = "";
            if (callLog.getLog_type().equals("IN")) {
                type = "수신";
                txtvCallLogType.setText("수");
            } else if (callLog.getLog_type().equals("OUT")) {
                type = "발신";
                txtvCallLogType.setText("발");
            } else if (callLog.getLog_type().equals("MISS")) {
                type = "부재중";
                txtvCallLogType.setText("부");
            } else {
                type = "알수없음";
                txtvCallLogType.setText("알");
            }

            txtvType.setText(type);
            txtvDuration.setText(callLog.getTime());
            txtvPhoneNum.setText(callLog.getShop_name());
            txtvTimeStamp.setText(myDateFormat.format(callLog.getCreated_at().getTime()));

            layoutSearchList.addView(v);
        }
    }

    private void getSearchMessage() {
        ConnectServer.getRequestAllMessage(SearchActivity.this, search.getNum(), new ConnectServer.JsonResponseHandler() {
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

        for (final Message message : messagesList) {
            final View v = inf.inflate(R.layout.search_message_list_item, null);
            TextView txtvMessage = v.findViewById(R.id.txtvMessage);
            TextView txtvTimeStamp = v.findViewById(R.id.txtvTimeStamp);
            TextView txtvPhoneNum = v.findViewById(R.id.txtvPhoneNum);

            txtvMessage.setText(message.getContent());
            txtvPhoneNum.setText(message.getShop_name());
            txtvTimeStamp.setText(myDateFormat.format(message.getCreated_at().getTime()));

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageDetailActivity.class);
                    intent.putExtra("message", message);
                    startActivity(intent);
                }
            });

            layoutSearchList.addView(v);
        }
    }

    @Override
    public void bindViews() {
        txtvSearchTab3 = findViewById(R.id.txtvSearchTab3);
        txtvSearchTab2 = findViewById(R.id.txtvSearchTab2);
        txtvSearchTab1 = findViewById(R.id.txtvSearchTab1);
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
