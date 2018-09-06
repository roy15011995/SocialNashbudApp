package com.estar.nashbud.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.GridViewScrollable;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by User on 11-12-2017.
 */

public class FriendsProfileActivity extends AppCompatActivity {
    CircleImageView my_profile_image;
    SharedPreference sharedPreference;
    String my_pic, my_name;
    TextView my_TextViewName,toolbar_name,textWebsite,textBio;
    Context context;
    FloatingActionButton fbMsg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference,mDatabase,databaseReferenceAddFriend
            ,databaseReferenceFriends,databaseReferenceNotification,databaseReferenceUser,databaseReferencefriendRequest;
    SharedPreferences sharedPreferences,sharedPreferences1;
    DatabaseReference mDatabaseFriendsProfile;
    User user;
    String getUid,userId;
    ImageView imageBack;
    private Menu menu;
    Toolbar toolbar;
    GridViewScrollable gridView;
    Button AddFriend;
    private String CURRENT_STATE,UserId;
    String sender_user_id,CurrentDate;
    ArrayList<Post_Model> list = new ArrayList<>();
    String[] mStringArray;
    Bundle extras;
    String displayName,instanceId,message,mobileNo,online,photoUrl,status,time,timeStamp,uid,userName;

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
        setContentView(R.layout.activity_friends_profile_page);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        setSupportActionBar(toolbar);
        context = FriendsProfileActivity.this;
        my_profile_image = findViewById(R.id.my_profile_image);
        my_TextViewName = findViewById(R.id.my_TextViewName);
        textBio = findViewById(R.id.textView_profile_status);
        textWebsite = findViewById(R.id.textView_website);
        toolbar_name = findViewById(R.id.text_name);
        fbMsg = findViewById(R.id.activity_thread_send_fab);
        gridView = (GridViewScrollable) findViewById(R.id.myFriendsProfile_recyclerView);
        gridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
        mDatabaseFriendsProfile = FirebaseDatabase.getInstance().getReference().child("post_images");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferencefriendRequest = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        /*sharedPreferences1=context.getSharedPreferences("PREF_KEY_FRIENDSUSERID", Context.MODE_PRIVATE);
        UserId = sharedPreferences1.getString("friends_user_id","");
        Log.e("FriendsUserIdSnapshot ","\n"+UserId);*/

        extras=getIntent().getExtras();
        if(extras!=null)
        {
            UserId = extras.getString("user_id");
            Log.e("FriendsUserIdSnapshot ","\n"+UserId);
        }

        my_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferenceUser.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            String GetPhotoPath = user.getPhotoUrl();
                            String UserName =  user.getUserName();
                            Intent intent = new Intent(FriendsProfileActivity.this,MyProfileImageView.class);
                            intent.putExtra("ProfilePhoto",GetPhotoPath);
                            intent.putExtra("ProfileName",UserName);
                            intent.putExtra("Profile","FromFriendsProfile");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        fbMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                Intent intent = new Intent(FriendsProfileActivity.this, ThreadActivity.class);
                intent.putExtra(Constants.USER_ID_EXTRA,userId);
                startActivity(intent);
            }
        });
        imageBack = findViewById(R.id.image_back);
        AddFriend = (Button)findViewById(R.id.button_add);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            userId = getIntent().getStringExtra("user_id");
            Log.e("user id firends profile","" + userId);

        }

        sharedPreferences=getSharedPreferences("PREF_KEY_FRIENDSUSERID",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("friends_user_id",userId);
        editor.commit();
        //editor.clear();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("post_images");
        loadUserDetails();
        initializeFirebaseAuthListener();
        ShowGridViewImage();
        //initializeUsersRecycler();

        mAuth = FirebaseAuth.getInstance();
        sender_user_id = mAuth.getCurrentUser().getUid();
        Log.e("CurrentUserId",""+sender_user_id);
        databaseReferenceAddFriend = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        databaseReferenceFriends = FirebaseDatabase.getInstance().getReference().child("Friends");
        databaseReferenceNotification = FirebaseDatabase.getInstance().getReference().child("Notifications");
        databaseReferenceFriends.keepSynced(true);
        databaseReferenceAddFriend.keepSynced(true);
        databaseReferenceNotification.keepSynced(true);

        CURRENT_STATE = "not_friends";

        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend.setEnabled(false);

                Log.e("CurrentState ",""+CURRENT_STATE);

                if (CURRENT_STATE.equals("not_friends")){
                    SendFriendRequests();
                }
                else if(CURRENT_STATE.equals("request_sent"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendsProfileActivity.this);
                    builder.setTitle("Cancel Request");
                    builder.setMessage("Do you want to cancel this request?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddFriend.setText("Add");
                            AddFriend.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_curved_corner_lite));
                            AddFriend.setCompoundDrawables(null,null,null,null);
                            CancelFriendRequests();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(CURRENT_STATE.equals("request_received"))
                {

                    AcceptFriendRequest();
                }
                else if(CURRENT_STATE.equals("friends"))
                {
                    UnFriendaFriend();
                }
            }
        });

        databaseReferenceAddFriend.child(sender_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){


                    if(dataSnapshot.hasChild(userId))
                    {
                        String request_type = dataSnapshot.child(userId).child("request_type").getValue().toString();

                        if(request_type.equals("sent"))
                        {
                            CURRENT_STATE = "request_sent";
                            AddFriend.setText("Pending");
                            AddFriend.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_curved_corner));
                            Drawable img = getApplicationContext().getResources().getDrawable( R.mipmap.wating );
                            img.setBounds( 0, 0, 40, 40 );
                            AddFriend.setCompoundDrawables(img,null,null,null);

                            if (AddFriend.getText().equals("Pending"))
                            {
                                fbMsg.setVisibility(View.GONE);
                                AddFriend.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_curved_corner));
                            }
                        }
                        else if(request_type.equals("receive")){
                            CURRENT_STATE = "request_received";
                            AddFriend.setText("Respond");
                            if (AddFriend.getText().equals("Respond")){
                                fbMsg.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                else
                {
                    databaseReferenceFriends.child(sender_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(userId)){
                                CURRENT_STATE = "friends";
                                AddFriend.setText("Remove");
                                if (AddFriend.getText().equals("Remove")){
                                    fbMsg.setVisibility(View.VISIBLE);
                                }
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

    public void ShowGridViewImage()
    {
        mDatabaseFriendsProfile.orderByChild("uid").equalTo(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        Post_Model model = ds.getValue(Post_Model.class);
                        Log.e("GetPhotoUrl","\n"+model.getPostImage());

                        /*if (model.getAl_imagepath() != null) {
                            mStringArray = new String[model.getAl_imagepath().size()];
                            mStringArray = model.getAl_imagepath().toArray(mStringArray);


                            Log.e("mStringArray", "" + mStringArray.length);

                            try {

                                for (int i1 = 0; i1 < mStringArray.length; i1++) {

                                    //list_tag.add(i1,new Tag_Model(GetListItemList));

                                    Log.d("StringArrayIs", "" + mStringArray[i1]);
                                    Post_Model post_model = new Post_Model();
                                    post_model.setImagePath(mStringArray[i1]);
                                    list.add(post_model);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

                            //myFirendsAdapter myAdapter = new myFirendsAdapter(getApplicationContext(), list);
                        final GridViewAdapter gridViewAdapter=new GridViewAdapter(FriendsProfileActivity.this,Post_Model.class,
                                R.layout.my_profile_row,mDatabaseFriendsProfile.orderByChild("uid").equalTo(userId));
                        gridView.setAdapter(gridViewAdapter);
                        gridView.setNumColumns(3);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                                String GetKey = gridViewAdapter.getRef(position).getKey();
                                Log.e("GetKeyMyProfile ",""+GetKey);
                                Intent intent = new Intent(FriendsProfileActivity.this,ShowMyProfilePost.class);
                                intent.putExtra("PostKey",GetKey);
                                startActivity(intent);
                            }
                        });
                       // }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class GridViewAdapter extends FirebaseListAdapter<Post_Model> {

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

    private void UnFriendaFriend() {
        databaseReferenceFriends.child(sender_user_id).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    databaseReferenceFriends.child(userId).child(sender_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            AddFriend.setEnabled(true);
                            CURRENT_STATE = "not_friends";
                            AddFriend.setText("Add");
                            if (AddFriend.getText().equals("Add")){
                                fbMsg.setVisibility(View.GONE);
                            }

                        }
                    });
                }
            }
        });
    }

    private void AcceptFriendRequest() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = simpleDateFormat.format(calendarDate.getTime());
        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    displayName = dataSnapshot.child("displayName").getValue().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendsProfileActivity.this);
                    builder.setTitle("Respond to this request");
                    builder.setMessage(displayName+" sent you a request");
                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    displayName = dataSnapshot.child("displayName").getValue().toString();
                                    instanceId = dataSnapshot.child("instanceId").getValue().toString();
                                    message = dataSnapshot.child("message").getValue().toString();
                                    mobileNo = dataSnapshot.child("mobileNo").getValue().toString();
                                    online = dataSnapshot.child("online").getValue().toString();
                                    status = dataSnapshot.child("status").getValue().toString();
                                    photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                                    time = dataSnapshot.child("time").getValue().toString();
                                    timeStamp = dataSnapshot.child("timeStamp").getValue().toString();
                                    uid = dataSnapshot.child("uid").getValue().toString();
                                    userName = dataSnapshot.child("userName").getValue().toString();

                                    final User user = new User();
                                    user.setDisplayName(displayName);
                                    user.setInstanceId(instanceId);
                                    user.setMessage(message);
                                    user.setMobileNo(mobileNo);
                                    user.setOnline(Boolean.parseBoolean(online));
                                    user.setPhotoUrl(photoUrl);
                                    user.setTime(Long.parseLong(time));
                                    user.setTimeStamp(Long.parseLong(timeStamp));
                                    user.setUid(uid);
                                    user.setCurrent_time(CurrentDate);
                                    user.setCheck(false);
                                    user.setStatus(status);
                                    user.setUserName(userName);

                                    databaseReferenceFriends.child(sender_user_id).child(userId)
                                            .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            databaseReferenceUser.child(sender_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    displayName = dataSnapshot.child("displayName").getValue().toString();
                                                    instanceId = dataSnapshot.child("instanceId").getValue().toString();
                                                    message = dataSnapshot.child("message").getValue().toString();
                                                    mobileNo = dataSnapshot.child("mobileNo").getValue().toString();
                                                    online = dataSnapshot.child("online").getValue().toString();
                                                    status = dataSnapshot.child("status").getValue().toString();
                                                    photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                                                    time = dataSnapshot.child("time").getValue().toString();
                                                    timeStamp = dataSnapshot.child("timeStamp").getValue().toString();
                                                    uid = dataSnapshot.child("uid").getValue().toString();
                                                    userName = dataSnapshot.child("userName").getValue().toString();

                                                    User user = new User();
                                                    user.setDisplayName(displayName);
                                                    user.setInstanceId(instanceId);
                                                    user.setMessage(message);
                                                    user.setMobileNo(mobileNo);
                                                    user.setOnline(Boolean.parseBoolean(online));
                                                    user.setPhotoUrl(photoUrl);
                                                    user.setTime(Long.parseLong(time));
                                                    user.setTimeStamp(Long.parseLong(timeStamp));
                                                    user.setUid(uid);
                                                    user.setCurrent_time(CurrentDate);
                                                    user.setCheck(false);
                                                    user.setStatus(status);
                                                    user.setUserName(userName);

                                                    databaseReferenceFriends.child(userId).child(sender_user_id)
                                                            .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            databaseReferenceAddFriend.child(sender_user_id).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful())
                                                                    {
                                                                        databaseReferenceAddFriend.child(userId).child(sender_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    AddFriend.setEnabled(true);
                                                                                    CURRENT_STATE = "friends";
                                                                                    AddFriend.setText("remove");
                                                                                    if (AddFriend.getText().equals("remove")){
                                                                                        fbMsg.setVisibility(View.VISIBLE);
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void CancelFriendRequests() {
        databaseReferenceAddFriend.child(sender_user_id).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    databaseReferenceAddFriend.child(userId).child(sender_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                AddFriend.setEnabled(true);
                                CURRENT_STATE = "not_friends";

                            }
                        }
                    });
                }

            }
        });
    }

    private void SendFriendRequests()
    {
        databaseReferenceAddFriend.child(sender_user_id).child(userId)
                .child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    databaseReferenceAddFriend.child(userId).child(sender_user_id)
                            .child("request_type").setValue("receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("from",sender_user_id);
                                hashMap.put("type","Request");
                                databaseReferenceNotification.child(userId).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint("ResourceAsColor")
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                      if(task.isComplete())
                                      {
                                          AddFriend.setEnabled(true);
                                          CURRENT_STATE = "request_sent";
                                          AddFriend.setText("Pending");
                                          Drawable img = getApplicationContext().getResources().getDrawable( R.mipmap.wating );
                                          img.setBounds( 0, 0, 40, 40 );
                                          AddFriend.setCompoundDrawables(img,null,null,null);
                                      }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

   /* private void initializeUsersRecycler() {
        myFirendsAdapter myFriendsAdapter=new myFirendsAdapter(getApplicationContext(),mDatabase);

        Log.e("mDatabase","" + mDatabase);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),numberOfColumns));
        recyclerView.setAdapter(myFriendsAdapter);
    }*/

    public void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    //addUserToDatabase(firebaseUser);
                    getUid = firebaseUser.getUid();
                    //loadUserDetails();
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
                .child(userId);

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
        toolbar_name.setText(user.getUserName());
        my_TextViewName.setText(user.getDisplayName());
        textWebsite.setText(user.getWebsite());
        textBio.setText(user.getBio());
        Log.e("pic & name","" + user.getPhotoUrl() + " " + my_TextViewName);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
