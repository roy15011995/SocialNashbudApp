package com.estar.nashbud.bottombarpages;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.estar.nashbud.R;

public class Discover extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        toolbar=(Toolbar)findViewById(R.id.toolbar_discover);

        toolbar.setTitle("Discover People");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        tabLayout=(TabLayout)findViewById(R.id.Tab_Layout_discover);

        viewPager=(ViewPager)findViewById(R.id.pager);
        //actionBar=(ActionBar)getActivity().getActionBar();
        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(new People_Fragment(),"People");
        pagerAdapter.addFragments(new FacebookFragment(),"Facebook");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.search_discover:
                Toast.makeText(getApplicationContext(),"Click On Search",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
