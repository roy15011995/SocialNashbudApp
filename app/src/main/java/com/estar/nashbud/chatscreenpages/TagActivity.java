package com.estar.nashbud.chatscreenpages;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.upload_photo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    String GetPostKey,getUid;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference,databaseReferenceUser,ref;
    ImageView Like_ProfilePic;
    TextView Like_ProfileName;
    Button Like;
    FirebaseUser user;
    String Name,Photo;
    User users;
    ArrayList<User> list;
    CustomAdapterLike customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        toolbar = (Toolbar) findViewById(R.id.toolbar_like);
        listView = (ListView) findViewById(R.id.listTag);

        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

        toolbar.setTitle("People Likes");
        Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        close.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(close);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GetPostKey = getIntent().getStringExtra("PostKey").toString();

        if(GetPostKey!=null){
            Log.e("GetPostKey",""+GetPostKey);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").child(GetPostKey);



    }
}
