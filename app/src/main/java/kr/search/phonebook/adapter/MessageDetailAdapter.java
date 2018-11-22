package kr.search.phonebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kr.search.phonebook.R;
import kr.search.phonebook.data.Message;

public class MessageDetailAdapter extends BaseAdapter {

    Context mContext;
    List<Message> mList;
    LayoutInflater inf;

    public MessageDetailAdapter(Context context, List<Message> list) {
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
        View row = inf.inflate(R.layout.message_detail_list_item, viewGroup, false);
        LinearLayout layoutMessageItemSend = row.findViewById(R.id.layoutMessageItemSend);
        TextView txtvMessageItemSend = row.findViewById(R.id.txtvMessageItemSend);
        LinearLayout layoutMessageItemReceive = row.findViewById(R.id.layoutMessageItemReceive);
        TextView txtvMessageItemReceive = row.findViewById(R.id.txtvMessageItemReceive);

        final Message item = mList.get(position);

        if (item.getType().equals("IN")) {
            layoutMessageItemSend.setVisibility(View.GONE);
            layoutMessageItemReceive.setVisibility(View.VISIBLE);
            txtvMessageItemReceive.setText(item.getContent());
        } else {
            layoutMessageItemSend.setVisibility(View.VISIBLE);
            layoutMessageItemReceive.setVisibility(View.GONE);
            txtvMessageItemSend.setText(item.getContent());
        }

        return row;
    }

    public void updateList(List<Message> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}