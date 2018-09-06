package com.estar.nashbud.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.EditPost;
import com.estar.nashbud.chatscreenpages.LikeActivity;
import com.estar.nashbud.comments.CommentsActivity;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.upload_photo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ShowMyProfilePost extends AppCompatActivity {
    Toolbar toolbar;
    Bundle extra;
    String GetPostKey, ProfileName, UserName, ProfilePic, Image,
            PostDate, PostTime, PostDescription, PostPlace, Uid,
            userId_current,CurrentDate,CurrentTime,RandomSave,getUid,getProfilePic,getProfileName;
    DatabaseReference databaseReference, databaseReferenceUser, databaseReferenceLike, databaseReferencePost,
            databaseReferenceComments,databaseReferenceDiary;
    ImageView profilePic, postImage, option_menu_dots,divider_dots,Like_Inactive;
    TextView profileName, profileUserName, postDate, postTime, postDescription, postPlace, like_Count,comments_count;
    ArrayList<String> arrayList;
    ProgressDialog progressDialog;
    long countComments = 0;
    long countLikes = 0;
    RelativeLayout Relative_Photo, Relative_Like;
    private FirebaseAuth mAuth;
    private boolean mProcesslike = false;
    FirebaseUser firebaseUser;
    LinearLayout Linear_Comments;
    boolean isFirstTime = false;
    ArrayList<String> custId = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_profile_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        profilePic = (ImageView) findViewById(R.id.post_user_pic_profile);
        postImage = (ImageView) findViewById(R.id.post_image_profile);
        profileName = (TextView) findViewById(R.id.post_user_name_profile);
        postDate = (TextView) findViewById(R.id.post_date_profile);
        postTime = (TextView) findViewById(R.id.post_time_profile);
        postDescription = (TextView) findViewById(R.id.post_description_profile);
        postPlace = (TextView) findViewById(R.id.post_place_profile);
        like_Count = (TextView) findViewById(R.id.like_Count);
        comments_count = (TextView) findViewById(R.id.comments_count);
        option_menu_dots = (ImageView) findViewById(R.id.dots_profile);
        divider_dots= (ImageView) findViewById(R.id.divider_dots);
        Relative_Like = (RelativeLayout)findViewById(R.id.relative_like_post_profile);
        Like_Inactive = (ImageView)findViewById(R.id.like_inactive_post_profile);
        Linear_Comments = (LinearLayout) findViewById(R.id.linear_comments_post_profile);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("post_images");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceLike = FirebaseDatabase.getInstance().getReference().child("likes");
        databaseReferencePost = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReferenceComments = FirebaseDatabase.getInstance().getReference().child("Comments");
        databaseReferenceDiary = FirebaseDatabase.getInstance().getReference().child("Diary");
        mAuth = FirebaseAuth.getInstance();

        custId.clear();



        extra = getIntent().getExtras();

        if (extra != null) {
            GetPostKey = extra.getString("PostKey");
            getLike(GetPostKey);
            getcomments(GetPostKey);
            Log.e("GetPostKeyShowProfile ", "" + GetPostKey);
        }

        /*if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Image");
            Log.e("GetProfileName ",""+ProfileName);
            Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(close);
            toolbar.setTitleTextColor(Color.parseColor("#000000"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        ShowMyProfilePostDetailsInformation();
        ShowOptionMenuDotsForPostEditing();
        setBtnLike(GetPostKey);
        OperationLike(GetPostKey);
        OperationComment(GetPostKey);
        LikeCountOnClickCall(GetPostKey);
        CommentsCountOnClickCall(GetPostKey);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLike(GetPostKey);
        getcomments(GetPostKey);
        LikeCountOnClickCall(GetPostKey);
        CommentsCountOnClickCall(GetPostKey);
    }

    public void ShowMyProfilePostDetailsInformation() {
        databaseReference.child(GetPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Post_Model model = dataSnapshot.getValue(Post_Model.class);
                    Image = model.getPostImage();
                    PostDate = model.getPostDate();
                    PostTime = model.getPostTime();
                    PostDescription = model.getPostMessage();
                    PostPlace = model.getPlace();
                    Uid = model.getUid();

                    SetDetailsInformationForPost(Image, PostDate, PostTime, PostDescription, PostPlace, Uid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getLike(String postKey) {
        like_Count.setVisibility(View.VISIBLE);
        databaseReferenceLike.child(postKey).child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countLikes = dataSnapshot.getChildrenCount();
                    Log.e("LikesCount", "" + countLikes);
                    if (countLikes > 0) {
                        if (countLikes == 1) {
                            like_Count.setText(countLikes + " Like");
                        } else {
                            like_Count.setText(countLikes + " Likes");
                        }
                    } else {
                        like_Count.setText("");
                    }

                        /* divider_dots.setVisibility(View.VISIBLE);
                        if(!dataSnapshot.exists())
                        {
                            divider_dots.setVisibility(View.GONE);
                        }*/
                } else {
                    like_Count.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void SetDetailsInformationForPost(
            String ImagePostxx, String PostDatexx, String PostTimexx,
            String Description, String Place, String uid) {
        Glide.with(getApplicationContext())
                .load(ImagePostxx)
                .placeholder(getResources().getDrawable(R.drawable.image_placeholder))
                .dontAnimate()
                .into(postImage);

        postDate.setText(PostDatexx);
        postTime.setText(PostTimexx);
        postDescription.setText(Description);
        postPlace.setText(Place);

        databaseReferenceUser.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    ProfileName = user.getDisplayName();
                    ProfilePic = user.getPhotoUrl();

                    profileName.setText(ProfileName);
                    Glide.with(getApplicationContext())
                            .load(ProfilePic)
                            .placeholder(R.mipmap.profile)
                            .centerCrop()
                            .dontAnimate()
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .into(profilePic);

                    if (toolbar != null) {
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setTitle("@" + ProfileName);
                        Log.e("GetProfileName ", "" + ProfileName);
                        Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
                        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
                        getSupportActionBar().setHomeAsUpIndicator(close);
                        toolbar.setTitleTextColor(Color.parseColor("#000000"));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getcomments(String postKey) {
        databaseReferenceComments.child(postKey).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countComments = dataSnapshot.getChildrenCount();
                    Log.e("CommentsCount", "" + countComments);
                    comments_count.setText(countComments + " Comments");
                   divider_dots.setVisibility(View.VISIBLE);

                    if (!dataSnapshot.exists()) {
                        divider_dots.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ShowOptionMenuDotsForPostEditing() {
        option_menu_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(GetPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Post_Model model = dataSnapshot.getValue(Post_Model.class);
                            arrayList = new ArrayList<String>();
                            Image = model.getPostImage();
                            PostDate = model.getPostDate();
                            PostTime = model.getPostTime();
                            PostDescription = model.getPostMessage();
                            PostPlace = model.getPlace();
                            Uid = model.getUid();
                            arrayList = model.getAl_imagepath();

                            final Dialog dialog = new Dialog(ShowMyProfilePost.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog);
                            TextView edit = (TextView) dialog.findViewById(R.id.edit);
                            TextView delete = (TextView) dialog.findViewById(R.id.delete);

                            edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReferenceUser.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                User user = dataSnapshot.getValue(User.class);
                                                ProfileName = user.getDisplayName();
                                                ProfilePic = user.getPhotoUrl();


                                                Intent intent = new Intent(ShowMyProfilePost.this, EditPost.class);
                                                intent.putExtra("GetName", ProfileName);
                                                intent.putExtra("GetImage", ProfilePic);
                                                intent.putExtra("GetDescription", PostDescription);
                                                intent.putExtra("GetLocation", "" + PostPlace);
                                                intent.putExtra("PostKey", GetPostKey);
                                                intent.putStringArrayListExtra("PhotoList", arrayList);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });

                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowMyProfilePost.this);

                                    builder.setTitle("delete the post");
                                    builder.setMessage("Are you sure to delete the post ?");
                                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            progressDialog = new ProgressDialog(ShowMyProfilePost.this);
                                            progressDialog.setTitle("Post Deleting");
                                            progressDialog.setMessage("Please wait..!!");
                                            progressDialog.show();

                                            databaseReference.child(GetPostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isComplete()) {
                                                        databaseReferencePost.child(GetPostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isComplete()) {
                                                                    databaseReferenceLike.child(GetPostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isComplete()) {
                                                                                databaseReferenceComments.child(GetPostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        if (task.isComplete()) {
                                                                                            databaseReferenceDiary.child(GetPostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isComplete())
                                                                                                    {
                                                                                                        dialog.dismiss();
                                                                                                        Toast.makeText(ShowMyProfilePost.this, "Post Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                                                                        progressDialog.dismiss();
                                                                                                        finish();
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
                                                            }
                                                        });
                                                    }

                                                }
                                            });
//
                                        }
                                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    Dialog dialog = builder.create();
                                    dialog.show();
                                }
                            });

                            dialog.show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void setBtnLike(final String PostKey) {

            /*Like_Inactive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Likecount = Likecount + 1;

                    if(!((Likecount % 2)  == 0)){
                        Like_Inactive.setImageResource((R.drawable.like_active));
                    }
                    else
                    {
                        Like_Inactive.setImageResource((R.drawable.like));
                    }
                }
            });*/
        databaseReferenceLike.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(PostKey).child("Like").hasChild(mAuth.getCurrentUser().getUid())) {
                    Like_Inactive.setImageResource((R.drawable.like_active));
                    getLike(PostKey);
                } else {
                    Like_Inactive.setImageResource((R.drawable.like));
                    getLike(PostKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void OperationLike(final String PostKey)
    {
        Relative_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   PostKey3 = getRef(position).getKey();
                Log.e("GetPostKeyLike", "" + PostKey);

                mProcesslike = true;

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                userId_current = firebaseUser.getUid();

                databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
                Calendar calendarDate = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                CurrentDate = simpleDateFormat.format(calendarDate.getTime());

                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
                CurrentTime = simpleTimeFormat.format(calendarTime.getTime());

                RandomSave = CurrentDate + CurrentTime;


                databaseReferenceLike.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mProcesslike) {
                            Like_Inactive.setImageDrawable(getApplicationContext().getDrawable(R.drawable.like_active));

                            databaseReferencePost.child(GetPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists())
                                    {
                                        Post_Model model = dataSnapshot.getValue(Post_Model.class);
                                        //OperationLikeEffectedInUserTable(model,GetPostKey);

                                        if (model.getLikeInput() != null) {
                                            for (int i = 0; i < model.getLikeInput().size(); i++) {
                                                if (model.getLikeInput().get(i).contains(mAuth.getCurrentUser().getUid())) {
                                                    model.getLikeInput().remove(mAuth.getCurrentUser().getUid());
                                                    databaseReferencePost.child(PostKey).child("likeInput").setValue(model.getLikeInput());
                                                    //Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like));
                                                    mProcesslike = false;
                                                }else{
                                                    custId.add(model.getLikeInput().get(i));
                                                    custId.add(mAuth.getCurrentUser().getUid());
                                                    databaseReferencePost.child(PostKey).child("likeInput").setValue(custId);
                                                    //Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like_active));
                                                }

                                            }
                                        }else {
                                            mProcesslike = false;
                                            custId.add(mAuth.getCurrentUser().getUid());
                                            databaseReferencePost.child(PostKey).child("likeInput").setValue(custId);
                                            Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like_active));
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            if (dataSnapshot.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).hasChild(mAuth.getCurrentUser().getUid())) {

                                databaseReferenceLike.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).removeValue();
                                getLike(PostKey);
                                Like_Inactive.setImageDrawable(getApplicationContext().getDrawable(R.drawable.like));
                                mProcesslike = false;
                            } else {

                                databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String ProfilePic = "";
                                        String ProfileName ="";
                                        if (dataSnapshot.exists()) {
                                            String Uid = dataSnapshot.child("uid").getValue().toString();
                                            if (mAuth.getCurrentUser().getUid().contains(Uid)) {
                                                if(dataSnapshot.hasChild("displayName")) {
                                                    ProfileName = dataSnapshot.child("displayName").getValue().toString();
                                                }
                                                if(dataSnapshot.hasChild("photoUrl")) {
                                                    ProfilePic = dataSnapshot.child("photoUrl").getValue().toString();
                                                }
                                                HashMap hashMap = new HashMap();
                                                hashMap.put("profilePic", ProfilePic);
                                                hashMap.put("profileName", ProfileName);
                                                hashMap.put("uid", mAuth.getCurrentUser().getUid());
                                                hashMap.put(mAuth.getCurrentUser().getUid(), "Liked");

                                                //Log.e("HasMapUid", "" + model.getUid());

                                                databaseReferenceLike.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).setValue(hashMap);
                                                Like_Inactive.setImageDrawable(getApplicationContext().getDrawable(R.drawable.like_active));
                                                mProcesslike = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });
    }

    private void OperationComment(final String PostKey)
    {
        Linear_Comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //       String PostKey1 = getRef(position).getKey();
                getUid = firebaseUser.getUid();
                databaseReferenceUser.child(getUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            getProfilePic = user.getPhotoUrl();
                            getProfileName = user.getDisplayName();

                            Intent intent1 = new Intent(ShowMyProfilePost.this, CommentsActivity.class);
                            intent1.putExtra("GetUid", getUid);
                            intent1.putExtra("GetName", getProfileName);
                            intent1.putExtra("GetImage", getProfilePic);
                            intent1.putExtra("GetPostKey", PostKey);
                            startActivityForResult(intent1, 1);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void LikeCountOnClickCall(final String PostKey)
    {
        like_Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(ShowMyProfilePost.this, LikeActivity.class);
                intent2.putExtra("PostKey", PostKey);
                intent2.putExtra("resultCode","FromProfile");
                startActivity(intent2);
            }
        });
    }

    private void CommentsCountOnClickCall(final String PostKey)
    {
        comments_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   String PostKey4 = getRef(position).getKey();
                Intent intent = new Intent(ShowMyProfilePost.this, CommentsActivity.class);
                intent.putExtra("GetPostKey", PostKey);
                startActivityForResult(intent, 1);
            }
        });
    }

}
