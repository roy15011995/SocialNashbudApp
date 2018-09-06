package com.estar.nashbud.chatscreenpages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

import java.util.ArrayList;

/**
 * Created by User on 13-09-2017.
 */

public class ChatListAdapter extends BaseAdapter {
    Context var;
    ArrayList<UserDetails> items=new ArrayList<UserDetails>();

    public ChatListAdapter(Context var, ArrayList<UserDetails> items) {
        this.var = var;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final UserDetails pojo=items.get(position);
        final LayoutInflater inflater = (LayoutInflater)var.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.user_raw_data,null);
            holder=new ViewHolder();

            holder.profileImg = (ImageView) convertView.findViewById(R.id.item_user_image_view);
            holder.friendName = (TextView) convertView.findViewById(R.id.item_friend_name_text_view);
            holder.email = (TextView) convertView.findViewById(R.id.item_friend_email_text_view);

            convertView.setTag(holder);

        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.profileImg.setImageResource(Integer.parseInt("" + pojo.getProfileImg()));
        holder.friendName.setText(pojo.getMobileNumber());
        holder.email.setText(pojo.getEmail());

        return convertView;
    }
    public class ViewHolder
    {
        TextView friendName,email;
        ImageView profileImg;
    }


}
