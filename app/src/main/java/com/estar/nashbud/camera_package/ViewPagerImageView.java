package com.estar.nashbud.camera_package;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.EditPost;
import com.estar.nashbud.chatscreenpages.SliderPagerAdapter;

import java.util.ArrayList;

public class ViewPagerImageView extends AppCompatActivity {

    ArrayList<ImageModel> photoModels = new ArrayList<>();
    Toolbar toolbar;
    ImageModel imageModel;
    Bundle bundle;
    String[] mStringArray;
    SliderPagerImageAdapter sliderPagerAdapter;
    ViewPager vp_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_image_view);

        Intent i=getIntent();
        ArrayList<String> Image = i.getStringArrayListExtra("image");
        toolbar = (Toolbar)findViewById(R.id.toolbar_all);
        bundle = getIntent().getExtras();
        vp_slider = (ViewPager)findViewById(R.id.vp_slider_imageView);

        imageModel = new ImageModel(Image);

        Log.e("ImageShowActivity :",""+Image);

        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }


        toolbar.setTitle(" ");
        Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(close);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mStringArray = new String[Image.size()];
        mStringArray = Image.toArray(mStringArray);

        for(int i1=0;i1<mStringArray.length;i1++){
            Log.e("Image_Size",""+Image.size());

            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(mStringArray[i1]);
            photoModels.add(imageModel);

            //photoModels.add(i1,new ImageModel(Image));


            Log.e("GetBreakImage :",""+new ImageModel(Image).getAl_imagepath().get(i1));


        }

        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next_photo, menu);
        /*SpannableString s = new SpannableString("My red MenuItem");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.next_photo:
                Intent intent = new Intent("com.estar.nashbud.USER_ACTION");
                intent.putStringArrayListExtra("GetPhoto",imageModel.getAl_imagepath());
                finish();
                sendBroadcast(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {


        sliderPagerAdapter = new SliderPagerImageAdapter(ViewPagerImageView.this, photoModels);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
