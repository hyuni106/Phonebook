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
import kr.idealidea.phonebook.data.Recent;
import kr.idealidea.phonebook.utils.AppUtils;
import kr.idealidea.phonebook.utils.CallingService;
import kr.idealidea.phonebook.utils.ConnectServer;
import kr.idealidea.phonebook.utils.ContextUtils;
import kr.idealidea.phonebook.utils.GlobalData;

public class RecentAdapter extends BaseAdapter {

    Context mContext;
    List<Recent> mList;
    LayoutInflater inf;

    public RecentAdapter(Context context, List<Recent> list) {
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
        TextView txtvRecentName = row.findViewById(R.id.txtvRecentName);
        TextView txtvRecentShopName = row.findViewById(R.id.txtvRecentShopName);

        final Recent number = mList.get(position);

        txtvRecentPhoneNum.setText(AppUtils.makePhoneNumber(number.getNum()));

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("phone", number.getNum());
                mContext.startActivity(intent);

//                ConnectServer.postRequestCallNumInfo(mContext, number.replaceAll("-", ""), new ConnectServer.JsonResponseHandler() {
//                    @Override
//                    public void onResponse(JSONObject json) {
//                        try {
//                            if (json.getInt("code") == 200) {
//                                Intent intent = new Intent(mContext, SearchActivity.class);
//                                intent.putExtra("phone", number);
//                                mContext.startActivity(intent);
//
////                                String shopName = json.getJSONObject("data").getString("name");
////                                int count = json.getJSONObject("data").getInt("total");
////
////                                Intent serviceIntent = new Intent(mContext, CallPopupActivity.class);
////                                serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, number);
////                                serviceIntent.putExtra(CallingService.EXTRA_SHOP_NAME, shopName);
////                                serviceIntent.putExtra(CallingService.EXTRA_COUNT, count);
////                                serviceIntent.putExtra("isCall", false);
////                                mContext.startActivity(serviceIntent);
//                            } else if (json.getInt("code") == 400) {
//                                Intent serviceIntent = new Intent(mContext, CallPopupActivity.class);
//                                serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, number);
//                                serviceIntent.putExtra(CallingService.EXTRA_SHOP_NAME, "저장된 내역 없음");
//                                serviceIntent.putExtra(CallingService.EXTRA_COUNT, 0);
//                                serviceIntent.putExtra("isCall", true);
//                                mContext.startActivity(serviceIntent);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        });

        return row;
    }

    public void updateList(List<Recent> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
