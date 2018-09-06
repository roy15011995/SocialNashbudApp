package com.estar.nashbud.chatscreenpages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.CameraActivity;
import com.estar.nashbud.comments.Comments;
import com.estar.nashbud.comments.CommentsActivity;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import clojure.lang.IFn;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Activity_fragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.activity_list)
    ListView postList;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    @BindView(R.id.whats_going_on)
    RelativeLayout postField;
    long countComments = 0;
    ArrayList<String> custId = new ArrayList<String>();
    private String mTagPeople;
    HashMap<String, String> likeHash = new HashMap<String, String>();
    private static final int REQUEST = 112;
    RelativeLayout relative_Photo, relative_Like;
    private ViewPager vp_slider;
    ImageView profilePic, PostPic, option_menu_dots, Like_Inactive, divider_dots, post_image;
    ImageButton Like_Active;
    LinearLayout linearLayout_location_row, Linear_Comments, Linear_tagPeople;
    TextView ProfileName, Description, post_data, post_time, UserNameLocation, Place, LikeCount, CommentsCount, tag_people;
    public Unbinder unbinder;
    private FirebaseAuth mAuth;
    String getUid,userId_current;
    boolean isFirstTime = false;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference, mDatabase, databaseReference1,
            mDatabaseComments, databaseReferenceLikes,
            databaseReferenceUser,databaseReferencepostImages,databaseReferenceDiary,databaseReferenceFriends , dbReport;
    PostListAdapter adapter;
    FirebaseUser firebaseUser, firebaseUser1;
    String displayName,instanceId,message,mobileNo,online,photoUrl,status,time,timeStamp,uid,userName,GetPostKey;
    String CurrentTime, CurrentDate,RandomSave;
    LinearLayout linear_emptyView,linear_name;
    private boolean mProcesslike = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.moments_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceLikes = FirebaseDatabase.getInstance().getReference().child("likes");
        databaseReferenceLikes.keepSynced(true);
        dbReport = FirebaseDatabase.getInstance().getReference().child("Report");
        dbReport.keepSynced(true);
        mDatabaseComments = FirebaseDatabase.getInstance().getReference().child("Comments");
        mDatabaseComments.keepSynced(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mDatabase.keepSynced(true);
        databaseReferencepostImages = FirebaseDatabase.getInstance().getReference().child("post_images");
        databaseReferenceDiary = FirebaseDatabase.getInstance().getReference().child("Diary");
        databaseReferenceFriends = FirebaseDatabase.getInstance().getReference().child("Friends");
        custId.clear();
        photoImage.setOnClickListener(this);
        postField.setOnClickListener(this);
        linear_emptyView = (LinearLayout)view.findViewById(R.id.linear_emptyView);

        initializeFirebaseAuthListener();
        initializeUsersRecycler();



        return view;
    }

    private void initializeUsersRecycler() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Post_Model postModel = null;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        postModel = postSnapshot.getValue(Post_Model.class);
                        if (postModel.getUid() != null) {
                            adapter = new PostListAdapter(getActivity(), Post_Model.class, R.layout.activity_row_layout, mDatabase.orderByChild("timestamp"));
                            postList.setAdapter(adapter);
                            postList.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            linear_emptyView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }
                    }
                }
                else
                {
                    postList.setVisibility(View.GONE);
                    linear_emptyView.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
       // postList.smoothScrollToPosition(adapter.getCount());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoImage:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    };
                    if (!hasPermissions(getActivity(), PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) getActivity(), PERMISSIONS, REQUEST);
                    } else {
                        callNextActivity();
                    }
                } else {
                    callNextActivity();
                }

                break;
            case R.id.whats_going_on:
                Intent myIntent = new Intent(getActivity(), Post.class);
                myIntent.putExtra("SendCode", "100");
                getActivity().startActivity(myIntent);
                break;
            default:
                break;

        }

    }

    public class PostListAdapter extends FirebaseListAdapter<Post_Model> {

        public PostListAdapter(Activity activity, Class<Post_Model> modelClass, int modelLayout, Query ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        public Post_Model getItem(int position) {
            return super.getItem(getCount() - 1 - position);
        }


        @Override
        protected void populateView(View v, final Post_Model model, final int pos) {

            profilePic = (ImageView) v.findViewById(R.id.post_user_pic);
            //PostPic =(ImageView)v.findViewById(R.id.post_pic);
            ProfileName = (TextView) v.findViewById(R.id.post_user_name);
            Description = (TextView) v.findViewById(R.id.post_description);
            post_data = (TextView) v.findViewById(R.id.post_date);
            post_time = (TextView) v.findViewById(R.id.post_time);
            UserNameLocation = (TextView) v.findViewById(R.id.name_location);
            Place = (TextView) v.findViewById(R.id.post_place);
            Like_Inactive = (ImageView) v.findViewById(R.id.like_inactive);
            linearLayout_location_row = (LinearLayout) v.findViewById(R.id.linear_location_row);
            option_menu_dots = (ImageView) v.findViewById(R.id.dots);
            vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
            relative_Photo = (RelativeLayout) v.findViewById(R.id.relative_photo);
            LikeCount = (TextView) v.findViewById(R.id.like_Count);
            relative_Like = (RelativeLayout) v.findViewById(R.id.relative_like);
            Linear_Comments = (LinearLayout) v.findViewById(R.id.linear_comments);
            CommentsCount = (TextView) v.findViewById(R.id.comments_count);
            tag_people = (TextView) v.findViewById(R.id.tagged_with);
            Linear_tagPeople = (LinearLayout) v.findViewById(R.id.relative3);
            Linear_tagPeople = (LinearLayout) v.findViewById(R.id.relative3);
            divider_dots = (ImageView) v.findViewById(R.id.divider_dots);
            post_image = (ImageView) v.findViewById(R.id.post_image);
            linear_name = (LinearLayout) v.findViewById(R.id.linear_name);
            final int  position =  getCount() - 1 - pos;
            final  String PostKey = getRef(position).getKey();
            FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
            final String current_user_Id = id.getUid();

              getcomments(PostKey);
            if (model.getLikeInput() != null) {
                if (model.getLikeInput().size() > 0) {
                    for (int i = 0; i < model.getLikeInput().size(); i++) {
                        custId.add(model.getLikeInput().get(i));
                        Log.i("Customer Id ", model.getLikeInput().get(i).toString());
                        if (model.getLikeInput().get(i).equals(current_user_Id)) {
                            Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like_active));
                        }else{
                            Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }
                    }
                    LikeCount.setVisibility(View.VISIBLE);
                    if (model.getLikeInput().size() == 1) {
                        LikeCount.setText(model.getLikeInput().size() + "Like");
                    } else {
                        LikeCount.setText(model.getLikeInput().size() + "Likes");
                    }

                } else {
                    LikeCount.setVisibility(View.GONE);
                    Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like));
                }
            }else{
                Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like));
                LikeCount.setVisibility(View.GONE);
            }

            relative_Like.setOnClickListener(new View.OnClickListener() {
              //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    getLikeOnclick(model , PostKey);
                    GetLikeOnClickForLikeTableInDatabase(PostKey);
                }

            });



            option_menu_dots.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    getUid = firebaseUser.getUid();

                    if (getUid.equals(model.getUid())) {

                        final Dialog dialogxx = new Dialog(getActivity());
                        dialogxx.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogxx.setCancelable(true);
                        dialogxx.setContentView(R.layout.dialog);
                        TextView edit = (TextView) dialogxx.findViewById(R.id.edit);
                        TextView delete = (TextView) dialogxx.findViewById(R.id.delete);

                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), EditPost.class);
                                intent.putExtra("GetName", model.getProfilename());
                                intent.putExtra("GetImage", model.getProfilePic());
                                intent.putExtra("GetDescription", model.getPostMessage());
                                intent.putExtra("GetLocation", "" + model.getPlace());
                                intent.putExtra("PostKey", PostKey);
                                intent.putStringArrayListExtra("PhotoList", model.getAl_imagepath());
                                startActivity(intent);
                                dialogxx.dismiss();
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //dialogxx.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("delete the post");
                                builder.setMessage("Are you sure to delete the post ?");
                                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setTitle("Post Deleting");
                                        progressDialog.setMessage("Please wait..!!");
                                        progressDialog.show();
                                        dialogxx.dismiss();

                                        mDatabase.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isComplete()) {
                                                    databaseReferencepostImages.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isComplete()) {

                                                                mDatabaseComments.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isComplete()) {
                                                                            databaseReferenceDiary.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isComplete()) {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(getActivity(), "Post Successfully Deleted", Toast.LENGTH_SHORT).show();
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

                        dialogxx.show();

                    } else {
//                        PopupMenu popup = new PopupMenu(getActivity(), v,Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//                        MenuInflater inflater = popup.getMenuInflater();
//                        inflater.inflate(R.menu.popup_menu_receiver, popup.getMenu());
//                        popup.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//                        popup.show();

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog);
                        TextView edit = (TextView) dialog.findViewById(R.id.edit);
                        edit.setText("Report");
                        TextView delete = (TextView) dialog.findViewById(R.id.delete);
                        delete.setVisibility(View.GONE);


                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Report");
                                builder.setMessage("Do you want to report this post ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(current_user_Id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if(dataSnapshot.exists()){

                                                    Calendar calendarDate = Calendar.getInstance();
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                                                    CurrentDate = simpleDateFormat.format(calendarDate.getTime());

                                                    displayName = dataSnapshot.child("displayName").getValue().toString();
                                                    instanceId = dataSnapshot.child("instanceId").getValue().toString();
                                                    mobileNo = dataSnapshot.child("mobileNo").getValue().toString();
                                                    photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                                                    time = dataSnapshot.child("time").getValue().toString();
                                                    timeStamp = dataSnapshot.child("timeStamp").getValue().toString();
                                                    uid = dataSnapshot.child("uid").getValue().toString();
                                                    userName = dataSnapshot.child("userName").getValue().toString();

                                                    User user = new User();
                                                    user.setDisplayName(displayName);
                                                    user.setInstanceId(instanceId);
                                                    user.setMobileNo(mobileNo);
                                                    user.setPhotoUrl(photoUrl);
                                                    user.setTime(Long.parseLong(time));
                                                    user.setTimeStamp(Long.parseLong(timeStamp));
                                                    user.setUid(uid);
                                                    user.setCurrent_time(CurrentDate);
                                                    user.setUserName(userName);
                                                    user.setDefaultText("This Post was Reported");

                                                    dbReport.child(PostKey).child(current_user_Id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isComplete())
                                                            {
                                                                Toast.makeText(getActivity(),"The post has been reported",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                Dialog dialog1 = builder.create();
                                dialog1.show();
                                Log.e("GetPostKeyReport ",""+PostKey);
                            }
                        });

                        dialog.show();

                    }
                }
            });

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("GetPhotoPath :", "" + model.getProfilePic());

                    Log.e("GetUid :", "" + model.getUid());

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    getUid = firebaseUser.getUid();

                    if (getUid.equals(model.getUid())) {
                        Log.e("GetResult", "BothUidEqual");
                        Intent intent = new Intent(getActivity(), MyProfilePage.class);
                        startActivity(intent);
                    } else if (model.getUid() != null) {
                        //Log.e("GetResult","BothUidNotEqual");
                        Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                        i.putExtra("user_id", model.getUid());
                        startActivity(i);
                    }
                }
            });

            linear_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    getUid = firebaseUser.getUid();

                    if (getUid.equals(model.getUid())) {
                        Log.e("GetResult", "BothUidEqual");
                        Intent intent = new Intent(getActivity(), MyProfilePage.class);
                        startActivity(intent);
                    } else if (model.getUid() != null) {
                        String GetUid = getRef(position).getKey();
                        mDatabase.child(GetUid).child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    String Uid = dataSnapshot.getValue().toString();
                                    Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                                    i.putExtra("user_id", Uid);
                                    startActivity(i);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
            });

            Linear_Comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    //       String PostKey1 = getRef(position).getKey();
                    Log.e("PostKeyComments", "" + PostKey);
                    getUid = firebaseUser.getUid();
                    Intent intent1 = new Intent(getActivity(), CommentsActivity.class);
                    intent1.putExtra("GetUid", getUid);
                    intent1.putExtra("GetName", model.getProfilename());
                    intent1.putExtra("GetImage", model.getProfilePic());
                    intent1.putExtra("GetPostKey", PostKey);
                    startActivityForResult(intent1, 1);


                }
            });


            LikeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(getActivity(), LikeActivity.class);
                    intent2.putExtra("PostKey", PostKey);
                    intent2.putExtra("resultCode","FromActivity");
                    startActivity(intent2);

                }
            });

            CommentsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CommentsActivity.class);
                    intent.putExtra("GetPostKey", PostKey);
                    startActivityForResult(intent, 1);
                }
            });

            ProfileName.setText(model.getProfilename());
            Description.setText(model.getPostMessage());
            post_data.setText(model.getPostDate());
            post_time.setText(model.getPostTime());
            UserNameLocation.setText(model.getProfilename());
            String tagged_people = String.valueOf(model.getTagPeople_Path());
            String replacement = tagged_people.replaceAll("\\[", "").replaceAll("\\]", "");
            Log.e("GetReplacement", "" + replacement);
            String[] SpliteTagPeople = replacement.split(",");
            int length = SpliteTagPeople.length;

            if (model.getTagPeople_Path() == null) {
                tag_people.setText(" ");
                Linear_tagPeople.setVisibility(View.GONE);
            } else if (model.getTagPeople_Path() != null) {
                if (length == 1) {
                    mTagPeople = SpliteTagPeople[0];
                } else if (length == 2) {
                    mTagPeople = SpliteTagPeople[0] + " and " + SpliteTagPeople[1];
                } else if (length > 2) {
                    String others = "others";
                    SpannableString whiteSpannable = new SpannableString(others);
                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, others.length(), 0);
                    mTagPeople = SpliteTagPeople[0] + "," + SpliteTagPeople[1] + " and " + (SpliteTagPeople.length - 2) + " " + others;
                }
                tag_people.setText(mTagPeople);
                Linear_tagPeople.setVisibility(View.VISIBLE);
            }

            if (model.getPostMessage() == null) {
                Description.setVisibility(View.GONE);
            } else {
                Description.setVisibility(View.VISIBLE);
            }
            if (model.getPlace() == null) {
                linearLayout_location_row.setVisibility(View.GONE);
            } else if (model.getPlace() != null) {
                Place.setText(model.getPlace());
                linearLayout_location_row.setVisibility(View.VISIBLE);
            }

            CommentsCount.setText("");
            divider_dots.setVisibility(View.GONE);

            databaseReference.child(current_user_Id).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String profilePicurl = dataSnapshot.getValue().toString();
                        if (model.getUid().contains(current_user_Id)) {
                            Glide.with(getActivity())
                                    .load(profilePicurl)
                                    .placeholder(R.mipmap.profile)
                                    .centerCrop()
                                    .dontAnimate()
                                    .crossFade()
                                    .fitCenter()
                                    .bitmapTransform(new CropCircleTransformation(getContext()))
                                    .into(profilePic);
                        } else {
                            String GetUid = getRef(position).getKey();
                            mDatabase.child(GetUid).child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String Uid = dataSnapshot.getValue().toString();

                                        databaseReference.child(Uid).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String getPhotoUrl = dataSnapshot.getValue().toString();
                                                    Glide.with(getActivity())
                                                            .load(getPhotoUrl)
                                                            .placeholder(R.mipmap.profile)
                                                            .centerCrop()
                                                            .dontAnimate()
                                                            .crossFade()
                                                            .fitCenter()
                                                            .bitmapTransform(new CropCircleTransformation(getContext()))
                                                            .into(profilePic);
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
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            if (model.getPostImage() != null) {
                if (model.getPostImage().contains(".jpg") || model.getPostImage().contains(".jpge")
                        || model.getPostImage().contains(".png") || model.getPostImage().contains(".JPG")
                        || model.getPostImage().contains(".gif")) {
                    Glide.with(getActivity())
                            .load(model.getPostImage())
                            .dontAnimate()
                            .fitCenter()
                            .placeholder(getResources().getDrawable(R.drawable.image_placeholder))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(post_image);
                    post_image.setVisibility(View.VISIBLE);
                }
            } else {
                if (model.getPostImage() == null) {
                    post_image.setVisibility(View.GONE);
                }
            }

        }


    }

    private void getLikeOnclick(Post_Model model, String PostKey) {
        isFirstTime = true;
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        final String current_user_Id = id.getUid();
        custId.clear();
        if (isFirstTime) {
            if (model.getLikeInput() != null) {
                for (int i = 0; i < model.getLikeInput().size(); i++) {
                    if (model.getLikeInput().get(i).contains(current_user_Id)) {
                        model.getLikeInput().remove(current_user_Id);
                        mDatabase.child(PostKey).child("likeInput").setValue(model.getLikeInput());
                        Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        isFirstTime = false;
                    }else{
                        custId.add(model.getLikeInput().get(i));
                        custId.add(current_user_Id);
                        mDatabase.child(PostKey).child("likeInput").setValue(custId);
                        Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like_active));
                    }

                }
            } else {
                isFirstTime = false;
                custId.add(current_user_Id);
                mDatabase.child(PostKey).child("likeInput").setValue(custId);
                Like_Inactive.setImageDrawable(getResources().getDrawable(R.drawable.like_active));
            }

        }

    }

    private void GetLikeOnClickForLikeTableInDatabase(final String PostKey)
    {
        mProcesslike = true;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId_current = firebaseUser.getUid();

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users");
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = simpleDateFormat.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
        CurrentTime = simpleTimeFormat.format(calendarTime.getTime());

        RandomSave = CurrentDate + CurrentTime;
        //Like_Inactive.setImageDrawable(getActivity().getDrawable(R.drawable.like_active));


        databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mProcesslike) {
                    Log.e("BooleanValue ",""+mProcesslike);
                    //Like_Inactive.setImageDrawable(getActivity().getDrawable(R.drawable.like_active));

                    if (dataSnapshot.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).hasChild(mAuth.getCurrentUser().getUid())) {

                        databaseReferenceLikes.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).removeValue();
                        //Like_Inactive.setImageDrawable(getActivity().getDrawable(R.drawable.like));

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

                                        databaseReferenceLikes.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).setValue(hashMap);
                                        //Like_Inactive.setImageDrawable(getActivity().getDrawable(R.drawable.like_active));
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


    private void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth", "" + mAuth);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("current user", "" + user);
                if (user != null) {
                    getUid = user.getUid();
                } else {
                    Toast.makeText(getActivity(), "Oops ! Something  Wrong from Our side . Please Refresh app ", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void getcomments(String postKey) {
        mDatabaseComments.child(postKey).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countComments = dataSnapshot.getChildrenCount();
                    Log.e("CommentsCount", "" + countComments);
                    CommentsCount.setText(countComments + " Comments");
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
        Intent ss = new Intent(getActivity(), CameraActivity.class);
        ss.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ss.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(ss);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Gulshan " , requestCode  + " ????????" + resultCode +  " ?????" + data.toString());
        if (requestCode == 1) {
            if (resultCode == 0) {
                String PostKey = data.getStringExtra("PostKey");
                mDatabaseComments.child(PostKey).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            countComments = dataSnapshot.getChildrenCount();
                            Log.e("CommentsCount", "" + countComments);
                            CommentsCount.setText(countComments + " Comments");
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
                //Toast.makeText(getActivity(),PostKey.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
    }
}