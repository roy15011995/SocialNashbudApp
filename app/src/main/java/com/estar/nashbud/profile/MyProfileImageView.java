package com.estar.nashbud.profile;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyProfileImageView extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    Bundle extra;
    String PhotoUrl,UserName,GetStatusCheck,PhotoUrlFriend,UserNameFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_image_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        imageView = (ImageView)findViewById(R.id.imageMyProfilePic);
        extra = getIntent().getExtras();
        if(extra!=null)
        {

            GetStatusCheck = extra.getString("Profile");
            Log.e("GetStatus ",""+GetStatusCheck);
        }

        if(GetStatusCheck.equals("FromMyProfile"))
        {
            PhotoUrl = extra.getString("ProfilePhoto");
            UserName = extra.getString("ProfileName");
            Log.e("GetPhotUrlxxxx",""+PhotoUrl);
            SetUserDetails(UserName,PhotoUrl);
        }
        else if(GetStatusCheck.equals("FromFriendsProfile"))
        {
            PhotoUrlFriend = extra.getString("ProfilePhoto");
            UserNameFriend = extra.getString("ProfileName");
            Log.e("GetPhotUrlyyyy",""+PhotoUrl);
            SetUserDetails(UserNameFriend,PhotoUrlFriend);
        }

    }

    private void SetUserDetails(String UserNamexxx,String PhotoUrlxxx)
    {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("@" + UserNamexxx);
            Log.e("GetProfileName ", "" + UserNamexxx);
            Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(close);
            toolbar.setTitleTextColor(Color.parseColor("#000000"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(PhotoUrlxxx!=null)
        {
            Glide.with(MyProfileImageView.this)
                    .load(PhotoUrlxxx)
                    .placeholder(getResources().getDrawable(R.drawable.image_placeholder))
                    .centerCrop()
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        }
    }
}
