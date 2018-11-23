package kr.search.phonebook;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.search.phonebook.adapter.RecentAdapter;
import kr.search.phonebook.data.Recent;
import kr.search.phonebook.utils.AppUtils;
import kr.search.phonebook.utils.GlobalData;

public class MainActivity extends BaseActivity {
    Button phoneBookBtn;
    Button callListBtn;
    Button smsListBtn;
    TextView txtvMainPeriod;
    TextView txtvMainSearch;
    EditText editMainSearch;
    ListView listMainRecentNum;
    LinearLayout layoutMainNoItem;
    LinearLayout layoutMainSearch;

    List<Recent> recentList = new ArrayList<>();
    RecentAdapter mAdapter;

    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {
        phoneBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListViewActivity.class);
                intent.putExtra("intent", "book");
                startActivity(intent);
            }
        });

        callListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListViewActivity.class);
                intent.putExtra("intent", "log");
                startActivity(intent);
            }
        });

        smsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListViewActivity.class);
                intent.putExtra("intent", "message");
                startActivity(intent);
            }
        });

        txtvMainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = editMainSearch.getText().toString();
                if (search.equals("")) {
                    Toast.makeText(mContext, "검색할 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("phone", search);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setValues() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        txtvMainPeriod.setText(String.format("만료일 : %s ", myDateFormat.format(GlobalData.loginUser.getUserPeriod().getEnd().getTime())));
        listMainRecentNum.setEmptyView(layoutMainNoItem);

        if (GlobalData.loginUser.isAdmin()) {
            layoutMainSearch.setVisibility(View.VISIBLE);
        } else {
            layoutMainSearch.setVisibility(View.GONE);
        }

        recentList.clear();
        recentList = AppUtils.getRecentNumList(mContext);
        mAdapter = new RecentAdapter(mContext, recentList);
        listMainRecentNum.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        Log.d("onResume", "onResume");
        mAdapter.updateList(AppUtils.getRecentNumList(mContext));
        super.onResume();
    }

    @Override
    public void bindViews() {
        phoneBookBtn = findViewById(R.id.phoneBookBtn);
        callListBtn = findViewById(R.id.callListBtn);
        smsListBtn = findViewById(R.id.smsListBtn);
        txtvMainPeriod = findViewById(R.id.txtvMainPeriod);
        txtvMainSearch = findViewById(R.id.txtvMainSearch);
        editMainSearch = findViewById(R.id.editMainSearch);
        listMainRecentNum = findViewById(R.id.listMainRecentNum);
        layoutMainNoItem = findViewById(R.id.layoutMainNoItem);
        layoutMainSearch = findViewById(R.id.layoutMainSearch);
    }
}
