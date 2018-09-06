package com.estar.nashbud.bottombarpages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estar.nashbud.R;

import java.util.ArrayList;

public class eventAdapter extends BaseAdapter {
    String [] result;
    Context context;
    LayoutInflater inflater;

    public eventAdapter(String[] result, Context context) {
        this.result = result;
        this.context = context;
        inflater = (LayoutInflater)context.
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.notification_row_layout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.notification_name);
        return rowView;
    }

    public class Holder{
        TextView tv;
    }
}
