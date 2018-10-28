package kr.idealidea.phonebook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;

public class SplashActivity extends AppCompatActivity {
    String finalPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//               TODO - 업로드 시 토큰 변경
                                getPhoneNum();
//                ContextUtils.setUserToken(SplashActivity.this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwidXNlcl9pZCI6IjAxMC05OTkxLTgzODcifQ.99qdzaFILadWf2RQS9xfkJ3gvjvKWX_ZFB50caRCx8W8KE-vYWjsGbHpTLJwPwoRUHS2kzMttlOYxPQ_IuHnjg");

                                Intent intent = null;
                                if (ContextUtils.getUserToken(SplashActivity.this).equals("")) {
                                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    intent.putExtra("phone", finalPhoneNum);
                                } else {
                                    intent = new Intent(SplashActivity.this, MainActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                })
                .setDeniedMessage("어플을 사용하려면 권한을 허용해야 합니다.")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG
                        , Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_SMS)
                .check();
    }


    @SuppressLint("MissingPermission")
    public void getPhoneNum() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String phoneNum = "";
        if (telManager != null) {
            phoneNum = telManager.getLine1Number();
        }
        if (phoneNum != null) {
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }
            phoneNum = phoneNum.replace("-", "");
        } else {
            phoneNum = "01099918387";
        }
        finalPhoneNum = phoneNum;

        if (ContextUtils.isFirstStart(this).equals("")) {
            ConnectServer.putRequestSignUp(this, phoneNum, new ConnectServer.JsonResponseHandler() {
                @Override
                public void onResponse(JSONObject json) {
                    try {
                        if (json.getInt("code") == 200) {
                            ContextUtils.setUserToken(SplashActivity.this, json.getJSONObject("data").getString("token"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
