package com.estar.nashbud.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.ImageModel;

import java.util.ArrayList;

import xyz.neocrux.suziloader.SuziLoader;

/**
 * Created by User on 22-02-2018.
 */

public class Post_Adapter extends BaseAdapter {

    Context context;
    int [] imageId;
    ArrayList<Post_Model> user_list=new ArrayList<>();
    LinearLayout grid;
    LinearLayout photo;
    private static LayoutInflater inflater=null;
    public Post_Adapter(Context ctx, ArrayList<Post_Model> list, LinearLayout linear_photo_grid, LinearLayout linear_photo) {
        // TODO Auto-generated constructor stub
        context=ctx;
        user_list=list;
        grid = linear_photo_grid;
        photo = linear_photo;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ImageView img,image_cross;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Log.e("image_Post","" + user_list.get(position).getAl_imagepath());

        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.photo_grid_row_layout, null);
        holder.img=(ImageView) rowView.findViewById(R.id.image_post_grid);
        holder.image_cross=(ImageView)rowView.findViewById(R.id.image_post_grid_cross);
        //holder.img.setImageResource(user_list.get(position).getUserProfileImage());
        holder.img.setRotation(90);
        Post_Model post_model=user_list.get(position);
        //holder.image_cross.setImageResource(user_list.get(position).getImage_cross());

        try {
           /* for(int y=0; y < post_model.getAl_imagepath().size(); y++){

            }*/

                int microSecond = 6000000;// 6th second as an example
                BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
                FileDescriptorBitmapDecoder decoder = new FileDescriptorBitmapDecoder(
                        new VideoBitmapDecoder(microSecond),
                        bitmapPool,
                        DecodeFormat.PREFER_ARGB_8888);

                if(post_model.getImagePath().contains(".jpg")||post_model.getImagePath().contains(".jpge")
                        ||post_model.getImagePath().contains(".png")||post_model.getImagePath().contains(".JPG")){
                    Glide.with(context)
                            .load(post_model.getImagePath())
                            .placeholder(R.drawable.gallery)
                            .dontAnimate()
                            .fitCenter()
                            .error(R.drawable.warning)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(holder.img);

                    //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                }
                else if(post_model.getImagePath().contains(".gif")){
                    Glide.with(context)
                            .load(post_model.getImagePath())
                            .placeholder(R.drawable.gallery)
                            .dontAnimate()
                            .fitCenter()
                            .error(R.drawable.warning)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(holder.img);

                    //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                }
                else if(post_model.getImagePath().contains(".mp4")){

                        /*Glide.with(context)
                                .load(imageModel.getAl_imagepath().get(y))
                                .asBitmap()
                                .videoDecoder(decoder)
                                .override(50,50)
                                .placeholder(R.drawable.gallery)
                                .error(R.drawable.warning)
                                .into(holder.image);*/

                    SuziLoader loader = new SuziLoader(); //Create it for once
                    loader.with(context) //Context
                            .load(post_model.getImagePath()) //Video path
                            .into(holder.img) // imageview to load the thumbnail
                            .type("mini") // mini or micro
                            .show(); // to show the thumbnail


                       /* MediaController mc = new MediaController(context);
                        Uri uri = Uri.parse(imageModel.getAl_imagepath().get(y));
                        holder.videoView.setVideoURI(uri);
                        mc.setAnchorView(holder.videoView);
                        holder.videoView.setMediaController(mc);
                        holder.videoView.setVisibility(View.VISIBLE);
                        holder.image.setVisibility(View.GONE);

                     if(imageModel.getAl_imagepath().get(y)!=null){
                         holder.videoView.start();
                     }*/

                    //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
                }



                    /*Picasso.with(context)
                            .load(imageModel.getAl_imagepath().get(y))
                            .placeholder(R.drawable.gallery)   // optional
                            .centerCrop()
                            .into(holder.image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.e("Load Image","Loading Image Successfully..");
                                }

                                @Override
                                public void onError() {
                                 holder.image.setImageResource(R.drawable.warning);
                                    Log.e("Load Image","Loading Image Failed");
                                }
                            });*/

        }
        catch (Exception e){
            e.printStackTrace();
        }




        return rowView;
    }
}
