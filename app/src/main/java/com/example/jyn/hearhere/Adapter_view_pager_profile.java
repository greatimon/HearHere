package com.example.jyn.hearhere;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter_view_pager_profile extends FragmentPagerAdapter {

    private static int PROFILE_PAGE_NUMBER = 3;

    public Adapter_view_pager_profile(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return f_cast.newInstance("profile");
            case 1:
                return f_my_fans.newInstance();
            case 2:
                return f_my_bj.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "CAST";
            case 1:
                return "my_Fans";
            case 2:
                return "my_BJ";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PROFILE_PAGE_NUMBER;
    }
}

