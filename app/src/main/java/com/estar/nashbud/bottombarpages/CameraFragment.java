package com.estar.nashbud.bottombarpages;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.Camera_Fragment;
import com.estar.nashbud.camera_package.GalleryFragment;

/**
 * Created by Sudipta on 10/24/2017.
 */

public class CameraFragment extends Fragment {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBar actionBar;
    PagerAdapter pagerAdapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar_discover);

        if(toolbar!=null){
            getActivity().setActionBar(toolbar);
        }

        /*getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);*/

        if(pagerAdapter==null){
            pagerAdapter = new PagerAdapter(getChildFragmentManager());

            pagerAdapter.addFragments(new GalleryFragment(),"Gallery");
            pagerAdapter.addFragments(new Camera_Fragment(),"Shoot");
        }

        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        return view;
    }
}