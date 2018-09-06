package com.estar.nashbud.upload_photo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.estar.nashbud.BuildConfig;
import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.chatscreenpages.UserDetails;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.utils.Urls;
import com.estar.nashbud.utils.Utility;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UploadPhoto extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_CAMERA = 100;
    CircleImageView profile_image;
    ImageView choose_image;
    Button buttonFinish;
    Integer RESULT_LOAD_IMAGE = 1;
    String m_number_from_sp, existingProfilePicture, exisitingname;
    EditText editTextName;
    RequestQueue requestQueue;
    SharedPreference sharedPreference;
    ProgressDialog dialog,progressDialog;
    Bitmap bm = null;
    Context context;
    private String userChoosenTask, conImage, output;
    private String stEditName;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String mobileFirbase, pass = "1",get_m_number,sName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String Storage_Path = "All_Image_Uploads/";
    ProgressDialog progressDoalog;
    Uri FilePathUri;
    User user;
    String getUid;
    AlertDialog b;
    AlertDialog.Builder dialogBuilder;
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
        setContentView(R.layout.activity_upload_photo);
        context = UploadPhoto.this;

        sharedPreference = new SharedPreference();
        m_number_from_sp = sharedPreference.getPhNo(context);
        Intent intent = getIntent();
        get_m_number = intent.getStringExtra("intent_mobile");
        Log.e("intent mobile in upload","" + get_m_number);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(context);
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        profile_image = findViewById(R.id.profile_image);
        choose_image = findViewById(R.id.choose_image);
        buttonFinish = findViewById(R.id.buttonFinish);
        editTextName = findViewById(R.id.editTextName);
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        buttonFinish.setOnClickListener(this);
        choose_image.setOnClickListener(this);
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //GetExistingProfileDetails();
        initializeFirebaseAuthListener();
        //loadUserDetails();
        profile_image.setOnClickListener(this);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void initializeFirebaseAuthListener() {
        progressDoalog = new ProgressDialog(UploadPhoto.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    //addUserToDatabase(firebaseUser);
                    getUid = firebaseUser.getUid();
                    loadUserDetails();
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };
    }

//    public void ShowProgressDialog() {
//        dialogBuilder = new AlertDialog.Builder(UploadPhoto.this);
//        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//        b = dialogBuilder.create();
//        b.show();
//    }
//
//    public void HideProgressDialog(){
//
//        b.dismiss();
//    }

    private void loadUserDetails() {
        Log.i("UploadPhoto guid" , getUid.toString());
        DatabaseReference userReference = databaseReference
                .child("users")
                .child(getUid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (FilePathUri == null  ) {
                    try {
                        Log.i("UploadPhoto FilePathUri", String.valueOf(FilePathUri));
                    }catch (NullPointerException ex){
                        Log.i("UploadPhoto FilePathUri", String.valueOf(ex.getLocalizedMessage()));
                    }

                    displayUserDetails();
                }
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
            String sName = user.getDisplayName();
            editTextName.setText(sName);

            String profileUrl = user.getPhotoUrl();

            Log.d("UploadPhoto" , sName + "//// >>" + profileUrl );
            if (!(profileUrl.equals("") || profileUrl.equals("null") || profileUrl.equals(null))) {
                Glide.with(context)
                        .load(profileUrl)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(profile_image);
            } else {
               Toast.makeText(getApplicationContext() , "Oops ! We can't able to fetch your Profile pic." , Toast.LENGTH_SHORT).show();
            }
            progressDoalog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("UploadPhoto ex" , e.getMessage());
        }
        progressDoalog.dismiss();
    }

    private void addUserToDatabase(FirebaseUser firebaseUser) {
        user = new User();

        user.setDisplayName("saurabh");
        user.setUid(firebaseUser.getUid());
        user.setPhotoUrl("\"https://firebasestorage.googleapis.com/v0/b/fir-uploadpic-8d43e.appspot.com/o/All_Image_Uploads%2F1512383369351.jpg?alt=media&token=84643ad2-c8fb-4130-a360-19f5fa2c86b6\"");

        databaseReference.child("users")
                .child(user.getUid()).setValue(user);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            databaseReference.child("users")
                    .child(firebaseUser.getUid())
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadPhoto.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadPhoto.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        FilePathUri = FileProvider.getUriForFile(context,
//                BuildConfig.APPLICATION_ID + ".provider",
//                getOutputMediaFile());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, FilePathUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
               //   FilePathUri = data.getData();
              //  Log.i("XYAAA" , data.getData().toString());
            }
        }

    }

    private void onCaptureImageResult(Intent data) {


        try {


            FilePathUri = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profile_image.setImageBitmap(photo);
        }catch (Exception ex){
            Log.i("XYAAA" , ex.getMessage().toString());
        }





    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            FilePathUri = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), FilePathUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile_image.setImageBitmap(bm);



    }


    @Override
    public void onClick(View v) {
        if (v == buttonFinish) {
            if (isTest()) {
                UploadImageFileToFirebaseStorage();
            }
        }
    }

    private boolean isTest() {
        stEditName = editTextName.getText().toString().trim();
        if (stEditName.length() <= 0) {
            Toast.makeText(context,"Please Enter Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImageFileToFirebaseStorage() {

        if (FilePathUri != null) {

            Log.e("file path uri","" + FilePathUri);
            Log.e("edit name getting","" + stEditName);

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." +
                    GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show();

                            user = new User();
                            user.setDisplayName(stEditName);
                            user.setMobileNo(m_number_from_sp);
                            user.setUid(getUid);
                            user.setStatus("Hay There I am Using Nashbud..");
                            user.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());

                            databaseReference.child("users")
                                    .child(user.getUid()).setValue(user);

                            String instanceId = FirebaseInstanceId.getInstance().getToken();
                            if (instanceId != null) {
                                databaseReference.child("users")
                                        .child(getUid)
                                        .child("instanceId")
                                        .setValue(instanceId);
                            }

                            Intent intent = new Intent(UploadPhoto.this, ChatScreenOne.class);
                            startActivity(intent);
                            finish();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();
                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setTitle("Image is Uploading...");
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");

                        }
                    });
        }
        else {

            //Toast.makeText(context, "Please Select Image or Add Image Name", Toast.LENGTH_SHORT).show();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String GetUid = firebaseUser.getUid();

            databaseReference.child("users").child(GetUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        user = dataSnapshot.getValue(User.class);

                        //user = new User();
                        user.setDisplayName(stEditName);
                        user.setUid(getUid);
                        user.setMobileNo(m_number_from_sp);
                        user.setPhotoUrl(user.getPhotoUrl());
                        user.setStatus("Hay There I am Using Nashbud..");

                        databaseReference.child("users")
                                .child(user.getUid()).setValue(user);

                        String instanceId = FirebaseInstanceId.getInstance().getToken();
                        if (instanceId != null) {
                            databaseReference.child("users")
                                    .child(getUid)
                                    .child("instanceId")
                                    .setValue(instanceId);
                        }
                        Intent intent = new Intent(UploadPhoto.this, ChatScreenOne.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    private void namePicUpload() {
        if (InternetUtil.isConnected(UploadPhoto.this)) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Saving...");
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(true);
            //dialog.show();
            /*UserDetails.mobileNumber = mobileFirbase;
            UserDetails.password = pass;*/
            mobileNumbertoFirebase();


            /*final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Mobile", m_number_from_sp);
                jsonObject.put("Name", editTextName.getText().toString().trim());
                jsonObject.put("Image", output);

                Log.e("FIRST PIC API sending", "" + jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.PROFILE_PIC_NAME, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                Log.e("FIRST PIC API sending", "" + jsonObject);
                                Log.e("Response", "" + response.toString());
                                String Message = response.getString("Message");
                                if (Message.equals("Profile Updated Successfully!")) {
                                    //profile_image.setImageBitmap(bm);
                                    //sharedPreference.saveProfilePic(context, output);
                                    mobileNumbertoFirebase();
                                    Toast.makeText(UploadPhoto.this, Message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UploadPhoto.this, InviteFriends.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(UploadPhoto.this, Message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof NetworkError || error instanceof NoConnectionError) {
                        Log.e("ErrorNetwork", "" + error);
                    } else if (error instanceof ServerError) {
                        Log.e("ErrorServer", "" + error);
                    } else if (error instanceof AuthFailureError) {
                        Log.e("ErrorAuthFail", "" + error);
                    } else if (error instanceof ParseError) {
                        Log.e("ErrorPrase", "" + error);
                    } else if (error instanceof TimeoutError) {
                        Log.e("ErrorTimeOut", "" + error);
                    }
                    Log.e("error 1", "" + error.toString());

                }
            });
            requestQueue.add(jsonObjectRequest);*/
        } else {
            Toast.makeText(getApplicationContext(), "Ooops!Please connect Internet Connection", Toast.LENGTH_LONG).show();
        }


    }


    private void GetExistingProfileDetails() {
        sharedPreference = new SharedPreference();
        m_number_from_sp = sharedPreference.getPhNo(context);
        Log.e("Response mobile no", "" + m_number_from_sp);

        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.GET_PROFILE_DETAILS.concat(m_number_from_sp),
                jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject details = response.getJSONObject("results");
                    Log.e("Response GET profile", "" + details);
                    exisitingname = details.getString("Name");
                    existingProfilePicture = details.getString("ProfilePicture");
                    editTextName.setText(exisitingname);

                    if (!(existingProfilePicture.equals("") || existingProfilePicture.equals("null") || existingProfilePicture.equals(null))) {
                        Log.e("profile is", "" + existingProfilePicture);
                        Picasso.with(context).invalidate(existingProfilePicture);
                        Picasso.with(context).load(existingProfilePicture).networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .placeholder(R.mipmap.profile)
                                .error(R.mipmap.profile)
                                .into(profile_image);
                        sharedPreference.saveProfilePic(context, existingProfilePicture);
                    } else {
                        Log.e("profile is", "null");
                    }


                    sharedPreference.saveUserName(context, exisitingname);


//                   //String[] json_gender = new String[];
//                    json_gender.add(details.getString("Gender"));
//                    spinnerGender.setAdapter(new ArrayAdapter<String>(this, android.R.provider_paths.simple_spinner_dropdown_item, json_gender));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Log.e("ErrorNetwork", "" + error);
                } else if (error instanceof ServerError) {
                    Log.e("ErrorServer", "" + error);
                } else if (error instanceof AuthFailureError) {
                    Log.e("ErrorAuthFail", "" + error);
                } else if (error instanceof ParseError) {
                    Log.e("ErrorParse", "" + error);
                } else if (error instanceof TimeoutError) {
                    Log.e("ErrorTimeOut", "" + error);
                }
                Log.e("error 1", "" + error.toString());

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void mobileNumbertoFirebase() {

        mobileFirbase = m_number_from_sp;
        //pass = "1";
        String url = "https://nashbud-112f5.firebaseio.com/mobile_number.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://nashbud-112f5.firebaseio.com/mobile_number");
                //Firebase reference2 = new Firebase("https://nashbud-6b93d.firebaseio.com/profile_pic");

                if (s.equals("null")) {
                    //pd.dismiss();
                    reference.child(mobileFirbase).child("password").setValue(pass);
                    //reference2.child(mobileFirbase).child("profile").setValue(output);
                    UserDetails.mobileNumber = mobileFirbase;
                    UserDetails.password = pass;
                    Log.e("firebase register", "success");

                } else {
//                    pd.dismiss();
                    Log.e("user", "exist");

                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(mobileFirbase)) {
                            reference.child(mobileFirbase).child("password").setValue(pass);
                            Log.e("register", "success 2");
                            UserDetails.mobileNumber = mobileFirbase;
                            UserDetails.password = pass;
                            /*Toast.makeText(Otp.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Otp.this, UploadPhoto.class);
                            startActivity(intent);
                            finish();*/

                        } else {
                            Log.e("user", "exist");
                            Toast.makeText(context, "username already exists", Toast.LENGTH_LONG).show();
                            ExistingMobileNumbertoFirebase();
                            UserDetails.mobileNumber = mobileFirbase;
                            UserDetails.password = pass;
                           /* Toast.makeText(Otp.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Otp.this, UploadPhoto.class);
                            startActivity(intent);
                            finish();*/
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                //pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(UploadPhoto.this);
        rQueue.add(request);
    }

    private void ExistingMobileNumbertoFirebase() {
        pass = "1";
        mobileFirbase = get_m_number;
        String url = "https://nashbud-112f5.firebaseio.com/mobile_number.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.has(mobileFirbase)) {
                        Toast.makeText(context, "user not found", Toast.LENGTH_SHORT).show();
                    } else if (obj.getJSONObject(mobileFirbase).getString("password").equals(pass)) {
                        UserDetails.mobileNumber = mobileFirbase;
                        UserDetails.password = pass;
                        //startActivity(new Intent(, Users.class));
                    } else {
                        Toast.makeText(UploadPhoto.this, "incorrect password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(UploadPhoto.this);
        rQueue.add(request);
    }
}
