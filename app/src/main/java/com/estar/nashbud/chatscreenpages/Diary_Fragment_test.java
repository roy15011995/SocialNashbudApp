package com.estar.nashbud.chatscreenpages;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.comments.CommentsActivity;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Diary_Fragment_test extends Fragment {

    String getUid;
    DatabaseReference  mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<Post_Model> arrayList = new ArrayList<>();
    LinearLayout linearLayout_location_row, Linear_Comments, Linear_tagPeople;
    FirebaseUser firebaseUser, firebaseUser1;
    //SwipeRefreshLayout swipeRefreshLayout;
    SwipyRefreshLayout swipyRefreshLayout;
    private ViewPager vp_slider;
    private LinearLayout ll_dots, ll_count_number;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    private TextView[] count;
    int page_position = 0;
    String[] mStringArray;
    ArrayList<Post_Model> list = new ArrayList<>();
    String CurrentTime, CurrentDate, RandomSave, PostKey3;
    RelativeLayout Relative_Photo, Relative_Like;
    String profileName, profilePics, profileMobileNumber;
    String userId_current;
    Like like;
    private static final String TAG = "Heart";
    private boolean mProcesslike = false;
    private static final String CHAR_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 25;
    long countComments = 0;
    long countLikes = 0;
    private String mTagPeople;
    private static final int TOTAL_PAGES = 5;
    int mCurrentPage = 1;
    RecyclerView recyclerView;

    public Diary_Fragment_test() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diary__fragment_test, container, false);
        //listView = (ListView) view.findViewById(R.id.Diary_list_test);
        recyclerView = (RecyclerView)view.findViewById(R.id.diary_RecyclerView);
        getUid = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Diary").orderByChild("uid").equalTo(getUid).getRef();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post_Model,DiaryViewHolder>
                firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<Post_Model, DiaryViewHolder>
                (
                   Post_Model.class,
                   R.layout.activity_row_layout,
                   Diary_Fragment_test.DiaryViewHolder.class,
                   mDatabase
                )


        {
            @Override
            protected void populateViewHolder(DiaryViewHolder viewHolder, Post_Model model, int position) {

                final String PostKey = getRef(position).getKey();







            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{

        public DiaryViewHolder(View itemView) {
            super(itemView);
        }

        ImageView profilePic = (ImageView)itemView.findViewById(R.id.post_user_pic);
        TextView ProfileName=(TextView)itemView.findViewById(R.id.post_user_name);
        TextView Description=(TextView)itemView.findViewById(R.id.post_description);
        TextView post_data = (TextView)itemView.findViewById(R.id.post_date);
        TextView post_time =(TextView)itemView.findViewById(R.id.post_time);
        TextView UserNameLocation = (TextView)itemView.findViewById(R.id.name_location);
        TextView Place = (TextView)itemView.findViewById(R.id.post_place);

    }



}
