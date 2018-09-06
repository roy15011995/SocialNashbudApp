package com.estar.nashbud.bottombarpages;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.estar.nashbud.R;

/**
 * Created by Sudipta on 10/24/2017.
 */

public class Discover_Fragment extends Fragment {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBar actionBar;
    PagerAdapter pagerAdapter;
    ImageView imageback;
    TextView textTopic;
    Button done;
    LinearLayout linearLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment, container, false);
        /*toolbar=(Toolbar)view.findViewById(R.id.toolbar_discover);

        toolbar.setTitle("Discover People");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));*/
        tabLayout=(TabLayout)view.findViewById(R.id.Tab_Layout_discover);

        viewPager=(ViewPager)view.findViewById(R.id.pager);
        //actionBar=(ActionBar)getActivity().getActionBar();
       /* toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setHasOptionsMenu(true);*/

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });*/

      /*  if(toolbar!=null){
            getActivity().setActionBar(toolbar);
        }*/

        /*getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);*/

        imageback = (ImageView)view.findViewById(R.id.imageBack);
        imageback.setVisibility(View.GONE);

        done = (Button)view.findViewById(R.id.doneVisible);
        done.setVisibility(View.GONE);

        textTopic = (TextView)view.findViewById(R.id.textTopic);
        textTopic.setText("Discover People");
        textTopic.setTextColor(Color.BLACK);
        textTopic.setGravity(Gravity.TOP);

        linearLayout = (LinearLayout)view.findViewById(R.id.linear_background);
        linearLayout.setBackgroundColor(Color.WHITE);

        if(pagerAdapter==null){
            pagerAdapter = new PagerAdapter(getChildFragmentManager());

            pagerAdapter.addFragments(new People_Fragment(),"People");
            //pagerAdapter.addFragments(new FacebookFragment(),"Facebook");
            pagerAdapter.addFragments(new Suggested_Fragment(),"Suggested");
        }



        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        return view;
    }
}