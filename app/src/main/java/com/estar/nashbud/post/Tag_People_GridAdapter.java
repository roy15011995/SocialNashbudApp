package com.estar.nashbud.post;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by User on 22-02-2018.
 */

public class Tag_People_GridAdapter extends BaseAdapter {
    SparseBooleanArray mSelectedItemsIds;

    Context context;
    int [] imageId;
    ArrayList<Tag_People_Model> user_list=new ArrayList<>();
    private static LayoutInflater inflater=null;
    public Tag_People_GridAdapter(Context ctx, ArrayList<Tag_People_Model>list) {
        // TODO Auto-generated constructor stub
        context=ctx;
        user_list=list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItemsIds = new SparseBooleanArray();
    }
    @Override
    public int getCount() {
        return user_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
        ImageView image_profile,image_cross;
        TextView tag_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.tag_people_item_row, null);
        holder.tag_name=(TextView) rowView.findViewById(R.id.profile_name);
        holder.image_profile=(ImageView)rowView.findViewById(R.id.image_tag_people);
        holder.image_cross=(ImageView)rowView.findViewById(R.id.profile_image_cross);
        holder.tag_name.setText(user_list.get(position).getName());
       /* holder.image_cross.setImageResource(user_list.get(position).getImage_cross());*/
        try {
            Glide.with(context)
                    .load(user_list.get(position).getProfile_pic())
                    .placeholder(R.drawable.user_profile_pic)
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.image_profile);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        return rowView;
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
