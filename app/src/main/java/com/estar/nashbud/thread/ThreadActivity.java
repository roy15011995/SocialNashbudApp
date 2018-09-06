package com.estar.nashbud.thread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
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
import com.estar.nashbud.post.Post;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.upload_photo.Message;
import com.estar.nashbud.upload_photo.UploadPhoto;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.utils.Utility;
import com.estar.nashbud.widgets.EmptyStateRecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class ThreadActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    @BindView(R.id.activity_thread_messages_recycler)
    EmptyStateRecyclerView messagesRecycler;
    @BindView(R.id.activity_thread_send_fab)
    FloatingActionButton sendFab;
    @BindView(R.id.activity_thread_input_edit_text)
    TextInputEditText inputEditText;
    @BindView(R.id.activity_thread_empty_view)
    TextView emptyView;
    @BindView(R.id.activity_thread_editor_parent)
    RelativeLayout editorParent;
    @BindView(R.id.activity_thread_progress)
    ProgressBar progress;

    private DatabaseReference mDatabase, UserDatabaseReference, databaseReferenceFriends;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @State
    String userUid, DisplayName, PhotoUrl;
    public static String place, Address, checkStatus;
    @State
    boolean emptyInput;
    private LinearLayout mRevealView;
    private ImageButton gallery_btn, photo_btn, video_btn, audio_btn;
    private boolean hidden = true;
    private User user;
    private FirebaseUser owner;
    private TextView textUserName, text_status;
    private ImageView profilePic, imageBack, imageAttach, single_tick;
    Context context;
    String CurrentUserName, CurrentUserPhoto;

    private static ArrayList<String> getPhoto = new ArrayList<>();
    private static ArrayList<String> GetListItemList = new ArrayList<>();

    private Menu menu;
    Toolbar toolbar;

    Uri FilePathUri;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask, conImage, output;

    InternetUtil internetUtil;
    Boolean isConnect;
    SharedPreference sharedPreference;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences, sharedPreferences1;
    int count = 0;
    private static ThreadActivity ins;
    private static final int REQUEST = 112;
    Bitmap bm = null;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View view;
    ImageView SelectedImage;
    EditText Caption;
    FloatingActionButton fab_Caption_Image;
    StorageReference storageReference;
    String Storage_Path = "Caption_Image_Chat/";
    ProgressDialog progressDialog;
    String NameOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        context = ThreadActivity.this;

        mRevealView = findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        gallery_btn = findViewById(R.id.gallery_img_btn);
        photo_btn = findViewById(R.id.photo_img_btn);
        video_btn = findViewById(R.id.video_img_btn);
        audio_btn = findViewById(R.id.audio_img_btn);
        builder = new AlertDialog.Builder(ThreadActivity.this);
        view = getLayoutInflater().inflate(R.layout.show_photo_dialog, null);

        SelectedImage = (ImageView) view.findViewById(R.id.image_selection);
        Caption = (EditText) view.findViewById(R.id.caption);
        fab_Caption_Image = (FloatingActionButton) view.findViewById(R.id.fab_Caption_Image_send);


        builder.setView(view);
        alertDialog = builder.create();

        progressDialog = new ProgressDialog(context);
        storageReference = FirebaseStorage.getInstance().getReference();
        UserDatabaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceFriends = FirebaseDatabase.getInstance().getReference().child("Friends");

        owner = FirebaseAuth.getInstance().getCurrentUser();

        if (owner != null) {
            final String Uid = owner.getUid();

            UserDatabaseReference.child("users").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    user = dataSnapshot.getValue(User.class);
                    CurrentUserName = user.getDisplayName();
                    CurrentUserPhoto = user.getPhotoUrl();
                    Log.e("CurrentUserName :", "" + CurrentUserName + "\n" + "CurrentUserPhoto :" + CurrentUserPhoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        ins = this;

        //Log.e("GetGalleryPhotoOnCreate",""+getPhoto);


        if (savedInstanceState == null) {
            userUid = getIntent().getStringExtra(Constants.USER_ID_EXTRA);
            checkStatus = getIntent().getStringExtra("CheckStatus");
            DisplayName = getIntent().getStringExtra("DisplayName");
            PhotoUrl = getIntent().getStringExtra("PhotoUrl");
            Log.e("user id from thread", "" + userUid);
            Log.e("CheckStatus ", "" + checkStatus);
           /* Log.e("DisplayNameFromChat",""+DisplayName);
            Log.e("PhotoUrlFromChat",""+PhotoUrl);*/
        }
        sendFab.requestFocus();
        sendFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        textUserName = findViewById(R.id.text_name);
        profilePic = findViewById(R.id.image_profile);
        imageBack = findViewById(R.id.image_back);
        single_tick = (ImageView) findViewById(R.id.single_tick);

        text_status = (TextView) findViewById(R.id.text_status);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageAttach = findViewById(R.id.image_attach);
        imageAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach();
            }
        });
        loadUserDetails();
        initializeAuthListener();
        initializeInteractionListeners();
        gallery_btn.setOnClickListener(this);
        photo_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        audio_btn.setOnClickListener(this);


        fab_Caption_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GetCaption = Caption.getText().toString();

                if (GetCaption.length() >= 0 && FilePathUri != null) {

                    Log.e("GetCaption", "" + GetCaption/*+"\n"+"GetImageUri"+destination*/);
                    UploadCaptionImageForChat();
                    alertDialog.dismiss();
                }
            }
        });

        if(checkStatus!=null)
        {
            if (checkStatus.equals("I am alive")) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String CurrentUser = firebaseUser.getUid();
                databaseReferenceFriends.child(CurrentUser).child(userUid).child("check").setValue(true);
            }
        }
        // boolean onResumeCalled = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static ThreadActivity getInstace() {
        return ins;
    }

    public void UploadCaptionImageForChat() {
        final String GetCaption = Caption.getText().toString();

        if (GetCaption.length() >= 0 && FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ByteArrayOutputStream bytes=null;
            File destination=null;

            try {
                 bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                 destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;

                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }

            byte[] imageBytes = bytes.toByteArray();
            conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //output = adding + conImage;
            output = conImage;

            Log.e("GetImageUri", "" + destination);

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." +
                    GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show();

                    long timestamp = new Date().getTime();
                    long dayTimestamp = getDayTimestamp(timestamp);


                    String ownerUid = owner.getUid();
                    Log.e("Owner ID", " " + ownerUid);
                    String userUid = user.getUid();
                    String status = "read";
                    String displayName = user.getDisplayName();
                    String photoUrl = user.getPhotoUrl();

                    Log.e("Display Name", " " + displayName);
                    Log.e("UserId", " " + userUid);

                    Log.e("PhotoUrl", "" + photoUrl);

                    final String PhotoCaption = taskSnapshot.getDownloadUrl().toString();

                    Log.e("GetCurrentUserName", "" + CurrentUserName);


                    Message message =
                            new Message(timestamp, -timestamp, dayTimestamp, GetCaption, ownerUid, userUid, status, displayName, photoUrl, PhotoCaption, CurrentUserName, CurrentUserPhoto);
                    mDatabase
                            .child("notifications")
                            .child("messages")
                            .push()
                            .setValue(message);
                    mDatabase
                            .child("messages")
                            .child(userUid)
                            .child(ownerUid)
                            .push()
                            .setValue(message);
                    if (!userUid.equals(ownerUid)) {
                        mDatabase
                                .child("messages")
                                .child(ownerUid)
                                .child(userUid)
                                .push()
                                .setValue(message);
                    }
                    //inputEditText.setText("");
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

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setTitle("Image is Uploading...");
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                        }
                    });
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void attach() {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = mRevealView.getTop();
        int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

        //Below Android LOLIPOP Version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, endradius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(700);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                mRevealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();
            }
        }
        // Android LOLIPOP And ABOVE Version
        else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.
                        createCircularReveal(mRevealView, cx, cy, 0, endradius);
                mRevealView.setVisibility(View.VISIBLE);
                anim.start();
                hidden = false;
            } else {
                Animator anim = android.view.ViewAnimationUtils.
                        createCircularReveal(mRevealView, cx, cy, endradius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();
            }
        }
    }

    private void initializeInteractionListeners() {
        inputEditText.addTextChangedListener(this);
    }

    private void loadUserDetails() {
        DatabaseReference userReference = mDatabase
                .child("users")
                .child(userUid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                initializeMessagesRecycler();
                displayUserDetails();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ThreadActivity.this, R.string.error_loading_user, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
                    Intent login = new Intent(ThreadActivity.this, UploadPhoto.class);
                    startActivity(login);
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void initializeMessagesRecycler() {
        if (user == null || owner == null) {
            Log.d("@@@@", "initializeMessagesRecycler: User:" + user + " Owner:" + owner);
            return;
        }
        Query messagesQuery = mDatabase
                .child("messages")
                .child(owner.getUid())
                .child(user.getUid())
                .orderByChild("negatedTimestamp");
        MessagesAdapter adapter = new MessagesAdapter(this, owner.getUid(), messagesQuery);
        messagesRecycler.setAdapter(null);
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

    @OnClick(R.id.activity_thread_send_fab)
    public void onClick() {
        if (user == null || owner == null) {
            Log.d("@@@@", "onSendClick: User:" + user + " Owner:" + owner);
            return;
        }
       /* SharedPreferences sharedPref = getApplicationContext().(Context.MODE_PRIVATE);
        editor.putString("first_name", fname1.getText().toString());
        editor.putString("second_name", mname1.getText().toString());
        editor.commit();*/

        final String body = inputEditText.getText().toString().trim();

        if (body.length() > 0) {
            final long timestamp = new Date().getTime();
            final long dayTimestamp = getDayTimestamp(timestamp);

            final String ownerUid = owner.getUid();
            Log.e("Owner ID", " " + ownerUid);
            final String userUid = user.getUid();
            final String status = "read";
            final String displayName = user.getDisplayName();
            final String photoUrl = user.getPhotoUrl();

            Log.e("Display Name", " " + displayName);
            Log.e("UserId", " " + userUid);

            Log.e("PhotoUrl", "" + photoUrl);


            Message message =
                    new Message(timestamp, -timestamp, dayTimestamp, body, ownerUid, userUid, status, displayName, photoUrl, "", CurrentUserName, CurrentUserPhoto);
            mDatabase
                    .child("notifications")
                    .child("messages")
                    .push()
                    .setValue(message);
            mDatabase
                    .child("messages")
                    .child(userUid)
                    .child(ownerUid)
                    .push()
                    .setValue(message);
            if (!userUid.equals(ownerUid)) {
                mDatabase
                        .child("messages")
                        .child(ownerUid)
                        .child(userUid)
                        .push()
                        .setValue(message);
            }
            inputEditText.setText("");
            /*sharedPreference=new SharedPreference();
            body=sharedPreference.lastSendMessage(context);
            editor.putString("getMessage",body);*/

            sharedPreferences = getSharedPreferences("PREF_KEY_lASTSENDMESSAGE", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.e("GetMessage", "" + body);
            editor.putString("MessageText", body);
            editor.commit();

            sharedPreferences1 = getSharedPreferences("PREF_KEY_ID", context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            Log.e("ID", "" + userUid);
            editor1.putString("ID", userUid);
            editor1.commit();

            sharedPreferences1 = getSharedPreferences("PREF_KEY_TIME", context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences1.edit();
            Log.e("TIME", "" + timestamp);
            editor2.putLong("TIME", timestamp);
            editor2.commit();

            count++;
            Log.e("Count", "" + count);
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

    private void displayUserDetails() {

        toolbar.setTitle("");
        textUserName.setText(user.getDisplayName());

        textUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userUid != null) {
                    Intent i = new Intent(getApplicationContext(), FriendsProfileActivity.class);
                    i.putExtra("user_id", userUid);
                    startActivity(i);
                }


            }
        });
        String profileUrl = user.getPhotoUrl();
        try {
            if (!(profileUrl.equals("") || profileUrl.equals("null") || profileUrl.equals(null))) {
                Glide.with(context)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.profile)
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(profilePic);
            } else {
                Log.e("profile is", "null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Boolean status = user.getOnline();

        Log.e("Thread Status :", "" + status);

        if (status == null) {
            text_status.setVisibility(View.GONE);
        } else if (status == true) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    text_status.setText("Active Now");
                    text_status.setVisibility(VISIBLE);

                }
            }, 1000);

        } else if (status == false) {

            final Long time = user.getTime();


            if (time == null) {
                text_status.setText(" ");
            } else if (time != null) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Current Time ", "" + time);

                        LastSeenTime displayTime = new LastSeenTime();

                        String LastSeenTime = displayTime.getTimeAgo(time);

                        text_status.setText("Active " + LastSeenTime);

                        text_status.setVisibility(VISIBLE);
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_profile:
                Intent i = new Intent(context, FriendsProfileActivity.class);
                i.putExtra("user_id", userUid);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        hideRevealView();
        switch (v.getId()) {

            case R.id.gallery_img_btn:
                //Toast.makeText(context, "gallery", Toast.LENGTH_SHORT).show();

                break;
            case R.id.photo_img_btn:
                cameraIntent();
                //Toast.makeText(context, "Document", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_img_btn:

                galleryIntent();
                //Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();

                /*Intent intent2 = new Intent(getApplicationContext(), OpenGallery.class);
                startActivity(intent2);*/

                break;
            case R.id.audio_img_btn:
                //Toast.makeText(context, "audio", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            FilePathUri = data.getData();

            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), FilePathUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SelectedImage.setImageBitmap(bm);
        //bm = (Bitmap) data.getExtras().get("data");


        Log.e("filepath uri on capture", "" + data);
        Log.e("bm on capture", "" + bm);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

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

        //SelectedImage.setImageBitmap(bm);

        byte[] imageBytes = bytes.toByteArray();
        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //output = adding.concat(conImage);
        output = conImage;

        //Log.e("image converted", "" + output);

        Log.e("image DESTINATION", "" + destination);

        output = output.replaceAll("\\s+", "");
        //Log.e("space removed", "" + output);

        //uploadImage();
        Log.e("image capture", "" + bm);
        /*String base = output;

        Log.e("base64 getting","" + base);

        String base64String = output;
        String base64Image = base64String.split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageProfilePic.setImageBitmap(decodedByte);*/
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            FilePathUri = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), FilePathUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SelectedImage.setImageBitmap(bm);


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        final File destination = new File(Environment.getExternalStorageDirectory(),
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

        byte[] imageBytes = bytes.toByteArray();
        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //output = adding + conImage;
        output = conImage;

        //uploadImage();
        Log.e("image gallery location", "" + destination);

        Log.e("image gallery", "" + bm);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                alertDialog.show();
            } else if (requestCode == REQUEST_CAMERA) {
                SelectedImage.setImageURI(FilePathUri);
                //onCaptureImageResult(data);
                alertDialog.show();
                Log.e("file uri", "" + FilePathUri);
            }
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            super.onMessageReceived(remoteMessage);
        }
    }


    public static class MyReceiverThread extends BroadcastReceiver {
        Bundle Extra;
        String[] TagPeople;

        @Override
        public void onReceive(Context context, Intent intent) {
            Extra = intent.getExtras();
            if (Extra != null) {


                if (Extra.containsKey("GetPhoto")) {
                    getPhoto = Extra.getStringArrayList("GetPhoto");
                    Log.e("GetGalleryPhoto", "" + getPhoto);
                    //ThreadActivity.getInstace().updateGridViewPhoto(getPhoto);

                }
            }
        }
    }

}
