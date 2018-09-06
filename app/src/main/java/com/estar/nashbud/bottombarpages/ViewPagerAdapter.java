package com.estar.nashbud.bottombarpages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sudipta on 9/20/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT;
    private String tabTitles[];
    List<Fragment> fragments;
    Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context, List fragments, int PAGE_COUNT, String tabTitles[]) {
        super(fm);
        this.PAGE_COUNT = PAGE_COUNT;
        this.tabTitles = tabTitles;
        this.fragments = fragments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
