package kr.idealidea.phonebook.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.idealidea.phonebook.CallPopupActivity;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "PHONE STATE";
    private static String mLastState;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(final Context context, Intent intent) {
//        Log.d(TAG, "onReceive()"); /** * http://mmarvick.github.io/blog/blog/lollipop-multiple-broadcastreceiver-call-state/ * 2번 호출되는 문제 해결 */
//        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//        if (state.equals(mLastState)) {
//            return;
//        } else {
//            mLastState = state;
//        }
//        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
//            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
//            Intent serviceIntent = new Intent(context, CallingService.class);
//            serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
//            context.startService(serviceIntent);
//        }
        try {
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


//            User user = (User) intent.getSerializableExtra("user");

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                if (incomingNumber.equals(user.getUserPhoneNum())) {
//                    Toast.makeText(context, "전화한사람 이름" + user.getUserName(), Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(context, "모르는 번호 입니다.", Toast.LENGTH_SHORT).show();
//                }

                final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);

                ConnectServer.postRequestCallNumInfo(context, phone_number.replaceAll("-", ""), new ConnectServer.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            if (json.getInt("code") == 200) {
                                String shopName = json.getJSONObject("data").getString("name");
                                int count = json.getJSONObject("data").getInt("total");

                                Intent serviceIntent = new Intent(context, CallPopupActivity.class);
                                serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
                                serviceIntent.putExtra(CallingService.EXTRA_SHOP_NAME, shopName);
                                serviceIntent.putExtra(CallingService.EXTRA_COUNT, count);
                                serviceIntent.putExtra("isCall", true);
//                context.startService(serviceIntent);
                                context.startActivity(serviceIntent);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    context.startForegroundService(serviceIntent);
//                                } else {
//                                    context.startService(serviceIntent);
//                                }

                            } else if (json.getInt("code") == 400) {
                                Intent serviceIntent = new Intent(context, CallPopupActivity.class);
                                serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
                                serviceIntent.putExtra(CallingService.EXTRA_SHOP_NAME, "저장된 내역 없음");
                                serviceIntent.putExtra(CallingService.EXTRA_COUNT, 0);
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
