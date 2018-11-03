package kr.idealidea.phonebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CallPopupActivity extends AppCompatActivity {

    public static final String EXTRA_CALL_NUMBER = "call_number";
    public static final String EXTRA_SHOP_NAME = "shop_name";
    public static final String EXTRA_COUNT = "count";

    TextView txtvPopupPhone;
    TextView txtvPopupShopName;
    TextView txtvPopupCount;

    String call_number = "";
    String shopName = "";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_popup);

        call_number = getIntent().getStringExtra(EXTRA_CALL_NUMBER);
        shopName = getIntent().getStringExtra(EXTRA_SHOP_NAME);
        count = getIntent().getIntExtra(EXTRA_COUNT, 0);

        txtvPopupPhone = findViewById(R.id.txtvPopupPhone);
        txtvPopupShopName = findViewById(R.id.txtvPopupShopName);
        txtvPopupCount = findViewById(R.id.txtvPopupCount);

        txtvPopupPhone.setText(call_number);
        txtvPopupShopName.setText(shopName);
        txtvPopupCount.setText(String.format("%d 건", count));
    }
}
