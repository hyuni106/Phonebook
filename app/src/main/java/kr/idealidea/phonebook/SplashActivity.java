package kr.idealidea.phonebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;

public class SplashActivity extends AppCompatActivity {
    String finalPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//               TODO - 업로드 시 토큰 변경
                getPhoneNum();
//                ContextUtils.setUserToken(SplashActivity.this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwidXNlcl9pZCI6IjAxMC05OTkxLTgzODcifQ.99qdzaFILadWf2RQS9xfkJ3gvjvKWX_ZFB50caRCx8W8KE-vYWjsGbHpTLJwPwoRUHS2kzMttlOYxPQ_IuHnjg");

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("phone", finalPhoneNum);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }


    @SuppressLint("MissingPermission")
    public void getPhoneNum() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String phoneNum = "010-9991-8387";
        if (telManager.getLine1Number() != null) {
            phoneNum = telManager.getLine1Number();
            if (phoneNum.startsWith("+82")) {
                phoneNum = phoneNum.replace("+82", "0");
            }
            phoneNum = phoneNum.replace("-", "");
        }

        finalPhoneNum = phoneNum;

        if (ContextUtils.getUserToken(this).equals("")) {
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
