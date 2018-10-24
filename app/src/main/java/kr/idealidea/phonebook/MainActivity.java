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

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Button phoneBookBtn = findViewById(R.id.phoneBookBtn);
                        Button callListBtn = findViewById(R.id.callListBtn);
                        Button smsListBtn = findViewById(R.id.smsListBtn);

//                        TODO - 업로드 시 토큰 변경
                        getPhoneNum();
//                        ContextUtils.setUserToken(mContext, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwidXNlcl9pZCI6IjAxMC05OTkxLTgzODcifQ.99qdzaFILadWf2RQS9xfkJ3gvjvKWX_ZFB50caRCx8W8KE-vYWjsGbHpTLJwPwoRUHS2kzMttlOYxPQ_IuHnjg");

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
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                })
                .setDeniedMessage("어플을 사용하려면 권한을 허용해야 합니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG
                        , Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_SMS)
                .check();
    }

    @SuppressLint("MissingPermission")
    public void getPhoneNum() {
        if (ContextUtils.getUserToken(mContext).equals("")) {
            TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String phoneNum = "010-1234-5678";
            if (telManager != null) {
                phoneNum = telManager.getLine1Number();
            }
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }

            ConnectServer.putRequestSignUp(mContext, phoneNum, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {
                    try {
                        if (json.getInt("code") == 200) {
                            ContextUtils.setUserToken(mContext, json.getJSONObject("data").getString("token"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
