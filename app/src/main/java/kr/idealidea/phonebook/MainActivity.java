package kr.idealidea.phonebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.idealidea.phonebook.utils.AppUtils;
import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;

public class MainActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        Button phoneBookBtn = findViewById(R.id.phoneBookBtn);
        Button callListBtn = findViewById(R.id.callListBtn);
        Button smsListBtn = findViewById(R.id.smsListBtn);

        TextView txtvMainSearch = findViewById(R.id.txtvMainSearch);
        final EditText editMainSearch = findViewById(R.id.editMainSearch);

        phoneBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                                contacts();
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                intent.putExtra("intent", "book");
                startActivity(intent);
            }
        });

        callListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                                callLog();
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                intent.putExtra("intent", "log");
                startActivity(intent);
            }
        });

        smsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                                readSMSMessage();
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                intent.putExtra("intent", "message");
                startActivity(intent);
            }
        });

        txtvMainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = editMainSearch.getText().toString();
                if (search.equals("")) {
                    Toast.makeText(mContext, "검색할 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("phone", search);
                startActivity(intent);
            }
        });

    }
}
