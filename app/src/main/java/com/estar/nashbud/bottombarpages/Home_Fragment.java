package com.estar.nashbud.bottombarpages;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.ChatFragmentFinal;
import com.estar.nashbud.chatscreenpages.Diary_Fragment_test;
import com.estar.nashbud.chatscreenpages.Diary_fragment;
import com.estar.nashbud.chatscreenpages.Activity_fragment;

/**
 * Created by User on 10/24/2017.
 */

public class Home_Fragment extends Fragment {
    //PagerSlidingTabStrip pagerSlider;
    //ViewPager viewPager;
    PagerAdapter adapter;
    int[] unreadCount={1,3,3};
    String[] title={"Activity","Chats","Diary"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        /*pagerSlider = view.findViewById(R.id.pagerSlider);
        viewPager = view.findViewById(R.id.viewpager);
        pagerSlider.setShouldExpand(true);
        pagerSlider.setIndicatorHeight(2);*/

for(int i=0;i<title.length;i++){
    TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
}
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Diary"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;

    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Activity_fragment tab1 = new Activity_fragment();
                    Log.e("PositionAcitivity ",""+position);
                    return tab1;
                case 1:
                    ChatFragmentFinal tab2 = new ChatFragmentFinal();
                    Log.e("Position_ChatFragment ",""+position);
                    return tab2;
                case 2:
                    Diary_fragment tab3 = new Diary_fragment();
                    Log.e("Position_Diary ",""+position);
                    //Diary_Fragment_test tab3 = new Diary_Fragment_test();
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


    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
        setUpTabStrip();

    }*/

    /*public void setViewPager() {
        String name[] = new String[]{"Moments", "Chats", "Diary"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Activity_fragment());
        fragments.add(new ChatFragmentFinal());
        fragments.add(new Diary_fragment());
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), getActivity(), fragments, name.length, name));
        viewPager.setOffscreenPageLimit(name.length);
        pagerSlider.setViewPager(viewPager);

        pagerSlider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout mTabsLinearLayout;
                mTabsLinearLayout = ((LinearLayout) pagerSlider.getChildAt(0));
                for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
                    if (i == position) {
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    } else {
                        tv.setTextColor(Color.LTGRAY);
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setUpTabStrip() {
        LinearLayout mTabsLinearLayout;
        //your other customizations related to tab strip...blahblah
        // Set first tab selected
        mTabsLinearLayout = ((LinearLayout) pagerSlider.getChildAt(0));
        for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
            // TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

            if (i == 0) {
                // tv.setTextColor(Color.GRAY);
            } else {
                //  tv.setTextColor(Color.LTGRAY);
            }
        }
    }*/

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.badge_textview,null);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        //TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(""+unreadCount[pos]);
        }
        else
            tv_count.setVisibility(View.GONE);


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);

    }
}