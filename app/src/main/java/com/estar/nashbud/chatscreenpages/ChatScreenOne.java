package com.estar.nashbud.chatscreenpages;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.bottombarpages.Add_Request_Fragment;
import com.estar.nashbud.bottombarpages.Add_Requests;
import com.estar.nashbud.bottombarpages.CameraFragment;
import com.estar.nashbud.bottombarpages.Discover;
import com.estar.nashbud.bottombarpages.Discover_Fragment;
import com.estar.nashbud.bottombarpages.Events_Fragment;
import com.estar.nashbud.bottombarpages.Home_Fragment;
import com.estar.nashbud.camera_package.CameraActivity;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.profile.EditProfilePage;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.optionmenupages.NewBroadcastPage;
import com.estar.nashbud.optionmenupages.SendFeedBackPage;
import com.estar.nashbud.R;
import com.estar.nashbud.settings.Send_Feedback;
import com.estar.nashbud.settings.Settings_activity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ChatScreenOne extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ImageView iv_nashlist;
    CircleImageView civ_profile_icon;
    RelativeLayout rl_search;
    int backButtonCount = 0;
    BottomNavigationView bottomNavigationView;
    String my_toolbar_pic, mobileFirbase;
    Context context;
    SharedPreference sharedPreference;
    private Menu menu;
    LinearLayout linearToolbar;
    BottomBar bottomBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;
    private static final int REQUEST= 112;

    Context mContext = this;

    User user;
    String getUid;

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // EventBus.getDefault().unregister(this);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen_one);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = ChatScreenOne.this;
        //bottomBar = (BottomBar)findViewById(R.id.bottom_bar);
        sharedPreference = new SharedPreference();
        mobileFirbase = sharedPreference.getPhNo(context);
        //my_toolbar_pic = sharedPreference.getProfilePic(context);

        rl_search = findViewById(R.id.rl_search);

        //iv_nashlist = findViewById(R.id.iv_nashlist);

        civ_profile_icon = findViewById(R.id.civ_profile_icon);
        civ_profile_icon.setOnClickListener(this);
        civ_profile_icon.setVisibility(View.VISIBLE);
        linearToolbar = findViewById(R.id.linear_toolbar);
        //iv_nashlist.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.bb_home:
                        selectedFragment = new Home_Fragment();
                        toolbar.setVisibility(View.VISIBLE);
                        linearToolbar.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bb_discover:
                        selectedFragment = new Discover_Fragment();
                        toolbar.setVisibility(View.GONE);
                        linearToolbar.setVisibility(View.GONE);
                        linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));

                        /*Intent intent1=new Intent(context, Discover.class);
                        startActivity(intent1);*/
                        break;
                    case R.id.bb_camera:
                        //selectedFragment = new CameraFragment();
                        linearToolbar.setVisibility(View.GONE);
                        linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                       /* Intent intent=new Intent(context, CameraActivity.class);
                        startActivity(intent);*/

                        if (Build.VERSION.SDK_INT >= 23) {
                            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
                            String[] PERMISSIONS = {
                                    android.Manifest.permission.CAMERA,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            };


                            if (!hasPermissions(mContext, PERMISSIONS)) {
                                Log.d("TAG","@@@ IN IF hasPermissions");
                                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                            } else {
                                Log.d("TAG","@@@ IN ELSE hasPermissions");
                                callNextActivity();
                            }
                        } else {
                            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
                            callNextActivity();
                        }

                    case R.id.bb_request:
                        selectedFragment = new Add_Requests();
                        linearToolbar.setVisibility(View.GONE);
                        linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                        toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.bb_events:
                        selectedFragment = new Events_Fragment();
                        linearToolbar.setVisibility(View.GONE);
                        linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                        toolbar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(),"Comming soon",Toast.LENGTH_SHORT).show();
                        break;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, selectedFragment);
                transaction.commit();
                return true;
            }
        });


        /*bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            Fragment selectedFragment = null;
            @Override
            public void onTabSelected(int tabId) {
               switch (tabId){

                   case R.id.bb_home:
                       selectedFragment = new Home_Fragment();
                       toolbar.setVisibility(View.VISIBLE);
                       linearToolbar.setVisibility(View.VISIBLE);
                       break;

                   case R.id.bb_discover:
                       selectedFragment = new Discover_Fragment();
                       toolbar.setVisibility(View.GONE);
                       linearToolbar.setVisibility(View.GONE);
                       linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));

                        *//*Intent intent1=new Intent(context, Discover.class);
                       startActivity(intent1);*//*
                       break;
                   case R.id.bb_camera:
                       //selectedFragment = new CameraFragment();
                       linearToolbar.setVisibility(View.GONE);
                       linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                       Intent intent=new Intent(context, CameraActivity.class);
                       startActivity(intent);

                   case R.id.bb_request:
                       selectedFragment = new Add_Requests();
                       linearToolbar.setVisibility(View.GONE);
                       linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                       toolbar.setVisibility(View.GONE);
                       break;
                   case R.id.bb_events:
                       selectedFragment = new Events_Fragment();
                       linearToolbar.setVisibility(View.GONE);
                       linearToolbar.setBackgroundColor(Color.parseColor("#ffffff"));
                       toolbar.setVisibility(View.GONE);
                       break;
               }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new Home_Fragment());
                transaction.commit();
                return;
            }
        });*/

        //Manually displaying the first fragment - one time only

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new Home_Fragment());
        transaction.commit();
        initializeFirebaseAuthListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    callNextActivity();
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    //Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void callNextActivity()
    {

        Intent ss = new Intent(ChatScreenOne.this, CameraActivity.class);
        ss.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(ss);
    }

    public void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    //addUserToDatabase(firebaseUser);
                    getUid = firebaseUser.getUid();
                    loadUserDetails();
                    user = new User();
                    Log.e("@@@@", "home:signed_in:" + firebaseUser.getUid());
                    Log.e("@@@@", "getUid " + getUid );

                } else {
                    Log.e("@@@@", "home:signed_out");
                    /*Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();*/
                }
            }
        };
    }

    private void loadUserDetails() {
        DatabaseReference userReference = databaseReference
                .child("users")
                .child(getUid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                displayUserDetails();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, R.string.error_loading_user, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayUserDetails() {

        //sharedPreference.saveProfilePic(context,profileUrl);
        try {
            Log.e("pic & name","" + user.getPhotoUrl() );
            String profileUrl = user.getPhotoUrl();
            if (!(profileUrl.equals("") || profileUrl.equals("null") || profileUrl.equals(null))) {
                Glide.with(context)
                        .load(profileUrl)
                        .placeholder(R.mipmap.profile)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(civ_profile_icon);
            } else {
                Log.e("profile is", "null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ((v == civ_profile_icon)) {
            Intent intent = new Intent(ChatScreenOne.this, MyProfilePage.class);
            startActivity(intent);
        }
        /*if ((v == iv_nashlist)) {
            Intent intent = new Intent(ChatScreenOne.this, ContactList.class);
            startActivity(intent);
        }*/
    }

    @Override
    public void onBackPressed() {

        if (backButtonCount >= 1) {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button to exit.", Toast.LENGTH_LONG).show();
            backButtonCount++;
        }
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(ChatScreenOne.this, .class);
//        startActivity(i);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    circleReveal(R.id.searchtoolbar, 1, true, true);
//                else
//                    searchtollbar.setVisibility(View.VISIBLE);
//
//                item_search.expandActionView();
//                return true;
            case R.id.action_profile:
                Intent intent = new Intent(ChatScreenOne.this, EditProfilePage.class);
                startActivity(intent);
                return true;
            /*case R.id.action_new_group:
                Intent intent1 = new Intent(ChatScreenOne.this, NewGroupPage.class);
                startActivity(intent1);
                return true;*/
            case R.id.action_settings:
                Intent intent3 = new Intent(ChatScreenOne.this, Settings_activity.class);
                startActivity(intent3);
                return true;

            /*case R.id.logout:
                sharedPreference.clearPhNo(context);
                finish();
                return true;*/

            case R.id.feedback:
                Intent intent4 = new Intent(ChatScreenOne.this, Send_Feedback.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
