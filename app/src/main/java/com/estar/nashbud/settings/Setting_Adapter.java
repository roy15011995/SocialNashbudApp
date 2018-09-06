package com.estar.nashbud.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

/**
 * Created by User on 22-02-2018.
 */

public class Setting_Adapter extends BaseAdapter {

    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public Setting_Adapter(Context ctx, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=ctx;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return result.length;
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
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.setting_row_layout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text_setting);
        holder.img=(ImageView) rowView.findViewById(R.id.image_setting);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        return rowView;
    }
}
