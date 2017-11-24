package com.example.jyn.hearhere;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class Adapter_view_pager_search extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 3;

    public Adapter_view_pager_search(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("프래그먼트", "getItem() 호출: " + position);
        switch(position) {
            case 0:
                return f_nickName.newInstance();
            case 1:
                return f_search_live.newInstance();
            case 2:
                return f_search_cast.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        Log.d("viewPager", "getPageTitle() 호출!");
        switch(position) {
            case 0:
                return "닉네임";
            case 1:
                return "live";
            case 2:
                return "cast";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
//        Log.d("viewPager", "getCount() 호출!");
        return PAGE_NUMBER;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
