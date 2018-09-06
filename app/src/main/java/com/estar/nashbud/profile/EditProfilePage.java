package com.estar.nashbud.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.estar.nashbud.BuildConfig;
import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.R;
import com.estar.nashbud.upload_photo.InviteFriends;
import com.estar.nashbud.upload_photo.UploadPhoto;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.utils.Urls;
import com.estar.nashbud.utils.Utility;
import com.firebase.client.utilities.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.validation.Validator;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class EditProfilePage extends AppCompatActivity implements View.OnClickListener {

    ImageView imageBack;
    CircleImageView update_profile_image, update_choose_image;
    Button doneVisible;
    Switch switchFollowers;
    EditText update_editTextName, editTextBio, editTextWebsite,
            editTextUserName, editTextLocality, editTextEmailAddress,
            editTextStatus;
    TextView editTextMobileNumber;
    Spinner spinnerGender;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    String m_number_from_sp, name_from_sp;
    String name, Mobile, Email, Gender, Bio, ProfilePicture = "", UserName, Locality, Website;
    SharedPreference sharedPreference;
    Context context;
    Bitmap bm = null;
    private String userChoosenTask, conImage, output, stUpdateEditName;
    private String stName,stUserName,stBio,stWebsite,stEmail,stGender,stMobileNo,stStatus;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String Storage_Path = "All_Image_Uploads/";

    Uri FilePathUri;
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
        setContentView(R.layout.activity_edit_profile_page);

        context = EditProfilePage.this;
        sharedPreference = new SharedPreference();
        requestQueue = Volley.newRequestQueue(this);
        imageBack = findViewById(R.id.imageBack);
        doneVisible = findViewById(R.id.doneVisible);
        update_profile_image = findViewById(R.id.update_profile_image);
        update_choose_image = findViewById(R.id.update_choose_image);
        update_editTextName = findViewById(R.id.update_editTextName);
        editTextBio = findViewById(R.id.editTextBio);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextMobileNumber = findViewById(R.id.update_editTextMobileNumber);
        editTextStatus = findViewById(R.id.editTextStatus);
        spinnerGender = findViewById(R.id.spinnerGender);
        doneVisible.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        update_choose_image.setOnClickListener(this);
        update_profile_image.setOnClickListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        categories.add("Not to specify");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(dataAdapter);

        /*final String[] gender = {"Male", "Female"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        spinnerGender.setAdapter(arrayAdapter);*/

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stGender = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Log.e("Selected gender: " ,"" + stGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        initializeFirebaseAuthListener();
        //GetProfileDetails();

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
                    //user = new User();
                    Log.e("@@@@", "home:signed_in:" + firebaseUser.getUid());
                    Log.e("@@@@", "getUid & name" + getUid );

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
        try {
            String sName = user.getDisplayName();
            update_editTextName.setText(sName);
            editTextUserName.setText(user.getUserName());
            editTextBio.setText(user.getBio());
            editTextWebsite.setText(user.getWebsite());
            editTextEmailAddress.setText(user.getEmail());
            editTextMobileNumber.setText(user.getMobileNo());
            editTextStatus.setText(user.getStatus());
            Log.e("pic & name","" + user.getPhotoUrl() + " " + update_editTextName);
            String profileUrl = user.getPhotoUrl();
            if (!(profileUrl.equals("") || profileUrl.equals("null") || profileUrl.equals(null))) {
                Glide.with(context)
                        .load(profileUrl)
                        .placeholder(R.mipmap.profile)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(update_profile_image);
            } else {
                Log.e("profile is", "null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

    //*select image*//
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilePage.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfilePage.this);

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
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FilePathUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FilePathUri);
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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                update_profile_image.setImageURI(FilePathUri);
            Log.e("file_uri", "" + FilePathUri);
                //onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //update_profile_image.setImageBitmap(bm);

        byte[] imageBytes = bytes.toByteArray();
        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //output = adding.concat(conImage);
        output = conImage;

        Log.e("image converted", "" + output);

        output = output.replaceAll("\\s+", "");
        Log.e("space removed", "" + output);
        //UpdateProfilePic();

        Log.e("image capture", "" + bm);
        /*String base = output;

        Log.e("base64 getting","" + base);

        String base64String = output;
        String base64Image = base64String.split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageProfilePic.setImageBitmap(decodedByte);*/
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

        update_profile_image.setImageBitmap(bm);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] imageBytes = bytes.toByteArray();
        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //output = adding + conImage;
        output = conImage;
        output = output.replaceAll("\\s+", "");
        //UpdateProfilePic();
        Log.e("Image gallery", "" + bm);
    }

    private boolean isTest() {
        stName = update_editTextName.getText().toString().trim();
        if (stName.length() <= 0) {
            Toast.makeText(EditProfilePage.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        stUserName = editTextUserName.getText().toString().trim();
        /*if (stUserName.length() <= 0){
            Toast.makeText(EditProfilePage.this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        stBio = editTextBio.getText().toString().trim();
       /* if (stBio.length() <= 0){
            Toast.makeText(EditProfilePage.this, "Please Enter Your Bio", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        stWebsite = editTextWebsite.getText().toString().trim();
        stEmail = editTextEmailAddress.getText().toString().trim();
        stStatus = editTextStatus.getText().toString().trim();

        /*if (stEmail.length() <= 0){
            Toast.makeText(EditProfilePage.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        stMobileNo = editTextMobileNumber.getText().toString().trim();
        if (stMobileNo.length() <= 0){
            Toast.makeText(EditProfilePage.this, "Please Enter Mobile No", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == doneVisible) {
            if (isTest()) {
                saveEditProfile();
            }
        }
        else if (v == update_choose_image) {
            selectImage();
        }
        else if (v == imageBack) {

            finish();
        }
        else if(v == update_profile_image ){
            selectImage();
        }

    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void UpdateProfilePic() {
        if (InternetUtil.isConnected(EditProfilePage.this)) {

            dialog = new ProgressDialog(context);
            dialog.setTitle("Pic Uploading...");
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(true);
            dialog.show();
            sharedPreference = new SharedPreference();
            m_number_from_sp = sharedPreference.getPhNo(context);
            name_from_sp = sharedPreference.getUserName(context);
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Mobile", m_number_from_sp);
                jsonObject.put("Name", name_from_sp);
                jsonObject.put("Image", output);

                Log.e("U P PIC Sending", "" + jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.PROFILE_PIC_NAME, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                Log.e("Update P Pic Response", "" + response.toString());
                                String Message = response.getString("Message");
                                if (Message.equals("Profile Updated Successfully!")) {
                                    update_profile_image.setImageBitmap(bm);
                                    Toast.makeText(EditProfilePage.this, "Pic Updated", Toast.LENGTH_SHORT).show();
                                    GetProfileDetails();
                                    sharedPreference.saveProfilePic(context, output);
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(EditProfilePage.this, "Pic not Updated", Toast.LENGTH_SHORT).show();
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
            requestQueue.add(jsonObjectRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Ooops!Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
    @SuppressLint("LongLogTag")
    private void GetProfileDetails() {
        if (InternetUtil.isConnected(EditProfilePage.this)) {
            m_number_from_sp = sharedPreference.getPhNo(context);
            Log.e("GetMobileNumberSharepreference ",m_number_from_sp);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.GET_PROFILE_DETAILS.concat(m_number_from_sp),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject details = response.getJSONObject("results");
                                Log.e("GET_P_Response", "" + details);
                                name = details.getString("Name");
                                Email = details.getString("Email");
                                Mobile = details.getString("Mobile");
                                Gender = details.getString("Gender");
                                Bio = details.getString("Bio");
                                ProfilePicture = details.getString("ProfilePicture");
                                UserName = details.getString("UserName");
                                Locality = details.getString("Locality");
                                Website = details.getString("Website");
                                update_editTextName.setText(name);
                                editTextEmailAddress.setText(Email);
                                editTextMobileNumber.setText(Mobile);
                                editTextBio.setText(Bio);
                                editTextUserName.setText(UserName);
                                editTextLocality.setText(Locality);
                                editTextWebsite.setText(Website);
                                Log.e("Pic getting", ProfilePicture);
                                // my_object = Gender;
                                if (Gender.equals("Male")) {
                                    spinnerGender.setSelection(0);
                                } else {
                                    spinnerGender.setSelection(1);
                                }
                                Log.e("GENDER get", Gender);
                                if (!(ProfilePicture.equals("") || ProfilePicture.equals("null"))) {
                                    Log.e("Pic in glide print", ProfilePicture);
                                    Picasso.with(context).invalidate(ProfilePicture);
                                    Picasso.with(context).load(ProfilePicture).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .placeholder(R.mipmap.profile)
                                            .error(R.mipmap.profile).resize(110, 110).centerCrop()
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(update_profile_image);
                                } else {
                                    Log.e("Pic not found ", "");
                                }
                                sharedPreference.saveProfilePic(context, ProfilePicture);
                                sharedPreference.saveUserName(context, name);
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

        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void saveEditProfile() {
        if (InternetUtil.isConnected(EditProfilePage.this)) {
            if (FilePathUri != null) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Updating your Profile...");
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(true);
            dialog.show();
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." +
                    GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Profile Updated Successfully.. ", Toast.LENGTH_SHORT).show();

                            user = new User();
                            user.setUserName(stUserName);
                            user.setDisplayName(stName);
                            user.setBio(stBio);
                            user.setWebsite(stWebsite);
                            user.setEmail(stEmail);
                            user.setMobileNo(stMobileNo);
                            user.setGender(stGender);
                            user.setUid(getUid);
                            user.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());

                            if(editTextStatus.getText().length()==0)
                            {
                                user.setStatus("Hay There I am Using Nashbud..");
                            }
                            else
                            {
                                user.setStatus(stStatus);
                            }

                            databaseReference.child("users")
                                    .child(user.getUid()).setValue(user);

                            String instanceId = FirebaseInstanceId.getInstance().getToken();
                            if (instanceId != null) {
                                databaseReference.child("users")
                                        .child(getUid)
                                        .child("instanceId")
                                        .setValue(instanceId);
                            }

                            finish();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            dialog.dismiss();
                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            dialog.setTitle("Image is Uploading...");
                            dialog.setMessage("Uploaded "+(int)progress+"%");

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
                            dialog = new ProgressDialog(context);
                            dialog.setTitle("Updating your Profile...");
                            dialog.setMessage("Please Wait...");
                            dialog.setCancelable(true);
                            dialog.show();

                            user = dataSnapshot.getValue(User.class);

                            //user = new User();
                            user.setUserName(stUserName);
                            user.setDisplayName(stName);
                            user.setBio(stBio);
                            user.setWebsite(stWebsite);
                            user.setEmail(stEmail);
                            user.setMobileNo(stMobileNo);
                            user.setGender(stGender);
                            user.setUid(getUid);
                            user.setPhotoUrl(user.getPhotoUrl());

                            if(editTextStatus.getText().length()==0)
                            {
                                user.setStatus("Hay There I am Using Nashbud..");
                            }
                            else
                            {
                                user.setStatus(stStatus);
                            }

                            databaseReference.child("users")
                                    .child(user.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(EditProfilePage.this,"Profile Updated Successfully..",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            String instanceId = FirebaseInstanceId.getInstance().getToken();
                            if (instanceId != null) {
                                databaseReference.child("users")
                                        .child(getUid)
                                        .child("instanceId")
                                        .setValue(instanceId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete())
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(EditProfilePage.this,"Profile Updated Successfully..",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        } else {
            Toast.makeText(getApplicationContext(), "Ooops!Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

}
