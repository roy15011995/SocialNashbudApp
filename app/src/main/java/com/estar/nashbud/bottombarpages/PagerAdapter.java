package com.estar.nashbud.bottombarpages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by User on 28-03-2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tab_Title = new ArrayList<>();


    public  void addFragments(Fragment fragments , String titles){
        this.fragments.add(fragments);
        this.tab_Title.add(titles);
    }
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_Title.get(position);
    }
}
