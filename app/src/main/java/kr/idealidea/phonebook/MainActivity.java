package kr.idealidea.phonebook;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.idealidea.phonebook.utils.AppUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Button phoneBookBtn = findViewById(R.id.phoneBookBtn);
                        Button callListBtn = findViewById(R.id.callListBtn);
                        Button smsListBtn = findViewById(R.id.smsListBtn);

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
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .setDeniedMessage("어플을 사용하려면 권한을 허용해야 합니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG
                        , Manifest.permission.WRITE_CALL_LOG)
                .check();
    }
}
