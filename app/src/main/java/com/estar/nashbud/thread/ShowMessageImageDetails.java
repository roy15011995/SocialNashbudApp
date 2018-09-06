package com.estar.nashbud.thread;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.profile.MyProfileImageView;

public class ShowMessageImageDetails extends AppCompatActivity {

    Toolbar toolbar;
    Bundle extra;
    String MessagePhoto,UserName,MessageBody;
    ImageView Message_photo;
    TextView MessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message_image_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        Message_photo = (ImageView)findViewById(R.id.Message_photo);
        MessageText = (TextView)findViewById(R.id.text_message);

        extra = getIntent().getExtras();
        if(extra!=null)
        {
            MessagePhoto = extra.getString("MessageImage");
            UserName = extra.getString("Name");
            MessageBody = extra.getString("MessageBody");
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("@" + UserName);
            Log.e("GetProfileName ", "" + UserName);
            Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(close);
            toolbar.setTitleTextColor(Color.parseColor("#000000"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(MessagePhoto!=null)
        {
            Glide.with(ShowMessageImageDetails.this)
                    .load(MessagePhoto)
                    .placeholder(getResources().getDrawable(R.drawable.image_placeholder))
                    .centerCrop()
                    .fitCenter()
                    .dontAnimate()
                    .into(Message_photo);
        }
        if(MessageBody!=null)
        {
            MessageText.setText(MessageBody);
            MessageText.setVisibility(View.VISIBLE);
        }
        else
        {
            MessageText.setVisibility(View.GONE);
        }
    }
}
