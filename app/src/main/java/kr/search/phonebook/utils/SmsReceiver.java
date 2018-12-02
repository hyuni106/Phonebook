package kr.search.phonebook.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.search.phonebook.CallPopupActivity;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String number = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "n";
                number = msgs[i].getOriginatingAddress();
            }
            //---display the new SMS message---
//            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();


            final String phone_number = number;
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
    }
}