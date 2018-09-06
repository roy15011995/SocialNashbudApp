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

public class Data_sycn_Adapter extends BaseAdapter {

    String [] Title;
    Context context;
    String [] Summery;
    private static LayoutInflater inflater=null;
    public Data_sycn_Adapter(Context ctx, String[] title_text, String[] title_summery) {
        // TODO Auto-generated constructor stub
        Title=title_text;
        context=ctx;
        Summery=title_summery;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Title.length;
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
        TextView title,summery;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.data_sync_row, null);
        holder.title=(TextView) rowView.findViewById(R.id.text_title);
        holder.summery=(TextView) rowView.findViewById(R.id.text_summery);
        holder.title.setText(Title[position]);
        holder.summery.setText(Summery[position]);
        return rowView;
    }
}
