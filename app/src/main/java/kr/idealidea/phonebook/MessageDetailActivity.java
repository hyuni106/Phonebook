package kr.idealidea.phonebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.idealidea.phonebook.adapter.MessageDetailAdapter;
import kr.idealidea.phonebook.data.Message;
import kr.idealidea.phonebook.utils.ConnectServer;

public class MessageDetailActivity extends BaseActivity {
    Message message;
    List<Message> list = new ArrayList<>();
    ListView listMessageDetail;
    MessageDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        message = (Message) getIntent().getSerializableExtra("message");
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

    }

    @Override
    public void setValues() {
        adapter = new MessageDetailAdapter(mContext, list);
        listMessageDetail.setAdapter(adapter);

        ConnectServer.postRequestMessageDetail(mContext, message.getUid() + "", message.getPhone(), new ConnectServer.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                try {
                    if (json.getInt("code") == 200) {
                        list.clear();
                        JSONArray contacts = json.getJSONObject("data").getJSONArray("messages");
                        for (int i=contacts.length() - 1; i>=0; i--) {
                            Message c = Message.getContactFromJson(contacts.getJSONObject(i));
                            list.add(c);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateList(list);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void bindViews() {
        listMessageDetail = findViewById(R.id.listMessageDetail);
    }
}
