package com.estar.nashbud.chatscreenpages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.post.Tag_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class EditPost extends AppCompatActivity {

    Toolbar toolbar;
    String GetDescription,GetProfilePic,GetProfileName,GetLocation,GetPostKey;
    EditText editTextPost;
    TextView Profile_Name,Location;
    ImageView profilePic,tick_gray,tick_black;
    ArrayList<String> getPhotoArrayList;
    String[] mStringArray,stringArray;
    ArrayList<Post_Model> list=new ArrayList<>();
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    DatabaseReference databaseReference,databaseReferenceDiary,databaseReferenceMyProfile;
    String GetText;
    ProgressDialog progressDialog;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_edit_post);
         toolbar = (Toolbar) findViewById(R.id.toolbar_all);
         tick_black = (ImageView)findViewById(R.id.tick_black);
         //tick_gray = (ImageView)findViewById(R.id.tick_gray);

        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

        toolbar.setTitle("Edit"+" "+" post");
        Drawable close = getResources().getDrawable(R.drawable.ic_close_black_24dp);
        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(close);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextPost = (EditText)findViewById(R.id.editpost_whats_going_on);
        Profile_Name = (TextView)findViewById(R.id.profile_name);
        Location = (TextView)findViewById(R.id.profile_location);
        profilePic = (ImageView) findViewById(R.id.edit_pic);
        vp_slider = (ViewPager) findViewById(R.id.vp_slider_edit);

        //ll_dots = (LinearLayout) findViewById(R.id.all_dots);
        extras = getIntent().getExtras();
        if (extras != null) {
            GetDescription = getIntent().getStringExtra("GetDescription");

            GetProfileName = getIntent().getStringExtra("GetName");
            GetProfilePic = getIntent().getStringExtra("GetImage");
            GetLocation = getIntent().getStringExtra("GetLocation");
            getPhotoArrayList = getIntent().getStringArrayListExtra("PhotoList");
            GetPostKey = getIntent().getStringExtra("PostKey");

            Log.e("Description Getting",""+GetDescription);
        }


        if(Location.getText().equals(null))
        {
            Location.setVisibility(View.GONE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReferenceDiary = FirebaseDatabase.getInstance().getReference().child("Diary");
        databaseReferenceMyProfile = FirebaseDatabase.getInstance().getReference().child("post_images");
        progressDialog = new ProgressDialog(this);



        editTextPost.addTextChangedListener(textwatcher);

        if(GetPostKey!=null){
            Log.e("GetPostKey",""+GetPostKey);
        }



        if(getPhotoArrayList!=null){
            Log.e("GetListItemList",""+getPhotoArrayList);

            /*lv_arr = (String[]) GetListItemList.toArray();

            Log.e("ListStringToArray",""+lv_arr);*/

             /*Object[] objectList = GetListItemList.toArray();
             stringArray = Arrays.copyOf(objectList,objectList.length,String[].class);*/

            mStringArray = new String[getPhotoArrayList.size()];
            mStringArray = getPhotoArrayList.toArray(mStringArray);

            Log.e("mStringArray",""+mStringArray.length);

            for (int i1=0;i1<mStringArray.length;i1++){

                //list_tag.add(i1,new Tag_Model(GetListItemList));

                Post_Model post_model = new Post_Model();

                post_model.setImagePath(mStringArray[i1]);

                list.add(post_model);
            }

        }

           if(!(GetDescription == null))
           {
               Log.e("DescriptionGetted",""+GetDescription);
               editTextPost.setText(GetDescription);
           }

           else
           {
               editTextPost.setText("");
           }



           if(GetProfileName!=null)
           {
               Profile_Name.setText(GetProfileName);
               Log.e("GetProfileName",""+GetProfileName);
           }

           if(GetProfilePic!=null)
              {
               Glide.with(getApplicationContext())
                       .load(GetProfilePic)
                       .crossFade()
                       .centerCrop()
                       .fitCenter()
                       .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                       .dontAnimate()
                       .into(profilePic);
               Log.e("GetProfilePic",""+GetProfilePic);
              }

              if(GetLocation != null)
              {
                  Location.setText(GetLocation);
                  Log.e("GetLocation",""+GetLocation);
                  Location.setVisibility(View.VISIBLE);
                  Log.e("IfCalled","Called");
              }


        // method for initialisation
        init();

        // method for adding indicators

        //addBottomDots(0);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

    }

    TextWatcher textwatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
             Log.e("CharSequence Count",""+count);

             if(editTextPost.getText().toString().length()==count)
             {
                 tick_black.setVisibility(View.GONE);
             }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            tick_black.setVisibility(View.VISIBLE);
            //Do what ever you want here after you enter each text

            tick_black.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setTitle("Editing on Progress.....");
                    progressDialog.setMessage("Please wait....");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    databaseReference.child(GetPostKey).child("postMessage").setValue(editTextPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete())
                            {

                                databaseReferenceDiary.child(GetPostKey).child("postMessage").setValue(editTextPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isComplete()){

                                            databaseReferenceMyProfile.child(GetPostKey).child("postMessage").setValue(editTextPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditPost.this,"Editted Successfully",Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                            }
                                    }
                                });

                            }
                            else
                            {
                                String messages = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),"Failed to Upload...Pls Try again Later "+messages,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });

        }
    };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void init() {


        sliderPagerAdapter = new SliderPagerAdapter(EditPost.this,list);
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

    private void addBottomDots(int currentPage) {
        dots = new TextView[list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(Color.parseColor("#616161"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#ff33b5e5"));
            dots[currentPage].setTextSize(35);
    }

}
