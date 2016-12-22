package com.hackerli.retrofit.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by CoXier on 2016/5/2.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment>  mFragments;
    private String[] titles ;

    TabsPagerAdapter(FragmentManager fm, List<Fragment> mFragments, String[] titles) {
        super(fm);
        this.mFragments = mFragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
