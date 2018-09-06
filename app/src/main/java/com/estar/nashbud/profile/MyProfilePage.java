package com.estar.nashbud.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.Adapter_PhotosFolder;
import com.estar.nashbud.camera_package.CameraActivity;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.post.Tag_Model;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.GridViewScrollable;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import xyz.neocrux.suziloader.SuziLoader;

public class MyProfilePage extends AppCompatActivity {

    Button buttonCompleteProfile;
    CircleImageView my_profile_image;
    SharedPreference sharedPreference;
    String my_pic, my_name;
    TextView my_TextViewName, toolbar_name, textWebsite, textBio;
    Context context;
    GridViewScrollable gridView;
    ArrayList<Post_Model> list = new ArrayList<>();


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference, mDatabase ,mMyPostDataBase ;
    ArrayList<String> getPhotoPath = new ArrayList<>();
    GenericTypeIndicator<ArrayList<Post_Model>> t = new GenericTypeIndicator<ArrayList<Post_Model>>() {

    };
    User user;
    String getUid;
    private Menu menu;
    Toolbar toolbar;
    ImageView imageBack;
    Post_Model post_model = new Post_Model();
    private static MyProfilePage ins;
    String[] mStringArray;
    ArrayList<String> getPhoto = new ArrayList<>();
    FloatingActionButton fab;
    Context mContext = this;
    private static final int REQUEST = 112;
    FirebaseUser firebaseUser;
    
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
        setContentView(R.layout.activity_my_profile_page);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        setSupportActionBar(toolbar);
        context = MyProfilePage.this;
        buttonCompleteProfile = findViewById(R.id.button_complete_profile);
        my_profile_image = findViewById(R.id.my_profile_image);
        my_TextViewName = findViewById(R.id.my_TextViewName);
        textBio = findViewById(R.id.textView_profile_status);
        textWebsite = findViewById(R.id.textView_website);
        toolbar_name = findViewById(R.id.text_name);
        gridView = (GridViewScrollable) findViewById(R.id.myProfile_recyclerView);
        fab=(FloatingActionButton)findViewById(R.id.add_profile_fab);
     //   gridView.setVerticalScrollBarEnabled(false);
        gridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
        ins = this;

        setDynamicHeight(gridView);
        sharedPreference = new SharedPreference();
        //my_pic = sharedPreference.getProfilePic(context);
        //my_name = sharedPreference.getUserName(context);
        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        my_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GetPhotoPath = user.getPhotoUrl();
                String UserName =  user.getUserName();
                Intent intent = new Intent(MyProfilePage.this,MyProfileImageView.class);
                intent.putExtra("ProfilePhoto",GetPhotoPath);
                intent.putExtra("ProfileName",UserName);
                intent.putExtra("Profile","FromMyProfile");
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    Log.d("TAG", "@@@ IN IF Build.VERSION.SDK_INT >= 23");
                    String[] PERMISSIONS = {
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    };

                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        Log.d("TAG", "@@@ IN IF hasPermissions");
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
                    } else {
                        Log.d("TAG", "@@@ IN ELSE hasPermissions");
                        callNextActivity();

                    }
                } else {
                    Log.d("TAG", "@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
                    callNextActivity();
                }


            }
        });

        //my_TextViewName.setText(my_name);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("post_images");
         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         getUid=firebaseUser.getUid();

        /* mDatabase.orderByChild("uid").equalTo(getUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    for(final DataSnapshot ds: dataSnapshot.getChildren()) {
                        Post_Model model = ds.getValue(Post_Model.class);
                        Log.e("GetPhotoUrl", "\n" + model.getPostImage());

                        if (model.getAl_imagepath().size() > 0){
                        mStringArray = new String[model.getAl_imagepath().size()];
                        mStringArray = model.getAl_imagepath().toArray(mStringArray);

                        Log.e("mStringArray", "" + mStringArray.length);

                        try {

                            for (int i1 = 0; i1 < mStringArray.length; i1++) {

                                Log.d("StringArrayIs", "" + mStringArray[i1]);
                                Post_Model post_model = new Post_Model();
                                post_model.setImagePath(mStringArray[i1]);
                                list.add(post_model);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (firebaseUser != null) {
                            getUid = firebaseUser.getUid();

                            Log.e("FirebaseMyProfCurUser", "" + getUid);

                            Log.e("PostModelUserId", "" + model.getUid());

                            if (getUid.contains(model.getUid())) {
                                final myProfileAdapter myAdapter = new myProfileAdapter(getApplicationContext(), list);
                                gridView.setAdapter(myAdapter);
                                gridView.setNumColumns(3);

                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                                            Post_Model s=(Post_Model)myAdapter.getItem(position);
                                            s=ds.getValue(Post_Model.class);
                                            String PostKey = s.getPostImage();
                                            Log.e("GetPostKeyMyProfile ",""+PostKey);
                                    }
                                });

                            }
                        }

                    }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        ShowGridViewImage();


        initializeFirebaseAuthListener();


        buttonCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfilePage.this, EditProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public static MyProfilePage getInstace() {
        return ins;
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
                    Log.e("@@@@", "getUid " + getUid);

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
        toolbar.setTitle("");
        toolbar_name.setText(user.getDisplayName());
        my_TextViewName.setText(user.getDisplayName());
        textWebsite.setText(user.getWebsite());
        textBio.setText(user.getBio());
        Log.e("pic & name", "" + user.getPhotoUrl() + " " + my_TextViewName);
        String profileUrl = user.getPhotoUrl();

        try {
            if (!(profileUrl.equals("") || profileUrl.equals("null") || profileUrl.equals(null))) {
                Glide.with(context)
                        .load(user.getPhotoUrl())
                        .placeholder(R.mipmap.profile)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(my_profile_image);
            } else {
                Log.e("profile is", "null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        this.menu = menu;

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
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();
                return true;
            /*case R.id.action_new_group:
                Toast.makeText(context,"new group clicked",Toast.LENGTH_SHORT).show();
                return true;*/
            /*case R.id.action_new_broadcast:
                Toast.makeText(context,"broadcast clicked",Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.action_settings:
                Toast.makeText(context, "settings clicked", Toast.LENGTH_SHORT).show();
                return true;
           /* case R.id.logout:
                Toast.makeText(context, "logout clicked", Toast.LENGTH_SHORT).show();
                return true;*/

            case R.id.feedback:
                /*Toast.makeText(context, "logout clicked", Toast.LENGTH_SHORT).show();*/
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
        mAuth.addAuthStateListener(mAuthListener);

    }



    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > 3){
            x = items/3;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
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

    public void callNextActivity() {
        Intent ss = new Intent(MyProfilePage.this, CameraActivity.class);
        ss.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ss.putExtra("IsPROFILE" , "Yes");
        startActivity(ss);
    }

    public class GridViewAdapter extends FirebaseListAdapter<Post_Model>{

        /**
         * @param activity    The activity containing the ListView
         * @param modelClass  Firebase will marshall the data at a location into
         *                    an instance of a class that you provide
         * @param modelLayout This is the layout used to represent a single list item.
         *                    You will be responsible for populating an instance of the corresponding
         *                    view with the data from an instance of modelClass.
         * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location,
         *                    using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
         */
        public GridViewAdapter(Activity activity, Class<Post_Model> modelClass, int modelLayout, Query ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        public Post_Model getItem(int position) {
            return super.getItem(position);
        }

        @Override
        protected void populateView(View v, Post_Model model, int position) {

            ImageView imageView= (ImageView) v.findViewById(R.id.my_profile_imageView);

            Glide.with(getApplicationContext())
                    .load(model.getPostImage())
                    .placeholder(R.drawable.gallery)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageView);

        }
    }

    public void ShowGridViewImage()
    {
        mDatabase.orderByChild("uid").equalTo(getUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(final DataSnapshot ds: dataSnapshot.getChildren()) {
                        Post_Model model = ds.getValue(Post_Model.class);
                        Log.e("GetPhotoUrl", "\n" + model.getPostImage());

                       /* if (model.getAl_imagepath().size() > 0){
                            mStringArray = new String[model.getAl_imagepath().size()];
                            mStringArray = model.getAl_imagepath().toArray(mStringArray);

                            Log.e("mStringArray", "" + mStringArray.length);

                            try {

                                for (int i1 = 0; i1 < mStringArray.length; i1++) {

                                    Log.d("StringArrayIs", "" + mStringArray[i1]);
                                    Post_Model post_model = new Post_Model();
                                    post_model.setImagePath(mStringArray[i1]);
                                    //list.add(post_model);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

                            if (firebaseUser != null) {
                                getUid = firebaseUser.getUid();

                                Log.e("FirebaseMyProfCurUser", "" + getUid);

                                Log.e("PostModelUserId", "" + model.getUid());

                                if (getUid.contains(model.getUid())) {
                                    //final myProfileAdapter myAdapter = new myProfileAdapter(getApplicationContext(), list);
                                    final GridViewAdapter gridViewAdapter=new GridViewAdapter(MyProfilePage.this,Post_Model.class,
                                            R.layout.my_profile_row,mDatabase.orderByChild("uid").equalTo(getUid));
                                    gridView.setAdapter(gridViewAdapter);
                                    gridView.setNumColumns(3);

                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                                            String GetKey = gridViewAdapter.getRef(position).getKey();
                                            Log.e("GetKeyMyProfile ",""+GetKey);

                                            Intent intent = new Intent(MyProfilePage.this,ShowMyProfilePost.class);
                                            intent.putExtra("PostKey",GetKey);
                                            startActivity(intent);

                                        }
                                    });

                                }
                            }

                       // }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
