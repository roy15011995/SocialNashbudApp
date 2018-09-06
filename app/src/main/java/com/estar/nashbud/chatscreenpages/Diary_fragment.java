package com.estar.nashbud.chatscreenpages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estar.nashbud.R;
import com.estar.nashbud.comments.CommentsActivity;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.profile.ShowMyProfilePost;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Amit on 9/20/2017.
 */

public class Diary_fragment extends Fragment {

    LinearLayout text_whats_going;
    ListView listView;
    String getUid;
    DatabaseReference databaseReference,mDatabase,databaseReferenceLikes,mDatabaseComments,databaseReferencePost,
            databaseReferenceDiary,databaseReferencepostImage;
    private FirebaseAuth mAuth,firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageView profilePic,PostPic,Like_Inactive,Like_Active,option_menu_dots,divider_dots,post_image;
    TextView ProfileName, Description,post_data,post_time,UserNameLocation,Place,
            LikeCount,CommentsCount,tag_people,post_date_header;
    ArrayList<Post_Model> arrayList=new ArrayList<>();
    ListViewAdpater listViewAdpater;
    LinearLayout linearLayout_location_row,Linear_tagPeople,RelativeLikeCommentsReposts,LinearView,linear_emptyView;
    FirebaseUser firebaseUser;
    String[]mStringArray;
    ArrayList<Post_Model> list=new ArrayList<>();
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private TextView[] dots;
    SliderPagerAdapter sliderPagerAdapter;
    int page_position = 0;
    ArrayList<String> slider_image_list;
    String OnlineUser,mTagPeople;
    long countLikes=0;
    long countComments=0;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.diary_fragment,container,false);

        listView=(ListView)view.findViewById(R.id.Diary_list);
        linear_emptyView = (LinearLayout)view.findViewById(R.id.linear_emptyView_diary);
        firebaseAuth= FirebaseAuth.getInstance();
        OnlineUser = firebaseAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        databaseReferenceLikes = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReferenceLikes.keepSynced(true);
        mDatabaseComments = FirebaseDatabase.getInstance().getReference().child("Comments");
        mDatabaseComments.keepSynced(true);
        databaseReferencePost = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReferenceDiary = FirebaseDatabase.getInstance().getReference().child("Diary");
        databaseReferencepostImage = FirebaseDatabase.getInstance().getReference().child("post_images");


        Log.e("CurrentUId",""+OnlineUser);

        initializeUsersRecycler();


        return view;
    }


    private void initializeUsersRecycler() {

        //mSelectedItemsIds = new SparseBooleanArray();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Diary"))
                {
                    mDatabase.child("Diary").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    String User=data.child("uid").getValue().toString();
                                    if(User.contains(OnlineUser))
                                    {
                                        listViewAdpater = new ListViewAdpater(getActivity(),
                                                Post_Model.class, R.layout.activity_row_layout, mDatabase.child("Diary").orderByChild("uid").equalTo(OnlineUser));
                                        listView.setAdapter(listViewAdpater);

                                        mDatabase.child("Diary").orderByChild("uid").equalTo(OnlineUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if(!dataSnapshot.exists())
                                                {
                                                    linear_emptyView.setVisibility(View.VISIBLE);
                                                    listView.setVisibility(View.GONE);
                                                    Log.e("IfCall ","Yes");
                                                }
                                                else
                                                {
                                                    linear_emptyView.setVisibility(View.GONE);
                                                    listView.setVisibility(View.VISIBLE);
                                                    Log.e("ElseCall ","Yes");
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    linear_emptyView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public class ListViewAdpater extends FirebaseListAdapter<Post_Model> {

        View mView;
        //boolean status=true;
        private SparseBooleanArray mSelectedItemsIds;

        public ListViewAdpater(Activity activity, Class<Post_Model> modelClass, int modelLayout, Query ref) {
            super(activity, modelClass, modelLayout, ref);
            mSelectedItemsIds = new SparseBooleanArray();
        }

        @Override
        public Post_Model getItem(int position) {
            return super.getItem(getCount() - 1 - position);
        }

        @Override
        protected void populateView(View v, final Post_Model model, final int position) {
            databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
            profilePic=(ImageView)v.findViewById(R.id.post_user_pic);
            //PostPic =(ImageView)v.findViewById(R.id.post_pic);
            ProfileName=(TextView)v.findViewById(R.id.post_user_name);
            Description=(TextView)v.findViewById(R.id.post_description);
            post_data = (TextView)v.findViewById(R.id.post_date);
            post_time =(TextView)v.findViewById(R.id.post_time);
            UserNameLocation = (TextView)v.findViewById(R.id.name_location);
            Place = (TextView)v.findViewById(R.id.post_place);
            Like_Inactive = (ImageView)v.findViewById(R.id.like_inactive);
            //Like_Active = (ImageView)v.findViewById(R.id.like_active);
            linearLayout_location_row = (LinearLayout)v.findViewById(R.id.linear_location_row);
            vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
            LikeCount = (TextView)v.findViewById(R.id.like_Count);
            CommentsCount = (TextView)v.findViewById(R.id.comments_count);
            tag_people= (TextView)v.findViewById(R.id.tagged_with);
            Linear_tagPeople = (LinearLayout)v.findViewById(R.id.relative3);
            RelativeLikeCommentsReposts = (LinearLayout)v.findViewById(R.id.relative5);
            LinearView = (LinearLayout)v.findViewById(R.id.linear_view);
            option_menu_dots = (ImageView)v.findViewById(R.id.dots);
            post_date_header = (TextView)v.findViewById(R.id.post_date_header);
            linearLayout = (LinearLayout) v.findViewById(R.id.linear_date);
            divider_dots = (ImageView)v.findViewById(R.id.divider_dots);
            linearLayout.setVisibility(View.VISIBLE);

            LinearView.setVisibility(View.GONE);

            RelativeLikeCommentsReposts.setVisibility(View.GONE);
            post_image = (ImageView)v.findViewById(R.id.post_image);
            //post_image.setRotation(90F);
          //  final String PostKey = getRef(position).getKey();
            //Log.d("ImagePath" , model.getPostImage());
            if(model.getPostImage() != null) {
                if (model.getPostImage().contains(".jpg") || model.getPostImage().contains(".jpge")
                        || model.getPostImage().contains(".png") ||model.getPostImage().contains(".JPG")||
                        model.getPostImage().contains(".gif")) {
                    Glide.with(getActivity())
                            .load(model.getPostImage())
                            .placeholder(getResources().getDrawable(R.drawable.image_placeholder))
                            .dontAnimate()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(post_image);
                    post_image.setVisibility(View.VISIBLE);

                    //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                }

            }else {
                if( model.getPostImage()==null)
                {
                    post_image.setVisibility(View.GONE);
                }
            }
            //ll_dots = (LinearLayout) v.findViewById(R.id.ll_dots);

            //Like_Active.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
            //Like_Inactive.setVisibility(isSelected(position) ? View.INVISIBLE : View.VISIBLE);
            final String PostKey = getRef(position).getKey();

            slider_image_list = model.getAl_imagepath();

            if(model.getAl_imagepath()!=null){
                mStringArray = new String[slider_image_list.size()];
                mStringArray = slider_image_list.toArray(mStringArray);

                Log.e("mStringArray",""+mStringArray.length);

                list.clear();

                for (int i1=0;i1<mStringArray.length;i1++){

                    //list_tag.add(i1,new Tag_Model(GetListItemList));

                    Post_Model post_model = new Post_Model();
                    post_model.setImagePath(mStringArray[i1]);

                    list.add(post_model);
                }
            }


            // method for initialisation
            //init();

            // method for adding indicators

            //addBottomDots(0);

            // Automatic sliderMoving Code

            /*final Handler handler = new Handler();


       final Runnable update = new Runnable() {
                public void run() {
                    if (page_position == list.size()) {
                        page_position = 0;
                    } else {
                        page_position = page_position + 1;
                    }
                    vp_slider.setCurrentItem(page_position, true);
                }
            };

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 100, 5000);*/

            final String user_login_id=getRef(position).getKey();
            Log.e("LoginId",user_login_id);


            FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();

            final String current_user_Id=id.getUid();

            Log.e("Current_User_Id",""+current_user_Id);


            Log.e("Display Name :","" + model.getPostFullName()+" "+"Number :"+model.getMobile_number()+" "+"Description :"+model.getPostMessage()+" "
                    +"Current Time "+model.getPostTime()+" "+"Current Date"+model.getPostDate()+" "+"UserPic"+model.getProfilePic()
                    +" "+"PostPic"+model.getPostImage());

            databaseReference.child(current_user_Id).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String ProfilePic = dataSnapshot.getValue().toString();

                        if (model.getUid().contains(current_user_Id)) {
                            Glide.with(getActivity())
                                    .load(ProfilePic)
                                    .placeholder(R.mipmap.profile)
                                    .centerCrop()
                                    .dontAnimate()
                                    .crossFade()
                                    .fitCenter()
                                    .bitmapTransform(new CropCircleTransformation(getContext()))
                                    .into(profilePic);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ProfileName.setText(model.getProfilename());
            Description.setText(model.getPostMessage());
            post_date_header.setText(model.getPostDate());
            post_data.setVisibility(View.GONE);
            post_time.setText(model.getPostTime());
            UserNameLocation.setText(model.getProfilename());

            if(model.getPlace()==null){
                linearLayout_location_row.setVisibility(View.GONE);
            }
            else if(model.getPlace()!=null){
                Place.setText(model.getPlace());
                linearLayout_location_row.setVisibility(View.VISIBLE);
            }


            if(model.getPostMessage()==null)
            {
                Description.setVisibility(View.GONE);
            }
            else
            {
                Description.setVisibility(View.VISIBLE);
            }


            String tagged_people= String.valueOf(model.getTagPeople_Path());
            String replacement = tagged_people.replaceAll("\\[", "").replaceAll("\\]","");
            Log.e("GetReplacement",""+replacement);
            String[]SpliteTagPeople = replacement.split(",");
            int length=SpliteTagPeople.length;

            if(model.getTagPeople_Path()==null){
                tag_people.setText(" ");
                Linear_tagPeople.setVisibility(View.GONE);
            }
            else if(model.getTagPeople_Path()!=null){

                if(length==1){
                    mTagPeople = SpliteTagPeople[0];
                }
                else if(length==2){
                    mTagPeople = SpliteTagPeople[0] + " and "+SpliteTagPeople[1];
                }
                else if(length>2){
                    String others = "others";
                    SpannableString whiteSpannable= new SpannableString(others);
                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, others.length(), 0);

                    mTagPeople = SpliteTagPeople[0]+","+SpliteTagPeople[1]+" and "+(SpliteTagPeople.length-2)+" "+others;
                }

               /* int size= model.getTagPeople_Path().size();
                int size1= model.getTagPeople_Path().size()-1;
                Log.e("ArraySize",""+size);
                Log.e("ModifiedArraySize",""+size1);*/

                tag_people.setText(mTagPeople);
                Linear_tagPeople.setVisibility(View.VISIBLE);
            }

            option_menu_dots.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    getUid = firebaseUser.getUid();

                    if(getUid.equals(model.getUid())){

                        final Dialog dialogA = new Dialog(getActivity());
                        dialogA.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogA.setCancelable(true);
                        dialogA.setContentView(R.layout.dialog);
                        TextView edit = (TextView) dialogA.findViewById(R.id.edit);
                        TextView delete = (TextView) dialogA.findViewById(R.id.delete);

                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogA.dismiss();
                                Intent intent = new Intent(getActivity(),EditPost.class);
                                intent.putExtra("GetName",model.getProfilename());
                                intent.putExtra("GetImage",model.getProfilePic());
                                intent.putExtra("GetDescription",model.getPostMessage());
                                intent.putExtra("GetLocation",""+model.getPlace());
                                intent.putExtra("PostKey",PostKey);
                                intent.putStringArrayListExtra("PhotoList",model.getAl_imagepath());
                                startActivity(intent);
                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

                                builder.setTitle("delete the post");
                                builder.setMessage("Are you sure to delete the post ?");
                                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialogA.dismiss();
                                        progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setTitle("Post Deleting");
                                        progressDialog.setMessage("Please wait..!!");
                                        progressDialog.show();

                                        databaseReferenceDiary.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isComplete())
                                                {
                                                    databaseReferencePost.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isComplete())
                                                            {
                                                                databaseReferencepostImage.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isComplete())
                                                                            {
                                                                                        mDatabaseComments.child(PostKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isComplete())
                                                                                                {
                                                                                                    Toast.makeText(getActivity(), "Post Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                                                                    progressDialog.dismiss();
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

                        dialogA.show();


                    }
                    else
                    {
//                        PopupMenu popup = new PopupMenu(getActivity(), v,Gravity.CENTER);
//                        MenuInflater inflater = popup.getMenuInflater();
//                        inflater.inflate(R.menu.popup_menu_receiver, popup.getMenu());
//                        popup.setGravity(Gravity.CENTER_HORIZONTAL);
//                        popup.show();
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog);
                        TextView edit = (TextView) dialog.findViewById(R.id.edit);
                        edit.setText("");
                        TextView delete = (TextView) dialog.findViewById(R.id.delete);
                        delete.setVisibility(View.GONE);
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        dialog.show();

                    }
                }
            });


            LikeCount.setText(" ");

            databaseReferenceLikes.child(PostKey).child("likeInput").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        countLikes = dataSnapshot.getChildrenCount();
                        Log.e("LikesCount",""+countLikes);
                        LikeCount.setText(countLikes + " Likes");

                       /* divider_dots.setVisibility(View.VISIBLE);
                        if(!dataSnapshot.exists())
                        {
                            divider_dots.setVisibility(View.GONE);
                        }*/
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            LikeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent2 = new Intent(getActivity(),LikeActivity.class);
                    intent2.putExtra("PostKey",PostKey);
                    startActivity(intent2);


                }
            });

            CommentsCount.setText(" ");
            divider_dots.setVisibility(View.GONE);

            mDatabaseComments.child(PostKey).child("item").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        countComments = dataSnapshot.getChildrenCount();
                        Log.e("CommentsCount",""+countComments);
                        CommentsCount.setText(countComments + " Comments");

                        divider_dots.setVisibility(View.VISIBLE);
                        if(!dataSnapshot.exists())
                        {
                            divider_dots.setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            CommentsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PostKey4= getRef(position).getKey();
                    Intent intent = new Intent(getActivity(),CommentsActivity.class);
                    intent.putExtra("GetPostKey",PostKey4);
                    startActivity(intent);
                }
            });



            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("GetPhotoPath :",""+model.getProfilePic());

                    Log.e("GetUid :",""+model.getUid());

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    getUid = firebaseUser.getUid();

                    if (getUid.equals(model.getUid())){
                        Log.e("GetResult","BothUidEqual");
                        Intent intent = new Intent(getActivity(), MyProfilePage.class);
                        startActivity(intent);
                    }
                    else if(model.getUid()!=null)
                    {
                        //Log.e("GetResult","BothUidNotEqual");
                        Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                        i.putExtra("user_id",model.getUid());
                        startActivity(i);
                    }


                }
            });

            ProfileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("GetPhotoPath :",""+model.getProfilePic());

                    Log.e("GetUid :",""+model.getUid());

                    Log.e("GetProfileName :",""+model.getProfilename());

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    getUid = firebaseUser.getUid();

                    if (getUid.equals(model.getUid())){
                        Log.e("GetResult","BothUidEqual");
                        Intent intent = new Intent(getActivity(), MyProfilePage.class);
                        startActivity(intent);
                    }
                    else if(model.getUid()!=null)
                    {
                        //Log.e("GetResult","BothUidNotEqual");
                        Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                        i.putExtra("user_id",model.getUid());
                        startActivity(i);
                    }

                }
            });




        }

    }

    private void init() {

        try{
            sliderPagerAdapter = new SliderPagerAdapter(getActivity(),list);
            vp_slider.setAdapter(sliderPagerAdapter);

            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //addBottomDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(Color.parseColor("#616161"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#ff33b5e5"));
            dots[currentPage].setTextSize(35);
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