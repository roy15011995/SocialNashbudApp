package com.estar.nashbud.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.widgets.EmptyStateRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class Block_Contact extends AppCompatActivity {
ImageView imageback;
TextView text_topic;
    EmptyStateRecyclerView usersRecycler;
    @BindView(R.id.activity_main_empty_view)
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    Context context;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block__contact);
        imageback=(ImageView)findViewById(R.id.image_back);
        text_topic=(TextView)findViewById(R.id.text_topic);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Blocked Contacts");
        //ButterKnife.bind((Activity) getApplicationContext());
        context = getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.activity_main_users_recycler_block);
        floatingActionButton=findViewById(R.id.fab_user);
        //floatingActionButton.setVisibility(View.VISIBLE);
        sharedPreference = new SharedPreference();
        initializeFirebaseAuthListener();
        initializeUsersRecycler();

    }

    private void initializeUsersRecycler() {
        UsersAdapterBlock adapter = new UsersAdapterBlock(this.getApplicationContext(), mDatabase.child("users"));
        Log.e("mDatabase","" + mDatabase.child("users"));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        //usersRecycler.setEmptyView(emptyView);
    }

    private void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth","" + mAuth );
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("current user","" + user );
                if (user != null) {
                    //addUserToDatabase(user);
                    getUid = user.getUid();
                    Log.e("@@@@", "home:signed_in:" + user.getUid());
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };
    }

    private void addUserToDatabase(FirebaseUser firebaseUser) {
        user = new User();
        user.setDisplayName("saurabh");
        user.setUid(getUid);
        //user.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());

        databaseReference.child("users")
                .child(user.getUid()).setValue(user);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            databaseReference.child("users")
                    .child(getUid)
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserSelected(DatabaseReference selectedRef) {

        Intent thread = new Intent(context, ThreadActivity.class);
        thread.putExtra(Constants.USER_ID_EXTRA, selectedRef.getKey());
        Log.e("thread activity","" + selectedRef.getKey());
        startActivity(thread);

    }


}
