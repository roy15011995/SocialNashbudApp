package com.estar.nashbud.settings;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.FullScreenPictureActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

class UsersAdapterBlock extends FirebaseRecyclerAdapter<User, UsersAdapterBlock.UserViewHolder> {

    private final Context context;
    boolean click=true;
    /*int count=0;
    LayoutInflater layoutInflater;
    View view=layoutInflater.inflate(R.layout.app_bar_block,null);
    TextView text_people;*/

    UsersAdapterBlock(Context context, Query ref) {
        super(User.class, R.layout.block_contact_row, UserViewHolder.class, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, User user, int position) {
        holder.setUser(user);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_contact_row, parent, false);

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
        @BindView(R.id.block)
        TextView block;
        @BindView(R.id.unblock)
        TextView unblock;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            block.setOnClickListener(this);
            unblock.setOnClickListener(this);
            //itemUserImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.block:
                    block.setVisibility(View.GONE);
                    unblock.setVisibility(View.VISIBLE);
                    /*text_people=(TextView)view.findViewById(R.id.text_people);
                    count = Integer.parseInt((String)text_people.getText());
                    count++;
                    text_people.setText("" + count);
*/
                    /*EventBus.getDefault().post(getRef(getLayoutPosition()));*/
                    break;

                case R.id.unblock:
                    block.setVisibility(View.VISIBLE);
                    unblock.setVisibility(View.GONE);
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
                    .placeholder(R.drawable.user_block)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(itemUserImageView);
        }
    }
}
