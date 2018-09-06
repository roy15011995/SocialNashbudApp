package com.estar.nashbud.bottombarpages;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

class PeopleAdapter extends FirebaseRecyclerAdapter<User, PeopleAdapter.UserViewHolder> {

    private final Context context;

    PeopleAdapter(Context context, Query ref) {
        super(User.class, R.layout.discover_people_row, UserViewHolder.class, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, User user, int position) {
        holder.setUser(user);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_people_row, parent, false);

        return new UserViewHolder(itemView);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.people_discover_pic)
        ImageView people_discover_pic;
        @BindView(R.id.people_discover_name)
        TextView people_discover_name;
        @BindView(R.id.people_discover_mobile)
        TextView people_discover_contact;
        /*@BindView(R.id.item_user_parent)
        CardView itemUserParent;
        @BindView(R.id.linear_user_data)
        LinearLayout userData;*/
        @BindView(R.id.button_add_people)
        Button btn_add;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //userData.setOnClickListener(this);
            people_discover_pic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
               /* case R.id.linear_user_data:
                    EventBus.getDefault().post(getRef(getLayoutPosition()));
                    break;*/
                case R.id.people_discover_pic:

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

            people_discover_name.setText(user.getDisplayName());
            people_discover_contact.setText(user.getMobileNo());
            Glide.with(context)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.user_profile_pic)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(people_discover_pic);
        }
    }
}
