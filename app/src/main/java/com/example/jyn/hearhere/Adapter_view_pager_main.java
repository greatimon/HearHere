package com.example.jyn.hearhere;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter_view_pager_main extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 2;

    public Adapter_view_pager_main(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return f_live.newInstance();
            case 1:
                return f_cast.newInstance("main");
            default:
                return null;
        }
//        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Live";
            case 1:
                return "Cast";
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }
}
