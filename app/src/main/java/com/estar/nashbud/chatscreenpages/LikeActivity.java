package com.estar.nashbud.chatscreenpages;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.upload_photo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    String GetPostKey, getUid,GetCommentKey,GetProfileKey;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference, databaseReferenceUser, databaseReferenceCommentsLike,databaseReferencelike;
    ImageView Like_ProfilePic;
    TextView Like_ProfileName;
    Button Like;
    FirebaseUser user;
    String Name, Photo,resultCode;
    User users;
    ArrayList<User> list = new ArrayList<User>();
    CustomAdapterLike customAdapter;
    int i = 0;
    Post_Model postModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        toolbar = (Toolbar) findViewById(R.id.toolbar_like);
        listView = (ListView) findViewById(R.id.listLike);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Likes");
            Drawable close = getResources().getDrawable(R.drawable.back);
            close.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(close);
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        list.clear();
        GetPostKey = getIntent().getStringExtra("PostKey").toString();
        resultCode = getIntent().getStringExtra("resultCode").toString();
        initializeFirebaseAuthListener();

        if (GetPostKey != null) {
            Log.e("GetPostKey", "" + GetPostKey);
        }
        if(resultCode!=null)
        {
            Log.e("ResultCodePrint ",""+resultCode);
        }

        users = new User();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.keepSynced(true);
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceCommentsLike = FirebaseDatabase.getInstance().getReference().child("CommentsLike");
        databaseReferencelike = FirebaseDatabase.getInstance().getReference().child("likes");

        if(resultCode.equals("FromActivity"))
        {
            databaseReference.child(GetPostKey).child("likeInput").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String uid = "";
                    if (dataSnapshot.getValue() != null) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //postModel = postSnapshot.getValue(Post_Model.class);
                            // if (postSnapshot.getChildrenCount() != 0) {
                            Log.e("GetPostKeyxxx", "" + postSnapshot.getValue().toString());
                            getUiFromList(postSnapshot.getValue().toString());
                            //  }
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(resultCode.equals("FromComment"))
        {
            GetCommentKey = getIntent().getStringExtra("PostKey").toString();

            if(GetCommentKey!=null)
            {
                Log.e("GetCommentKey ",""+GetCommentKey);

                databaseReferenceCommentsLike.child(GetCommentKey).child("Like").orderByChild("uid").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            String Uid = ds.child("uid").getValue().toString();
                            Log.e("GetUid :",""+"\n"+Uid);

                            databaseReferenceUser.orderByChild("uid").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for(DataSnapshot Sdata : dataSnapshot.getChildren())
                                    {

                                        users = Sdata.getValue(User.class);

                                        Log.e("Name :",""+"\n"+users.getDisplayName());
                                        Log.e("Photo :",""+"\n"+users.getPhotoUrl());
                                        Log.e("UserIdTable",""+"\n"+users.getUid());

                                        list.add(users);
                                        customAdapter = new CustomAdapterLike(LikeActivity.this,list);
                                        listView.setAdapter(customAdapter);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        else if(resultCode.equals("FromProfile"))
        {
            GetProfileKey = getIntent().getStringExtra("PostKey").toString();

            databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
            databaseReferencelike.child(GetProfileKey).child("Like").orderByChild("uid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String Uid = ds.child("uid").getValue().toString();
                        Log.e("GetUid :",""+"\n"+Uid);

                        databaseReferenceUser.orderByChild("uid").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for(DataSnapshot Sdata : dataSnapshot.getChildren())
                                {

                                    users = Sdata.getValue(User.class);

                                    Log.e("Name :",""+"\n"+users.getDisplayName());
                                    Log.e("Photo :",""+"\n"+users.getPhotoUrl());
                                    Log.e("UserIdTable",""+"\n"+users.getUid());

                                    list.add(users);
                                    customAdapter = new CustomAdapterLike(LikeActivity.this,list);
                                    listView.setAdapter(customAdapter);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
    public  void getUiFromList(String getUid){
        databaseReferenceUser.orderByChild("uid").equalTo(getUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot Sdata : dataSnapshot.getChildren()) {
                    list.add(Sdata.getValue(User.class));
                    customAdapter = new CustomAdapterLike(LikeActivity.this, list);
                    listView.setAdapter(customAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth", "" + mAuth);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                Log.e("current user", "" + user);
                if (user != null) {
                    //addUserToDatabase(user);
                    getUid = user.getUid();
                    Log.e("current user ID", "" + getUid);

                    Log.e("@@@@", "home:signed_in:" + user.getUid());
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };
    }

}
