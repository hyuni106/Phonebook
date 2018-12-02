package kr.search.phonebook;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;

import kr.search.phonebook.utils.CustomProgressDialog;

public abstract class BaseActivity extends AppCompatActivity {

    Context mContext = this;
    public static CustomProgressDialog customProgressDialog = null;

    public abstract void setupEvents();
    public abstract void setValues();
    public abstract void bindViews();


    public void showCustomProgress() {
        customProgressDialog = new CustomProgressDialog(mContext);
        customProgressDialog .getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();
    }

    public void dismissCustomProgress() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
            customProgressDialog = null;
        }
    }

}