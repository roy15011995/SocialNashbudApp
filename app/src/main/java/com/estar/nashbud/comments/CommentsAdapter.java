package com.estar.nashbud.comments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.EditPost;
import com.estar.nashbud.chatscreenpages.LikeActivity;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.profile.ShowMyProfilePost;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.Message;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.InternetUtil;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/*
 * Created by Mahmoud on 3/13/2017.
 */

class CommentsAdapter extends FirebaseRecyclerAdapter<Comments, CommentsAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_SENT_WITH_PROFILEIMAGE = 1;
    private static final int VIEW_TYPE_SENT_WITH_PROFILENAME = 2;
    private static final int VIEW_TYPE_RECEIVED = 1;
    private static final int VIEW_TYPE_RECEIVED_WITH_PROFILEIMAGE = 4;
    private static final int VIEW_TYPE_RECEIVED_WITH_PROFILENAME = 5;


    private final String ownerUid;
    private final Context context;
    private ArrayList<Integer> selectedPositions;
    private Boolean isConnect;
    InternetUtil internetUtil;
    FirebaseUser firebaseUser;
    String getUid,GetPostKey;
    DatabaseReference databaseReference,databaseReferenceEditComments,
            databaseReferenceComments,databaseReferenceCOmmentsLike,databaseReferenceUser;
    private FirebaseAuth mAuth;
    Comments message = new Comments();
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View view;
    LayoutInflater layoutInflater = null;
    EditText EditComments;
    FloatingActionButton sent;
    Bundle Extra;
    ProgressDialog progressDialog;
    private boolean mProcesslike = false;
    String userId_current,CurrentDate,CurrentTime,RandomSave;
    long countLikes = 0;
    String displayName,instanceId,mobileNo,photoUrl,time,timeStamp,uid,userName;
    DatabaseReference dbReport;



    CommentsAdapter(Context context, String ownerUid, Query ref) {
        super(Comments.class, R.layout.item_comments_sent_received, MessageViewHolder.class, ref);
        this.context = context;
        this.ownerUid = ownerUid;
        selectedPositions = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        databaseReferenceCOmmentsLike = FirebaseDatabase.getInstance().getReference().child("CommentsLike");
        builder = new AlertDialog.Builder(context);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.dialog_comments_edit,null);
        EditComments = (EditText)view.findViewById(R.id.caption);
        sent = (FloatingActionButton)view.findViewById(R.id.fab_Caption_Image_send);

        progressDialog = new ProgressDialog(context);

        builder.setView(view);
        alertDialog=builder.create();


        Extra = ((Activity)context).getIntent().getExtras();
        if(Extra!=null)
        {
            GetPostKey = Extra.getString("GetPostKey");
            Log.e("GetPostKeyComments ",""+GetPostKey);
        }

        databaseReferenceEditComments = FirebaseDatabase.getInstance().getReference()
                .child("Comments").child(GetPostKey).child("item");

        databaseReferenceComments = FirebaseDatabase.getInstance().getReference().child("users");
        dbReport = FirebaseDatabase.getInstance().getReference().child("Comments Report");
        dbReport.keepSynced(true);

    }

    @Override
    protected void populateViewHolder(final MessageViewHolder holder, Comments message, int position) {


        holder.setMessage(message);
        holder.SetOptionMenu(message,position);
        holder.GetPhoto(position);
        holder.OperationCommentsLike(message,position);
        holder.setBtnLike(CommentsAdapter.this.getRef(position).getKey());
        holder.OpenProfileOnPhotoClick(position);
        holder.OpenProfileOnLinearNameClick(position);
        holder.OpenNoOfLikeOnLikeCountClick(position);

       /* isConnect = internetUtil.isConnected(context);
        if(!isConnect){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        holder.clock.setVisibility(VISIBLE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },1000);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        holder.Double_Tick_Unseen.setVisibility(VISIBLE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },1000);
        }
*/



    }

    @Override
    public int getItemViewType(int position) {
        Comments message = getItem(position);
        if (message.getFrom().equals(ownerUid)) {
            if (position == getItemCount() - 1 || selectedPositions.contains(position) ||
                    getItem(position + 1).getDayTimestamp() != message.getDayTimestamp()) {

                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_SENT;
            }
        } else {
            if (position == getItemCount() - 1 || selectedPositions.contains(position) ||
                    getItem(position + 1).getDayTimestamp() != message.getDayTimestamp()) {
                return VIEW_TYPE_RECEIVED;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_SENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_comments_sent_received, parent, false);
                break;

            case VIEW_TYPE_RECEIVED:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_comments_sent_received, parent, false);
                break;

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_comments_sent_received, parent, false);
        }
        return new MessageViewHolder(itemView);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

       /* @BindView(R.id.comments_time)
        TextView itemMessageDateTextView;*/
        @BindView(R.id.item_comments_message_text_view)
        TextView itemMessageBodyTextView;
        @BindView(R.id.item_message_parent)
        LinearLayout itemMessageParent;
        @Nullable
        @BindView(R.id.comments_time)
        TextView Comments_Time;
        @Nullable
        @BindView(R.id.linear_message_body)
        LinearLayout linear_message_body;
        @Nullable
        @BindView(R.id.comments_photo)
        ImageView ProfilePic;
        @BindView(R.id.item_comments_name_text_view)
        TextView ProfileName;
        @BindView(R.id.dots)
        ImageView option_menu_dots;
        @BindView(R.id.comments_like_count)
        TextView likeCount;
        @BindView(R.id.comments_likes)
        TextView like;
        @BindView(R.id.linear_comments_likes)
        LinearLayout linear_like;
        @BindView(R.id.linear_comments_like_count)
        LinearLayout linear_like_count;
        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*itemMessageBodyTextView.setOnClickListener(this);
            itemMessageBodyTextView.setOnLongClickListener(this);*/

            itemMessageBodyTextView.setOnClickListener(this);
            option_menu_dots.setOnClickListener(this);
            itemMessageBodyTextView.setOnLongClickListener(this);

        }

        void setMessage(Comments message) {
            int viewType = CommentsAdapter.this.getItemViewType(getLayoutPosition());
            itemMessageBodyTextView.setText(message.getBody());
            Log.e("message is","" + message.getBody());
            try{
                Comments_Time.setText(getTimePretty(message.getTimestamp()));
            }
            catch (Exception e){
                e.printStackTrace();
            }

           ProfileName.setText(message.getDisplayName());

            databaseReference.child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        String PhotoUrl = dataSnapshot.getValue().toString();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void OpenNoOfLikeOnLikeCountClick(final int Position)
        {
            linear_like_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostKey = CommentsAdapter.this.getRef(Position).getKey();
                    Intent intent = new Intent(context, LikeActivity.class);
                    intent.putExtra("PostKey", PostKey);
                    intent.putExtra("resultCode","FromComment");
                    context.startActivity(intent);
                }
            });
        }

        public void OpenProfileOnPhotoClick(/*final Comments comments,*/final int Position)
        {
            ProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostKey = CommentsAdapter.this.getRef(Position).getKey();
                    databaseReferenceEditComments.child(PostKey).child("from").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String GetUid = dataSnapshot.getValue().toString();
                            if(GetUid.equals(mAuth.getCurrentUser().getUid()))
                            {
                                Intent intent = new Intent(context, MyProfilePage.class);
                                context.startActivity(intent);
                            }
                            else
                            {
                                Intent i = new Intent(context, FriendsProfileActivity.class);
                                i.putExtra("user_id", GetUid);
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        public void OpenProfileOnLinearNameClick(/*final Comments comments,*/final int Position)
        {
            linear_message_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostKey = CommentsAdapter.this.getRef(Position).getKey();
                    databaseReferenceEditComments.child(PostKey).child("from").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String GetUid = dataSnapshot.getValue().toString();
                            if(GetUid.equals(mAuth.getCurrentUser().getUid()))
                            {
                                Intent intent = new Intent(context, MyProfilePage.class);
                                context.startActivity(intent);
                            }
                            else
                            {
                                Intent i = new Intent(context, FriendsProfileActivity.class);
                                i.putExtra("user_id", GetUid);
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }

        public void GetPhoto(int position)
        {
            String PostKey = CommentsAdapter.this.getRef(position).getKey();
            databaseReferenceEditComments.child(PostKey).child("from").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String GetUid = dataSnapshot.getValue().toString();
                        //Log.e("GetUidComments ",""+GetUid);

                      databaseReferenceComments.child(GetUid).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {

                              if(dataSnapshot.exists())
                              {
                                  String PhotoUrl = dataSnapshot.child("photoUrl").getValue().toString();

                                   Glide.with(context)
                                    .load(PhotoUrl)
                                    .placeholder(R.drawable.profile)
                                    .crossFade()
                                    .centerCrop()
                                    .fitCenter()
                                    .bitmapTransform(new CropCircleTransformation(context))
                                    .dontAnimate()
                                    .into(ProfilePic);
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

        public void SetOptionMenu(final Comments comments , final int position)
        {
            option_menu_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                String Uid = dataSnapshot.child("uid").getValue().toString();

                                if(comments.getFrom().contains(Uid))
                                {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.dialog);
                                    TextView edit = (TextView) dialog.findViewById(R.id.edit);
                                    TextView delete = (TextView) dialog.findViewById(R.id.delete);
                                    edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                            alertDialog.show();
                                            final String PostKey = CommentsAdapter.this.getRef(position).getKey();
                                            Log.e("GetPostKey ",""+PostKey);

                                            message = CommentsAdapter.this.getItem(position);
                                            EditComments.setText(message.getBody());

                                            sent.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    progressDialog.setTitle("Editing on Progress.....");
                                                    progressDialog.setMessage("Please wait....");
                                                    progressDialog.show();
                                                    progressDialog.setCanceledOnTouchOutside(false);

                                                    databaseReferenceEditComments.child(PostKey).child("body").setValue(EditComments.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isComplete())
                                                            {
                                                                ((Activity)context).finish();
                                                                progressDialog.dismiss();

                                                                Toast.makeText(context,"Edited Successfully",Toast.LENGTH_SHORT).show();
                                                            }
                                                            else
                                                            {
                                                                String messages = task.getException().getMessage();
                                                                Toast.makeText(context,"Failed to Upload...Pls Try again Later "+messages,Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                    });
                                                }
                                            });

                                        }
                                    });


                                    delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            AlertDialog.Builder builder=new AlertDialog.Builder(context);

                                            builder.setTitle("Delete the Comment");
                                            builder.setMessage("Are you sure to delete the Comment ?");
                                            builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    CommentsAdapter.this.getRef(position).removeValue();
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


                                 //   Dialog dialog = builder.create();
                                    dialog.show();
                                }
                                else
                                {
                                   /* PopupMenu popup = new PopupMenu(context, v,Gravity.CENTER);
                                    MenuInflater inflater = popup.getMenuInflater();
                                    inflater.inflate(R.menu.popup_menu_other_user_comments, popup.getMenu());
                                    popup.setGravity(Gravity.CENTER_HORIZONTAL);
                                    popup.show();*/

                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setContentView(R.layout.dialog);
                                    TextView edit = (TextView) dialog.findViewById(R.id.edit);
                                    TextView delete = (TextView) dialog.findViewById(R.id.delete);
                                    delete.setVisibility(GONE);

                                    edit.setText("Report");
                                    edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                            databaseReference.addValueEventListener(new ValueEventListener() {
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
                                                        user.setDefaultText("This Comment has been Reported");

                                                        dbReport.child(GetPostKey).child(CommentsAdapter.this.getRef(position).getKey()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isComplete())
                                                                {
                                                                    Toast.makeText(context,"The comment has been reported",Toast.LENGTH_SHORT).show();
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
                                    });
                                        dialog.show();
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
        public void getLike(String postKey) {
            likeCount.setVisibility(View.VISIBLE);
            databaseReferenceCOmmentsLike.child(postKey).child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        countLikes = dataSnapshot.getChildrenCount();
                        Log.e("LikesCount", "" + countLikes);
                        if (countLikes > 0) {
                            if (countLikes == 1) {
                                likeCount.setText(countLikes + " Like");
                            } else {
                                likeCount.setText(countLikes + " Likes");
                            }
                        } else {
                            likeCount.setText("");
                        }

                        /* divider_dots.setVisibility(View.VISIBLE);
                        if(!dataSnapshot.exists())
                        {
                            divider_dots.setVisibility(View.GONE);
                        }*/
                    } else {
                        likeCount.setText("");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        private void OperationCommentsLike(final Comments comments,final int Position)
        {
            linear_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String PostKey = CommentsAdapter.this.getRef(Position).getKey();
                    Log.e("GetFromCommentsUid ",""+comments.getFrom());

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


                    databaseReferenceCOmmentsLike.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (mProcesslike) {
                                like.setTextColor(R.color.gray);

                                if (dataSnapshot.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).hasChild(mAuth.getCurrentUser().getUid())) {

                                    databaseReferenceCOmmentsLike.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).removeValue();
                                    getLike(PostKey);
                                    like.setTextColor(R.color.colorPrimaryDark);

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

                                                    databaseReferenceCOmmentsLike.child(PostKey).child("Like").child(mAuth.getCurrentUser().getUid()).setValue(hashMap);
                                                    like.setTextColor(R.color.colorPrimaryDark);
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
            databaseReferenceCOmmentsLike.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(PostKey).child("Like").hasChild(mAuth.getCurrentUser().getUid())) {
                        like.setTextColor(R.color.colorPrimaryDark);
                        getLike(PostKey);
                    } else {
                        like.setTextColor(R.color.gray);
                        getLike(PostKey);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void LikeCountOnClickCall(final String PostKey)
        {
            likeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    databaseReferenceCOmmentsLike.child(PostKey).child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Comments comments = dataSnapshot.getValue(Comments.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent intent2 = new Intent(context, LikeActivity.class);
                    intent2.putExtra("PostKey", PostKey);
                    context.startActivity(intent2);

                }
            });
        }

        @Nullable
        @Override
        public void onClick(final View v) {
           /* if (selectedPositions.contains(getLayoutPosition())) {
                selectedPositions.remove(Integer.valueOf(getLayoutPosition()));
                setDateVisibility(GONE);
            } else {
                selectedPositions.add(getLayoutPosition());
                setDateVisibility(VISIBLE);
            }*/

           int id = v.getId();
           switch (id)
           {
               case R.id.dots:

           }

        }

        /*private void setDateVisibility(int visibility) {
            TransitionManager.beginDelayedTransition(itemMessageParent);
            itemMessageDateTextView.setVisibility(visibility);
        }*/
        @Nullable
        @Override
        public boolean onLongClick(View v) {
            Comments message = getItem(getLayoutPosition());
            if (message != null) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(
                        context.getString(R.string.clipboard_title_copied_message),
                        message.getBody());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.message_message_copied, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    private String getDatePretty(long timestamp, boolean showTimeOfDay) {
        DateTime yesterdayDT = new DateTime(DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
        yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
        Interval today = new Interval(DateTime.now().withTimeAtStartOfDay(), Days.ONE);
        Interval yesterday = new Interval(yesterdayDT, Days.ONE);
        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.mediumDate();
        if (today.contains(timestamp)) {
            /*if (showTimeOfDay) {
                return timeFormatter.print(timestamp);

            } else {
                return context.getString(R.string.today);
            }*/
            return context.getString(R.string.today);

        } else if (yesterday.contains(timestamp)) {
            return context.getString(R.string.yesterday);
        } else {
            return dateFormatter.print(timestamp);
        }
    }

    private String getTimePretty(long timestamp) {
        DateTime yesterdayDT = new DateTime(DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
        yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
        Interval today = new Interval(DateTime.now().withTimeAtStartOfDay(), Days.ONE);
        Interval yesterday = new Interval(yesterdayDT, Days.ONE);
        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
        if (today.contains(timestamp)) {
            return timeFormatter.print(timestamp);
        }
        else if(yesterday.contains(timestamp)){
            return timeFormatter.print(timestamp);
        }
        else {
            return timeFormatter.print(timestamp);
        }
    }

}
