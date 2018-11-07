package kr.idealidea.phonebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kr.idealidea.phonebook.CallPopupActivity;
import kr.idealidea.phonebook.R;
import kr.idealidea.phonebook.SearchActivity;
import kr.idealidea.phonebook.utils.CallingService;
import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;
import kr.idealidea.phonebook.utils.GlobalData;

public class RecentAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;
    LayoutInflater inf;

    public RecentAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = inf.inflate(R.layout.recent_list_item, viewGroup, false);
        TextView txtvRecentPhoneNum = row.findViewById(R.id.txtvRecentPhoneNum);

        final String number = mList.get(position);

        txtvRecentPhoneNum.setText(number);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.loginUser.isAdmin()) {
                    Intent intent = new Intent(mContext, SearchActivity.class);
                    intent.putExtra("phone", number);
                    mContext.startActivity(intent);
                } else {
                    ConnectServer.postRequestCallNumInfo(mContext, number.replaceAll("-", ""), new ConnectServer.JsonResponseHandler() {
                        @Override
                        public void onResponse(JSONObject json) {
                            try {
                                if (json.getInt("code") == 200) {
                                    String shopName = json.getJSONObject("data").getString("name");
                                    int count = json.getJSONObject("data").getInt("total");

                                    Intent serviceIntent = new Intent(mContext, CallPopupActivity.class);
                                    serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, number);
                                    serviceIntent.putExtra(CallingService.EXTRA_SHOP_NAME, shopName);
                                    serviceIntent.putExtra(CallingService.EXTRA_COUNT, count);
                                    serviceIntent.putExtra("isCall", false);
                                    mContext.startActivity(serviceIntent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

        return row;
    }
}
