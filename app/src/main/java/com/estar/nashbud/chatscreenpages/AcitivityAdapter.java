package com.estar.nashbud.chatscreenpages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by User on 24-03-2018.
 */

public class AcitivityAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    ArrayList<Post_Model>arrayList=new ArrayList<>();
    LayoutInflater layoutInflater;
    FirebaseUser firebaseUser;
    String Current_User;
    Post_Model post_model;
    PopupMenu popup;

    public AcitivityAdapter(Context context, ArrayList<Post_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Current_User = firebaseUser.getUid();
        post_model = new Post_Model();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(getCount() - position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.activity_row_layout,null);
        ViewHolder viewHolder=new ViewHolder();
        viewHolder.TagWith=(TextView)convertView.findViewById(R.id.tag_with);
        viewHolder.NoUsers=(TextView)convertView.findViewById(R.id.No_users);
        viewHolder.Others=(TextView)convertView.findViewById(R.id.others);
        viewHolder.post_description = (TextView)convertView.findViewById(R.id.post_description);
        viewHolder.post_user = (TextView)convertView.findViewById(R.id.post_user_name);
        //viewHolder.post_Image = (ImageView)convertView.findViewById(R.id.post_pic);
        viewHolder.post_user_pic = (ImageView)convertView.findViewById(R.id.post_user_pic);
        viewHolder.option_menu_dots = (ImageView)convertView.findViewById(R.id.dots);

        Post_Model post_model=arrayList.get(position);

        if(post_model.getPics().contains(".jpg")|| post_model.getPics().contains(".jpge")||post_model.getPics().contains(".png")
                ||post_model.getPics().contains(".JPG")||post_model.getPics().contains(".gif")||post_model.getPics().contains(".mp4")){

            Glide.with(context)
                    .load(post_model.getPics())
                    .placeholder(R.drawable.gallery)
                    .dontAnimate()
                    .centerCrop()
                    .fitCenter()
                    .error(R.drawable.warning)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(viewHolder.post_Image);
        }
        else if(post_model.getPics().contains(".jpg")|| post_model.getPics().contains(".jpge")||post_model.getPics().contains(".png")
                ||post_model.getPics().contains(".JPG")||post_model.getPics().contains(".gif")||post_model.getPics().contains(".mp4")){

            Glide.with(context)
                    .load(post_model.getProfilePic())
                    .placeholder(R.drawable.gallery)
                    .dontAnimate()
                    .centerCrop()
                    .fitCenter()
                    .error(R.drawable.warning)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(viewHolder.post_user_pic);
        }


        viewHolder.post_user.setText(post_model.getPostFullName());
        viewHolder.post_description.setText(post_model.getPostMessage());




        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dots :

               /* if(Current_User.contains(post_model.getUid())){
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.my_popup_menu, popup.getMenu());
                    popup.show();
                }*/
        }


    }

    public class ViewHolder{
    TextView TagWith,NoUsers,Others,post_description,post_user;
    ImageView post_Image,post_user_pic,option_menu_dots;

    }
}
