package com.estar.nashbud.chatscreenpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
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
import butterknife.ButterKnife;

/**
 * Created by User on 07-12-2017.
 */

public class ChatScreenTest extends AppCompatActivity{
    //@BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_users_recycler)
    EmptyStateRecyclerView usersRecycler;
    @BindView(R.id.activity_main_empty_view)
    TextView emptyView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    String database_Path = "All_NEW_DATA";

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
        setContentView(R.layout.chats_final_fragment);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeFirebaseAuthListener();
        initializeUsersRecycler();
    }

    private void initializeUsersRecycler() {
        UsersAdapter adapter = new UsersAdapter(this, mDatabase.child("users"));
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        usersRecycler.setEmptyView(emptyView);
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
                    Log.e("get uid","" + getUid);
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
        user.setPhotoUrl("fhsdhfhksjdhfhwkjhfkjhksdjfhkhkjashkjhjfkadhgkfdh");

        databaseReference.child("users")
                .child(firebaseUser.getUid()).setValue(user);

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
        Intent thread = new Intent(this, ThreadActivity.class);
        thread.putExtra(Constants.USER_ID_EXTRA, selectedRef.getKey());
        startActivity(thread);
    }
}
