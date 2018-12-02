package kr.search.phonebook.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import kr.search.phonebook.CallPopupActivity;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "PHONE STATE";
    private static String mLastState;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);

                ConnectServer.postRequestCallNumInfo(context, phone_number.replaceAll("-", ""), new ConnectServer.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            if (json.getInt("code") == 200) {
                                String shopName = json.getJSONObject("data").getString("shopname");
                                int count = json.getJSONObject("data").getInt("total");
                                String name = json.getJSONObject("data").getString("name");

                                Intent serviceIntent = new Intent(context, CallPopupActivity.class);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_CALL_NUMBER, phone_number);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_SHOP_NAME, shopName);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_NAME, name);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_COUNT, count);
                                serviceIntent.putExtra("isCall", true);
                                context.startActivity(serviceIntent);
                            } else if (json.getInt("code") == 400) {
                                Intent serviceIntent = new Intent(context, CallPopupActivity.class);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_CALL_NUMBER, phone_number);
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_SHOP_NAME, "저장된 내역 없음");
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_NAME, "저장된 내역 없음");
                                serviceIntent.putExtra(CallPopupActivity.EXTRA_COUNT, 0);
                                serviceIntent.putExtra("isCall", true);
                                context.startActivity(serviceIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
//                Toast.makeText(context, "Call Received State", Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                Toast.makeText(context, "Call Idle State", Toast.LENGTH_SHORT).show();
//                CallRecevierActivity.act.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
