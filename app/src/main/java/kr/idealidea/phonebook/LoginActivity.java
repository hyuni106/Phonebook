package kr.idealidea.phonebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText editLoginPhone;
    EditText editLoginAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String phone = getIntent().getStringExtra("phone");

        btnLogin = findViewById(R.id.btnLogin);
        editLoginPhone = findViewById(R.id.editLoginPhone);
        editLoginAuth = findViewById(R.id.editLoginAuth);

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
}
