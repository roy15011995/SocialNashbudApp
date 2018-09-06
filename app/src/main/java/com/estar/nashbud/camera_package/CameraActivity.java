package com.estar.nashbud.camera_package;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

//import com.estar.nashbud.Manifest;
import com.estar.nashbud.R;
import com.estar.nashbud.bottombarpages.FacebookFragment;
import com.estar.nashbud.bottombarpages.PagerAdapter;
import com.estar.nashbud.bottombarpages.People_Fragment;
import com.estar.nashbud.bottombarpages.Permissions;

public class CameraActivity extends AppCompatActivity {
Toolbar toolbar;
ViewPager viewPager;
TabLayout tabLayout;
ActionBar actionBar;
Bundle extra;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "CameraActivity";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        toolbar = (Toolbar)findViewById(R.id.toolbar_all);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        tabLayout=(TabLayout)findViewById(R.id.Tab_Layout_Camera);
        viewPager=(ViewPager)findViewById(R.id.pager_Camera);
        actionBar=getSupportActionBar();
        toolbar.setTitle("Shoot");
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }


      /*  if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

        if(checkPermissionArray(Permissions.permissions)){

        }
        else
        {
            verifyPermissions(Permissions.permissions);
        }*/


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(new Camera_Fragment(),"Shoot");
        //pagerAdapter.addFragments(new GalleryFragment(),"Gallery");
        pagerAdapter.addFragments(new CustomGalleryFragment(),"Gallery");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        extra = getIntent().getExtras();
        if(extra!=null)
        {
            String GetValue = extra.getString("IsPROFILE");
            Log.e("GetValue ",""+GetValue);
            CustomGalleryFragment galleryFragment = (CustomGalleryFragment) pagerAdapter.getItem(1);
            galleryFragment.GetValueIntent(GetValue);
        }
       // viewPager.setCurrentItem();
        if (tabLayout.getSelectedTabPosition() == 0){
            toolbar.setTitle("Shoot");
            Drawable close1 = getResources().getDrawable(R.drawable.close);
            close1.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(close1);
            toolbar.setTitleTextColor(Color.parseColor("#000000"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar.setTitle("Shoot");
                        Drawable close1 = getResources().getDrawable(R.drawable.close);
                        close1.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
                        getSupportActionBar().setHomeAsUpIndicator(close1);
                        toolbar.setTitleTextColor(Color.parseColor("#000000"));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);

                        break;
                    case 1:
                        toolbar.setTitle("Gallery");
                        Drawable close = getResources().getDrawable(R.drawable.close);
                        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
                        getSupportActionBar().setHomeAsUpIndicator(close);
                        toolbar.setTitleTextColor(Color.parseColor("#000000"));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

      /*  if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void setUpViewPager(){

    }

    public void verifyPermissions(String[] permissions){

        Log.d(TAG,"verifyPermissions : verifiying permissions.");
        ActivityCompat.requestPermissions(CameraActivity.this,permissions,1);

    }

    public boolean checkPermissionArray(String []permissions){
        Log.d(TAG,"checkPermissionArray : checking permission array.");
        for(int i=0;i<permissions.length;i++){
            String check = permissions[i];
            if(checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission){
        Log.d(TAG,"checkPermissionArray : checking permission array." +permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(CameraActivity.this,permission);

        if(permissionRequest!=PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"checkPermission : \n permission was not granted "+permission);
            return false;
        }
        else {
            Log.d(TAG,"checkPermission : \n permission was  granted "+permission);
            return true;
        }

    }

  /*  @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }*/
}
