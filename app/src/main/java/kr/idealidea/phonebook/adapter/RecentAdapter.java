package kr.idealidea.phonebook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kr.idealidea.phonebook.R;

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

        String number = mList.get(position);

        txtvRecentPhoneNum.setText(number);

        return row;
    }
}
