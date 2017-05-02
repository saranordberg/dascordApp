package com.example.chat.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.chat.chat.Chat_fragment;
import com.example.chat.chat.Profile_fragment;

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
                Profile_fragment tab1 = new Profile_fragment();
                return tab1;
            case 1:
                Guilds_fragment tab2 = new Guilds_fragment();
                return tab2;
            case 2:
                Chat_fragment tab3 = new Chat_fragment();
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