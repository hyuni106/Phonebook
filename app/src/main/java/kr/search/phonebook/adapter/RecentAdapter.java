package kr.search.phonebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.search.phonebook.R;
import kr.search.phonebook.SearchActivity;
import kr.search.phonebook.data.Recent;
import kr.search.phonebook.utils.AppUtils;

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
        TextView txtvRecentNameFirst = row.findViewById(R.id.txtvRecentNameFirst);

        final Recent number = mList.get(position);

        txtvRecentNameFirst.setText(number.getName().substring(0, 1));
        txtvRecentPhoneNum.setText(AppUtils.makePhoneNumber(number.getNum()));
        txtvRecentName.setText(number.getName());
        if (number.getCount() > 0) {
            txtvRecentShopName.setText(String.format("%s 외 %d명 저장", number.getShop_name(), number.getCount() - 1));
        } else {
            txtvRecentShopName.setText("저장 내역 없음");
        }

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
