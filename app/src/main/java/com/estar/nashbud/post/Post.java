package com.estar.nashbud.post;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.CameraActivity;
import com.estar.nashbud.camera_package.ImageModel;
import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.profile.EditProfilePage;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import xyz.neocrux.suziloader.SuziLoader;

public class Post extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linear_photo_grid, linear_photo, linear_location, linear_location_layout, linear_tag, linear_tag_layout;
    Context context;
    GridView gridView, gridView1;
    public static int[] prgmImages = {R.drawable.user_settings, R.drawable.television, R.drawable.synchronization_arrows, R.drawable.notifications_setting, R.drawable.info};
    ArrayList<Post_Model> list = new ArrayList<>();
    ArrayList<Post_Model> list_post_image = new ArrayList<>();
    ArrayList<Tag_Model> list_tag = new ArrayList<>();
    LinearLayout view_location;
    Bundle Extras, extra;
    public static String place, Address;
    TextView Post_Place_Name, Post_Address_Name, text_topic;
    ImageView Post_Place_Cross, Image_back;
    // ArrayList<String>arrayList=new ArrayList<>();
    private static String getPhoto ;
    private static ArrayList<String> GetListItemList = new ArrayList<>();
    EditText description_post;
    String Storage_Path = "Posted_Images/";
    StorageReference storageReference;

    DatabaseReference databaseReference, PostReference, PostImageReference, PostDiaryReference;
    FirebaseAuth mAuth;
    String Current_User_Id;
    String CurrentDate, CurrentTime, RandomSave, description, downloadUri, GetPhoto, GetDescription, CurrentTimeStamp;
    Button Btn_Post;
    Uri imageUri;
    ProgressDialog progressDialog;
    Uri imageUri1;
    //String[]GetListItemList = new String[]{};
    CheckBox checkBox;
    private String[] photo;
    String[] mStringArray, stringArray;
    InternetUtil internetUtil = new InternetUtil();
    ArrayList<String> AddPhotoPath = new ArrayList<>();
    ArrayList<String> likeCountUid = new ArrayList<>();
    ArrayList<String> TaggedPeople = new ArrayList<>();
    MyReceiver myReceiver = new MyReceiver();
    Bundle Extra;
    private static Post ins;
    int REQUEST_CODE = 10;
    ProgressDialog progressDialog1;

    public static final String MainPP_SP = "MainPP_data";
    public static final int R_PERM = 2822;
    private static final int REQUEST = 112;
    Context mContext = this;
    String getCode,getVal;
    FrameLayout frame;
    ImageView imageShow;
    ImageView imageShowcross;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        frame = (FrameLayout) findViewById(R.id.frame);
        linear_photo = (LinearLayout) findViewById(R.id.linear_photo);
        linear_photo_grid = (LinearLayout) findViewById(R.id.linear_photo_grid);
        linear_location = (LinearLayout) findViewById(R.id.linear_location);
        linear_location_layout = (LinearLayout) findViewById(R.id.linear_location_layout);
        linear_tag = (LinearLayout) findViewById(R.id.linear_tag);
        linear_tag_layout = (LinearLayout) findViewById(R.id.linear_tag_grid);
        view_location = (LinearLayout) findViewById(R.id.linear_view_location);
        Post_Place_Cross = (ImageView) findViewById(R.id.post_address_cross);
        Image_back = (ImageView) findViewById(R.id.image_back);
        description_post = (EditText) findViewById(R.id.edit_whats_going_on);
        Btn_Post = (Button) findViewById(R.id.btn_post);
        checkBox = (CheckBox) findViewById(R.id.check_post);
        text_topic = (TextView) findViewById(R.id.text_topic);
        imageShow = (ImageView) findViewById(R.id.image_show);
        if (getIntent().getStringExtra("type")  !=  "Shoot"){
            imageShow.setRotation(0F);
        }else{
            imageShow.setRotation(0F);
        }

        imageShowcross = (ImageView) findViewById(R.id.image_post_grid_cross);
        imageShowcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_photo_grid.setVisibility(View.GONE);
                linear_photo.setVisibility(View.VISIBLE);
                imageShow.setImageResource(0);
                getPhoto = "";
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                checkBox.setTextColor(getResources().getColor(R.color.colorGrey));
                Btn_Post.setBackground(getResources().getDrawable(R.drawable.before_text_btn));

            }
        });
        Image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        linear_photo.setOnClickListener(this);
        linear_location.setOnClickListener(this);
        linear_tag.setOnClickListener(this);
        //Post_Place_Cross.setOnClickListener(this);
        Btn_Post.setOnClickListener(this);
        ins = this;
        checkBox.setEnabled(false);

        description_post.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                SharedPreferences.Editor editor = getSharedPreferences("NashBud", MODE_PRIVATE).edit();
                editor.putString("PostString", description_post.getText().toString());
                editor.commit();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                     Btn_Post.setBackground(getResources().getDrawable(R.drawable.button_post));
                }

                if (s.length() == 0){
                    Btn_Post.setBackground(getResources().getDrawable(R.drawable.before_text_btn));
                }

            }
        });

        //progressDialog1.dismiss();

        //getPhoto = getIntent().getStringArrayListExtra("GetPhoto");
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Current_User_Id = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        PostReference = FirebaseDatabase.getInstance().getReference().child("posts");
        PostImageReference = FirebaseDatabase.getInstance().getReference().child("post_images");
        PostDiaryReference = FirebaseDatabase.getInstance().getReference().child("Diary");
        progressDialog = new ProgressDialog(this);

        Extra = getIntent().getExtras();
        Log.i("ExtraXyz", Extra.toString());
        if (Extra != null) {
            getCode = Extra.getString("SendCode");
            getVal = Extra.getString("IsPROFILE");
            Log.e("GetCode", "" + getCode);
            Log.e("GetValPost ",""+getVal);
        }
        if (getCode != null) {
            if (getCode.equals("120")) {
                checkBox.setChecked(true);
            } else if (getCode.equals("100")) {
                checkBox.setChecked(false);
            }
        }
        if(getVal!=null)
        {
            if(getVal.equals("Yes"))
            {
                checkBox.setChecked(true);
            }
            else
            {
                checkBox.setChecked(false);
            }
        }
        try {
            Log.e("GetGalleryPhoto", "" + Extra.getString("GetImage"));
            if (Extra.getString("GetImage") != null) {
                getPhoto = Extra.getString("GetImage");
               Log.e("GetGalleryPhoto  2", "" + Extra.getString("GetImage"));
              Post.getInstace().updateGridViewPhoto(getPhoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_topic.setText("Post");



        if(getPhoto!=null)
        {
            Btn_Post.setBackground(getResources().getDrawable(R.drawable.button_post));
        }
        else
        {
            Btn_Post.setBackground(getResources().getDrawable(R.drawable.before_text_btn));
        }
    }

    public static Post getInstace() {
        return ins;
    }

    public void updateGridViewPhoto(final String getGalleryPhoto) {
        Log.e("GetGalleryPhoto  3", "" + getGalleryPhoto);
        if (getGalleryPhoto == null) {
            linear_photo_grid.setVisibility(View.GONE);
            linear_photo.setVisibility(View.VISIBLE);
            checkBox.setTextColor(Color.parseColor("#BDBDBD"));
        }
        if (getGalleryPhoto != null) {
            checkBox.setTextColor(getResources().getColor(R.color.black_de));
            checkBox.setEnabled(true);
            //checkBox.setTextColor(getResources().getColor(R.color.black_de));
            Log.e("GetGalleryPhotoOnUi", "" + getGalleryPhoto);
            linear_photo_grid.setVisibility(View.VISIBLE);
            linear_photo.setVisibility(View.GONE);

            SharedPreferences prefs = getSharedPreferences("NashBud", MODE_PRIVATE);
            String restoredText = prefs.getString("PostString", null);
            description_post.setText(restoredText);
            description_post.setSelection(description_post.getText().length());

            if (restoredText != null) {
                String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            }

            checkBox.setTextColor(Color.parseColor("#000000"));
            Log.e("GetPhotoForPost", "" + getGalleryPhoto);
                        if(getGalleryPhoto.contains(".jpg")||getGalleryPhoto.contains(".jpge")
                                ||getGalleryPhoto.contains(".png")||getGalleryPhoto.contains(".JPG")){
                            Glide.with(Post.this)
                                    .load(getGalleryPhoto)
                                    .placeholder(R.drawable.gallery)
                                    .dontAnimate()
                                    .fitCenter()
                                    .error(R.drawable.warning)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .skipMemoryCache(true)
                                    .into(imageShow);

                            //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                        }
                        else if(getGalleryPhoto.contains(".gif")){
                            Glide.with(Post.this)
                                    .load(getGalleryPhoto)
                                    .placeholder(R.drawable.gallery)
                                    .dontAnimate()
                                    .fitCenter()
                                    .error(R.drawable.warning)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .skipMemoryCache(true)
                                    .into(imageShow);

                            //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                        }
                        else if(getGalleryPhoto.contains(".mp4")){

                        /*Glide.with(context)
                                .load(imageModel.getAl_imagepath().get(y))
                                .asBitmap()
                                .videoDecoder(decoder)
                                .override(50,50)
                                .placeholder(R.drawable.gallery)
                                .error(R.drawable.warning)
                                .into(holder.image);*/

                            SuziLoader loader = new SuziLoader(); //Create it for once
                            loader.with(Post.this) //Context
                                    .load(getGalleryPhoto) //Video path
                                    .into(imageShow) // imageview to load the thumbnail
                                    .type("mini") // mini or micro
                                    .show(); // to show the thumbnail
                        }

                    // GridView for Photo in Post
                    gridView = (GridView) findViewById(R.id.grid_view_photo);
                    gridView.setVisibility(View.GONE);
                    int size = list.size();
                    int totalWidth = (143 * size) * 2;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            totalWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

                    Post_Adapter post_adapter = new Post_Adapter(getApplicationContext(), list ,linear_photo_grid,
                            linear_photo);

                    gridView.setLayoutParams(params);
                    gridView.setStretchMode(GridView.STRETCH_SPACING);
                    gridView.setNumColumns(size);
                    gridView.setAdapter(post_adapter);

                    Log.e("Grid_Data", gridView.toString());


//
//            try {
//              //  for (int i = 0; i < stringArray.length; i++) {
//                //    Log.e("Image_Size", "" + getGalleryPhoto.size());
//                    Post_Model post_model = new Post_Model();
//                    post_model.setImagePath(getGalleryPhoto);
//                    list.add(post_model);
//                    //list.add(i,new Post_Model(getPhoto));
//
//                    imageUri = Uri.fromFile(new File(getGalleryPhoto));
//                    Log.e("ImageUriPost", "" + imageUri);
//               // }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }


    }

    public void updateTheTextView(final String t, final String t1) {
        Post.this.runOnUiThread(new Runnable() {
            public void run() {
                final TextView textV1 = (TextView) findViewById(R.id.post_place_name);
                Post_Address_Name = (TextView) findViewById(R.id.post_address_name);

                textV1.setText(t);
                Post_Address_Name.setText(t1);
                linear_location_layout.setVisibility(View.VISIBLE);
                view_location.setVisibility(View.VISIBLE);
                linear_location.setVisibility(View.GONE);
                Log.e("GetLocationOncreat", "" + t);
                Post_Place_Cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linear_location_layout.setVisibility(View.GONE);
                        linear_location.setVisibility(View.VISIBLE);
                        view_location.setVisibility(View.GONE);
                        textV1.setText("");

                        Post_Address_Name.setText("");
                    }
                });
            }
        });
    }

    public void updateGridViewTagPeople(final ArrayList<String> getTagPeople) {
        if (getTagPeople != null) {
            Log.e("GetTagPeopleOnUi", "" + getTagPeople);

            mStringArray = new String[getTagPeople.size()];
            mStringArray = getTagPeople.toArray(mStringArray);

            Log.e("mStringArray", "" + mStringArray.length);

            try {

                for (int i1 = 0; i1 < mStringArray.length; i1++) {

                    //list_tag.add(i1,new Tag_Model(GetListItemList));
                    Log.d("StringArrayIs", "" + mStringArray[i1]);
                    Tag_Model tag_model = new Tag_Model();
                    tag_model.setName(mStringArray[i1]);
                    list_tag.add(tag_model);
                }

                linear_tag_layout.setVisibility(View.VISIBLE);
                linear_tag.setVisibility(View.GONE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Post.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // GridView for tagName in Post
                gridView1 = (GridView) findViewById(R.id.listview1);
                int size_tag = list_tag.size();
                int totalWidth_tag = (143 * size_tag) * 2;

                LinearLayout.LayoutParams params_tag = new LinearLayout.LayoutParams(
                        totalWidth_tag, LinearLayout.LayoutParams.WRAP_CONTENT);

                gridView1.setLayoutParams(params_tag);
                gridView1.setStretchMode(GridView.STRETCH_SPACING);
                gridView1.setNumColumns(size_tag);
                gridView1.setAdapter(new Tag_Adapter(getApplicationContext(), list_tag));
            }
        });
    }

    public static class MyReceiver extends BroadcastReceiver {
        Bundle Extra;
        String[] TagPeople;

        @Override
        public void onReceive(Context context, Intent intent) {
            Extra = intent.getExtras();
            if (Extra != null) {

                if (Extra.containsKey("GetLocation") && Extra.containsKey("GetAddress")) {
                    Log.e("GetLocationValue", "" + Extra.get("GetLocation"));
                    Log.e("GetAddressValue", "" + Extra.get("GetAddress"));
                    place = (String) Extra.get("GetLocation");
                    Address = (String) Extra.get("GetAddress");
                    Post.getInstace().updateTheTextView(place, Address);
                } else if (Extra.containsKey("ArrayListData")) {
                    if (Extra.containsKey("ArrayListData")) {
                        GetListItemList = Extra.getStringArrayList("ArrayListData");
                        Log.e("GetTagPeople", "" + GetListItemList);
                        Post.getInstace().updateGridViewTagPeople(GetListItemList);
                    }
                } else if (Extra.containsKey("GetImage")) {

                    try {
                        getPhoto = Extra.getString("GetImage");
                        Log.e("GetGalleryPhoto", "" + getPhoto);
                        Post.getInstace().updateGridViewPhoto(getPhoto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_photo:
                /*linear_photo_grid.setVisibility(View.VISIBLE);
                linear_photo.setVisibility(View.GONE);*/
               /* progressDialog1 = new ProgressDialog(getApplicationContext());
                progressDialog1.setTitle("Gallery is Loading...");
                progressDialog1.setMessage("Please wait....");
                progressDialog1.show();*/

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

                break;

            case R.id.linear_location:
                /*linear_location_layout.setVisibility(View.VISIBLE);
                linear_location.setVisibility(View.GONE);*/
                linear_location.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Post.this, PlacesAutoCompleteActivity.class);
                startActivity(intent);
                break;

            case R.id.linear_tag:

                Intent intent1 = new Intent(Post.this, Tag_People.class);
                startActivity(intent1);
                break;


            case R.id.post_address_cross:
                linear_location_layout.setVisibility(View.GONE);
                linear_location.setVisibility(View.VISIBLE);
                view_location.setVisibility(View.GONE);
                Post_Place_Name.setText("");
                Post_Address_Name.setText("");
                break;


            case R.id.btn_post:

               // Toast.makeText(getApplicationContext(), description_post.getText().toString()  , Toast.LENGTH_SHORT).show();
                if (!description_post.getText().toString() .equals("")) {
                    validatePostInfo();
                }else{
                    Toast.makeText(getApplicationContext(), "Enter Status for your Feed . " , Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    callNextActivity();
                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied");
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

    public void callNextActivity() {
        Intent ss = new Intent(Post.this, CameraActivity.class);
        ss.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(ss);
    }

    private void validatePostInfo() {

        description = description_post.getText().toString();

       /* if(getPhoto==null){
            Toast.makeText(getApplicationContext(),"Please Select Image from Gallery....",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(getApplicationContext(),"At least say something about your post....",Toast.LENGTH_SHORT).show();
        }*/

        if (checkBox.isChecked()) {

            progressDialog.setTitle("New post is uploading.....");
            progressDialog.setMessage("Please wait....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StoringImagesPostProfileViewToFirebase();

            Log.e("if Condition", "if Called");
        } else {


            progressDialog.setTitle("New post is uploading.....");
            progressDialog.setMessage("Please wait....");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StoringImageToFirebase();
            Log.e("else Condition", "else Called");

        }

    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void SavePostInformationIntoDatabase() {
        databaseReference.child(Current_User_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserProfilePic = "";
                if (dataSnapshot.exists()) {
                    String UserFullName = "";
                    String UserName = "";
                    String mobile_Number = "";
                    String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
                    if (dataSnapshot.hasChildren()) {
                        if (dataSnapshot.child("displayName").getValue().toString() != null) {
                            UserFullName = dataSnapshot.child("displayName").getValue().toString();
                        }
                       else if (dataSnapshot.child("userName").getValue().toString() != null) {
                             UserName = dataSnapshot.child("userName").getValue().toString();
                        }
                      else   if (dataSnapshot.child("photoUrl").getValue().toString() != null) {
                            UserProfilePic = dataSnapshot.child("photoUrl").getValue().toString();
                        }
                       else  if (dataSnapshot.child("mobileNo").getValue().toString() != null) {
                            //String ProfileName = dataSnapshot.child("userName").getValue().toString();
                            mobile_Number = dataSnapshot.child("mobileNo").getValue().toString();
                        }
                        final Post_Model post_model = new Post_Model();



                        //hashMap.put(mAuth.getCurrentUser().getUid(), "Liked");
                        if (getPhoto != null) {
                            post_model.setProfilename(UserFullName);
                            post_model.setPostFullName(UserName);
                            post_model.setProfilePic(UserProfilePic);
                            post_model.setUid(Current_User_Id);
                            post_model.setPostDate(CurrentDate);
                            post_model.setPostTime(CurrentTime);
                            post_model.setPostImage(downloadUri);
                            post_model.setPostMessage(description);
                            post_model.setTimestamp(timeStamp);

                            Log.e("downloadUri", "" + downloadUri);
                            AddPhotoPath.add(downloadUri);
                            Log.e("downloadUriadadaaa", "" + AddPhotoPath.size());
                            post_model.setAl_imagepath(AddPhotoPath);
                            post_model.setMobile_number(mobile_Number);
                            post_model.setPlace(place);
                          //  likeCountUid.add("0");
                        //    post_model.setLikeCountUid(likeCountUid);


                            //post_model.setTagPeople_Path(GetListItemList);
                        } else {
                            post_model.setProfilename(UserFullName);
                            post_model.setPostFullName(UserName);
                            post_model.setProfilePic(UserProfilePic);
                            post_model.setUid(Current_User_Id);
                            post_model.setPostDate(CurrentDate);
                            post_model.setPostTime(CurrentTime);

                            post_model.setPostMessage(description);
                            post_model.setTimestamp(timeStamp);

                            post_model.setMobile_number(mobile_Number);
                            post_model.setPlace(place);
                         //   likeCountUid.add("0");
                            //post_model.setLikeCountUid(likeCountUid);

                        }

                        PostReference.child(CurrentTimeStamp).setValue(post_model).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isComplete()) {

                                    PostDiaryReference.child(CurrentTimeStamp).setValue(post_model).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isComplete()) {
                                                Toast.makeText(getApplicationContext(), "new Post Uploaded Successfully....", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                SharedPreferences preferences = getSharedPreferences("NashBud", MODE_PRIVATE);
                                                preferences.edit().remove("PostString").commit();
                                                Intent intent = new Intent(Post.this, ChatScreenOne.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });

                                } else {
                                    String messages = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(), "Failed to Upload...Pls Try again Later " + messages, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("CVHDJHSGUHDS", databaseError.toString());
            }
        });

    }

    private void StoringImageToFirebase() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = simpleDateFormat.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
        CurrentTime = simpleTimeFormat.format(calendarTime.getTime());

        Calendar timestamp = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormattime = new SimpleDateFormat("ssmmHH");
        CurrentTimeStamp = simpleTimeFormattime.format(timestamp.getTime());


        RandomSave = CurrentDate + CurrentTime;
        //Log.i("getPhoto" , getPhoto);
         if(getPhoto  != null  ){

              imageUri1 = Uri.fromFile(new File(getPhoto));

              StorageReference storageReference1 = storageReference.child(Storage_Path).child(RandomSave + imageUri1.getLastPathSegment());

              storageReference1.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                      if (task.isComplete()) {
                          downloadUri = task.getResult().getDownloadUrl().toString();

                          if (downloadUri.length() > 0) {
                              SavePostInformationIntoDatabase();
                          }

                      } else {
                          String messages = task.getException().getMessage();
                          Toast.makeText(getApplicationContext(), "Failed to Upload" + messages, Toast.LENGTH_SHORT).show();
                      }

                  }
              });

      }else{
          SavePostInformationIntoDatabase();
      }

    }

    private void SavePostImagesIntoDatabase() {
        databaseReference.child(Current_User_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UserProfilePic  = "";
                String UserName = "" ;
                if (dataSnapshot.exists()) {
                    String UserFullName = "";
                    String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
                    if (dataSnapshot.hasChild("displayName")) {
                         UserFullName = dataSnapshot.child("displayName").getValue().toString();
                    }
                    if (dataSnapshot.hasChild("userName")) {
                         UserName = dataSnapshot.child("userName").getValue().toString();
                    }
                    if(dataSnapshot.hasChild("photoUrl")) {
                        UserProfilePic = dataSnapshot.child("photoUrl").getValue().toString();
                    }

                    //String ProfileName = dataSnapshot.child("userName").getValue().toString();
                    String mobile_Number = dataSnapshot.child("mobileNo").getValue().toString();
                    final Post_Model post_model = new Post_Model();

                    /*HashMap postMap = new HashMap();

                    postMap.put("profilePic",UserProfilePic);
                    postMap.put("profilename",UserFullName);
                    postMap.put("uid",Current_User_Id);
                    postMap.put("postDate",CurrentDate);
                    postMap.put("postTime",CurrentTime);
                    //postMap.put("postMessage",description);
                    postMap.put("pics",downloadUri);
                    //postMap.put("place",Place);
                    postMap.put("mobile_number",mobile_Number);*/

                        post_model.setProfilename(UserFullName);
                        post_model.setPostFullName(UserName);
                        post_model.setProfilePic(UserProfilePic);
                        post_model.setUid(Current_User_Id);
                        post_model.setPostDate(CurrentDate);
                        post_model.setPostTime(CurrentTime);
                        post_model.setPostImage(downloadUri);
                        post_model.setMobile_number(mobile_Number);
                        post_model.setTimestamp(timeStamp);

                        AddPhotoPath.add(downloadUri);
                        post_model.setAl_imagepath(AddPhotoPath);
                        post_model.setPlace(place);
                        post_model.setTagPeople_Path(GetListItemList);
                        post_model.setPostMessage(description);
                        likeCountUid.add("0");
                       // post_model.setLikeCountUid(likeCountUid);


                       // Log.e("Post model in profile", "" + post_model);


                    PostImageReference.child(CurrentTimeStamp).setValue(post_model).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isComplete()) {

                                PostReference.child(CurrentTimeStamp).setValue(post_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isComplete()) {

                                            PostDiaryReference.child(CurrentTimeStamp).setValue(post_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isComplete()) {
                                                        AddPhotoPath.clear();
                                                        Toast.makeText(getApplicationContext(), "new Post Uploaded Successfully....", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(Post.this, ChatScreenOne.class);
                                                        startActivity(intent);
                                                      //  finish();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                            } else {
                                String messages = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Failed to Upload...Pls Try again Later " + messages, Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void StoringImagesPostProfileViewToFirebase() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = simpleDateFormat.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
        CurrentTime = simpleTimeFormat.format(calendarTime.getTime());


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String date = df.format(Calendar.getInstance().getTime());

        Calendar timestamp = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormattime = new SimpleDateFormat("ssmmHH");
        CurrentTimeStamp = simpleTimeFormattime.format(timestamp.getTime());
        RandomSave = CurrentDate + CurrentTime;

        if(getPhoto  != null){
            //imageUri1 = new Uri[getPhoto.size()];
          //  for (int i = 0; i < getPhoto.size(); i++) {
               imageUri1 = Uri.fromFile(new File(getPhoto));
               // Log.e("ImageUriForLoop", "" + imageUri1[i]);
                StorageReference storageReference1 = storageReference.child(Storage_Path).child(RandomSave + getPhoto);
                storageReference1.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isComplete()) {
                            downloadUri = task.getResult().getDownloadUrl().toString();
                            Log.e("DownLoadUri", "" + downloadUri);
                            if (downloadUri.length() > 0) {
                                SavePostImagesIntoDatabase();
                            }
                        } else {
                            String messages = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), "Failed to Upload" + messages, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
           // }
        }else {
            SavePostImagesIntoDatabase();
        }

    }

}
