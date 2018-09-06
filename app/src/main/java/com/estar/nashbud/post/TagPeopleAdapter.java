package com.estar.nashbud.post;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.FullScreenPictureActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class TagPeopleAdapter extends FirebaseRecyclerAdapter<User, TagPeopleAdapter.UserViewHolder> {

    private final Context context;
    boolean click=true;
    ImageView itemUserImageView;
    ImageView Tick_null;
    ImageView Tick_Tick;
    private SparseBooleanArray mSelectedItemsIds;
    /*int count=0;
    LayoutInflater layoutInflater;
    View view=layoutInflater.inflate(R.layout.app_bar_block,null);
    TextView text_people;*/
    GridView gridView;
    ArrayList<Tag_People_Model>list=new ArrayList<>();

    public TagPeopleAdapter(Context context, Query ref) {
        super(User.class, R.layout.tag_people_row, UserViewHolder.class, ref);
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, User user, int position) {
        holder.setUser(user);
        holder.Tick_Tick.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.Tick_null.setVisibility(isSelected(position) ? View.INVISIBLE : View.VISIBLE);

        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();

        String current_user_Id=id.getUid();

        Log.e("Current_User_Id",""+current_user_Id);

       /* if (current_user_Id.equals(user.getUid())){
            holder.user_linear_layout.setVisibility(View.GONE);
            holder.itme_view.setVisibility(View.GONE);
        }*/
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_people_row, parent, false);

        return new UserViewHolder(itemView);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_block)
        ImageView itemUserImageView;
        @BindView(R.id.block_name)
        TextView itemFriendNameTextView;
        @BindView(R.id.block_contact)
        TextView itemFriendContactTextView;
       /* @BindView(R.id.block_contact)
        CardView itemUserParent;*/
        /*@BindView(R.id.linear_user_data)
        LinearLayout userData;*/
        @BindView(R.id.tick_null)
        ImageView Tick_null;
        @BindView(R.id.tick_tick)
        ImageView Tick_Tick;
        @BindView(R.id.linear_tag_people_row)
        LinearLayout LinearTagPeopleRow;
        @BindView(R.id.user_linear_layout)
        LinearLayout user_linear_layout;
        @BindView(R.id.item_view)
        View itme_view;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*Tick_null=(ImageView)itemView.findViewById(R.id.tick_null);
            Tick_Tick=(ImageView)itemView.findViewById(R.id.tick_tick);*/

          /*  Tick_null.setOnClickListener(this);
            Tick_Tick.setOnClickListener(this);*/


            //itemUserImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tick_null:
                    Tick_null.setVisibility(View.GONE);
                    Tick_Tick.setVisibility(View.VISIBLE);



                    /*Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
                    soundUri.buildUpon();*/
                    /*text_people=(TextView)view.findViewById(R.id.text_people);
                    count = Integer.parseInt((String)text_people.getText());
                    count++;
                    text_people.setText("" + count);
*/
                    /*EventBus.getDefault().post(getRef(getLayoutPosition()));*/

                    break;

                case R.id.tick_tick:
                    Tick_null.setVisibility(View.VISIBLE);
                    Tick_Tick.setVisibility(View.GONE);
                    /*try{
                        list.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("Exception",e.toString());
                    }*/

                    break;
                case R.id.image_block:

                    SharedPreference sharedPreference = new SharedPreference();

                    //Intent i = new Intent(context,FullScreenPictureActivity.class);

                    String dataSplit = String.valueOf(getRef(getLayoutPosition()));

                    Log.e("after","" + dataSplit.replaceAll(".*/", ""));

                    //i.putExtra("user_id",dataSplit.replaceAll(".*/", ""));
                    //context.startActivity(i);

                    sharedPreference.saveFullPicData(context,dataSplit.replaceAll(".*/", ""));

                    //InputNameDialogFragment tFragment = new InputNameDialogFragment();


                    FragmentActivity activity = (FragmentActivity)(context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    FullScreenPictureActivity tFragment = new FullScreenPictureActivity();
                    tFragment.show(fm, "fragment_alert");
                    break;
            }
        }

        void setUser(User user) {

            itemFriendNameTextView.setText(user.getDisplayName());
            itemFriendContactTextView.setText(user.getMobileNo());
            Glide.with(context)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(itemUserImageView);
        }
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

          /*  mSelectedItemsIds.get(mSelectedItemsIds.keyAt(position));*/
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItemsIds.size());
        for (int i = 0; i < mSelectedItemsIds.size(); ++i) {
            items.add(mSelectedItemsIds.keyAt(i));
        }
        return items;
    }
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
