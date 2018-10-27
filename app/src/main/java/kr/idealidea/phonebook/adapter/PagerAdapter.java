package kr.idealidea.phonebook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kr.idealidea.phonebook.fragments.CallLogFragment;
import kr.idealidea.phonebook.fragments.ContactFragment;
import kr.idealidea.phonebook.fragments.MessageFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ContactFragment tab1 = new ContactFragment();
                return tab1;
            case 1:
                CallLogFragment tab2 = new CallLogFragment();
                return tab2;
            case 2:
                MessageFragment tab3 = new MessageFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}