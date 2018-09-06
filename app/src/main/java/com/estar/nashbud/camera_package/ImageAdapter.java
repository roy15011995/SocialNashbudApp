package com.estar.nashbud.camera_package;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<String> images;
    private Activity context;

    public ImageAdapter(Activity localContext) {

        context = localContext;
        images = getAllShownImagesPath(context);
    }

    @Override
    public int getCount() {
        return images.size();
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
        ViewHolder viewHolder=new ViewHolder();
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.galchild,null);
            viewHolder.Photo=(ImageView)convertView.findViewById(R.id.ImageView01);
            //viewHolder.Photo.setLayoutParams(new GridView.LayoutParams(270,270));
            viewHolder.Photo.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(context)
                    .load(images.get(position))
                    .placeholder(R.drawable.gallery)
                    .centerCrop()
                    .crossFade()
                    .into(viewHolder.Photo);
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView Photo;
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri,uri1;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        uri1 = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
