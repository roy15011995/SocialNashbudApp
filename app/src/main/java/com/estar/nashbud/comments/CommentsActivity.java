package com.estar.nashbud.comments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.BaseActivity;
import com.estar.nashbud.BuildConfig;
import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.CameraActivity;
import com.estar.nashbud.chatscreenpages.LastSeenTime;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.upload_photo.Message;
import com.estar.nashbud.upload_photo.UploadPhoto;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.utils.Utility;
import com.estar.nashbud.widgets.EmptyStateRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class CommentsActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.Comments_messages_recycler)
    EmptyStateRecyclerView messagesRecycler;
    @BindView(R.id.Comments_send_fab)
    FloatingActionButton sendFab;
    @BindView(R.id.Comments_input_edit_text)
    TextInputEditText inputEditText;
    @BindView(R.id.Comments_empty_view)
    TextView emptyView;
    @BindView(R.id.Comments_editor_parent)
    RelativeLayout editorParent;
    @BindView(R.id.Comments_thread_progress)
    ProgressBar progress;

    private DatabaseReference mDatabase,mDatabaseComments,mDatabasebaseCommentsCurrentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @State
    String userUid;
    @State
    boolean emptyInput;
    private boolean hidden = true;
    private User user;
    private FirebaseUser owner;
    private TextView textUserName,text_status;
    private ImageView profilePic,imageBack,imageAttach,single_tick;
    Context context;

    private Menu menu;
    Toolbar toolbar;

    Uri FilePathUri;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;

    InternetUtil internetUtil;
    Boolean isConnect;
    SharedPreference sharedPreference;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences,sharedPreferences1;
    int count=0;
    Bundle Extra;
    String ProfileName,ProfilePic,GetUid,GetPostKey;
    ImageView myProfilePic;
    FirebaseUser firebaseUser;
    String CurrentUser,CurrentProfilename,CurrentProfilePic;
    String body;
    long timestamp,dayTimestamp;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.activity_comments_toolbar);
        myProfilePic = (ImageView)findViewById(R.id.Comments_profile_image);
        sendFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        context = CommentsActivity.this;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseComments = FirebaseDatabase.getInstance().getReference();
        mDatabasebaseCommentsCurrentUser = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUser = firebaseUser.getUid();



        Extra = getIntent().getExtras();

        if(Extra!=null)
        {
            ProfileName = Extra.getString("GetName");
            ProfilePic = Extra.getString("GetImage");
            GetUid = Extra.getString("GetUid");
            GetPostKey = Extra.getString("GetPostKey");

            Log.e("GetPostKey",""+ GetPostKey);
        }

      /*  if (savedInstanceState == null) {
            userUid = getIntent().getStringExtra(Constants.USER_ID_EXTRA);
            Log.e("user id from thread","" + userUid);

        }*/

        sendFab.requestFocus();
        imageBack = findViewById(R.id.Comments_image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("PostKey",GetPostKey);
                setResult(0,returnIntent);
                finish();
            }
        });
        //loadUserDetails();
        initializeAuthListener();
        initializeInteractionListeners();

        mDatabase.child("users").child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CurrentProfilePic = dataSnapshot.child("photoUrl").getValue().toString();

                    Glide.with(context)
                            .load(CurrentProfilePic)
                            .placeholder(R.drawable.profile)
                            .crossFade()
                            .centerCrop()
                            .fitCenter()
                            .bitmapTransform(new CropCircleTransformation(context))
                            .dontAnimate()
                            .into(myProfilePic);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void initializeInteractionListeners() {
        inputEditText.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }


    }


    private void initializeAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                owner = firebaseAuth.getCurrentUser();
                if (owner != null) {
                    initializeMessagesRecycler();
                    Log.d("@@@@", "thread:signed_in:" + owner.getUid());
                } else {
                    Log.d("@@@@", "thread:signed_out");
                    Intent login = new Intent(CommentsActivity.this, UploadPhoto.class);
                    startActivity(login);
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void initializeMessagesRecycler() {

        Query messagesQuery = mDatabaseComments
                .child("Comments")
                .child(GetPostKey)
                .child("item")
                .orderByChild("negatedTimestamp");
        CommentsAdapter adapter = new CommentsAdapter(this, CurrentUser, messagesQuery);
        //messagesRecycler.setAdapter(null);
        messagesRecycler.setAdapter(adapter);
        messagesRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        messagesRecycler.setEmptyView(emptyView);
        messagesRecycler.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                messagesRecycler.smoothScrollToPosition(0);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.Comments_send_fab)
    public void onClick() {

         body = inputEditText.getText().toString().trim();


        if (body.length() > 0){
             timestamp = new Date().getTime();
             dayTimestamp = getDayTimestamp(timestamp);

            mDatabase.child("users").child(CurrentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        CurrentProfilename = dataSnapshot.child("displayName").getValue().toString();
                        CurrentProfilePic = dataSnapshot.child("photoUrl").getValue().toString();
                        String CurrrentUserSnapshot = dataSnapshot.child("uid").getValue().toString();

                            Comments message =
                                    new Comments(timestamp, -timestamp, dayTimestamp, body, CurrentUser,CurrentProfilename,CurrentProfilePic);

                            mDatabaseComments
                                    .child("Comments")
                                    .child(GetPostKey)
                                    .child("item")
                                    .push()
                                    .setValue(message);
                            inputEditText.setText("");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void displayLoadingState() {
        //was considering a progress bar but firebase offline database makes it unnecessary

        //TransitionManager.beginDelayedTransition(editorParent);
        progress.setVisibility(isLoading ? VISIBLE : INVISIBLE);
        //displayInputState();
    }

    private void displayInputState() {
        //inputEditText.setEnabled(!isLoading);
        sendFab.setEnabled(!emptyInput && !isLoading);
        //sendFab.setImageResource(isLoading ? R.color.colorTransparent : R.drawable.ic_send);
    }

    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void afterTextChanged(Editable s) {
        emptyInput = s.toString().trim().isEmpty();
        String TextInput = s.toString().trim();
        if(TextInput.length()>0)
        {
            sendFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00aadc")));
        }
        else
        {
            sendFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        }
        displayInputState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        this.menu = menu;
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_profile:
                Intent i = new Intent(context, FriendsProfileActivity.class);
                i.putExtra("user_id",userUid);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            super.onMessageReceived(remoteMessage);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("PostKey",GetPostKey);
            setResult(0,returnIntent);
            finish();

        }
        return true;
    }

}
