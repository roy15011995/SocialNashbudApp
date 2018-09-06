package com.estar.nashbud.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

import java.util.ArrayList;

/**
 * Created by User on 22-02-2018.
 */

public class Tag_Adapter extends BaseAdapter {

    Context context;
    int [] imageId;
    ArrayList<Tag_Model> user_list=new ArrayList<>();
    private Tag_Model[] lv_arr = {};
    String[]name;

    public Tag_Adapter(Context ctx, ArrayList<Tag_Model>list) {
        context=ctx;
        user_list=list;
    }
    @Override
    public int getCount() {
        return user_list.size();
    }

    @Override
    public Object getItem(int position) {
        return user_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder
    {
        ImageView image_cross;
        TextView tag_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         holder=new ViewHolder();
        //View rowView;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.tag_grid_row_layout, null);
            holder.tag_name = (TextView) convertView.findViewById(R.id.name_tag);
            holder.image_cross = (ImageView) convertView.findViewById(R.id.image_post_grid_cross);

            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        Tag_Model tag_model = user_list.get(position);

        holder.tag_name.setText(tag_model.getName());

        /*lv_arr = user_list.toArray(new Tag_Model[user_list.size()]);

        Log.e("ListStringToArray",""+lv_arr);

        for (int i = 0; i < tag_model.getName().size(); i++) {

            holder.tag_name.setText(tag_model.getName().get(i));
            //Log.e("name getting",""+tag_model.getName().get(0));
            Log.e("name getting", "" + tag_model.getName().get(0));
        }*/



      // holder.image_cross.setImageResource(user_list.get(position).getImage_cross());
        return convertView;
    }
}
