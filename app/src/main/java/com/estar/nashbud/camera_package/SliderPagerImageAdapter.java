package com.estar.nashbud.camera_package;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;

import java.util.ArrayList;

public class SliderPagerImageAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<ImageModel> image_arraylist= new ArrayList<>();
    String[]mArrayString;

    public SliderPagerImageAdapter(Activity activity, ArrayList<ImageModel> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        notifyDataSetChanged();   //add here
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        ImageModel imageModel=image_arraylist.get(position);

        /*mArrayString = new String[post_model.getAl_imagepath().size()];
        mArrayString = post_model.getAl_imagepath().toArray(mArrayString);
*/
        //holder.image_cross.setImageResource(user_list.get(position).getImage_cross());

        try {
           /* for(int y=0; y < post_model.getAl_imagepath().size(); y++){

            }*/

            int microSecond = 6000000;// 6th second as an example
            BitmapPool bitmapPool = Glide.get(activity).getBitmapPool();
            FileDescriptorBitmapDecoder decoder = new FileDescriptorBitmapDecoder(
                    new VideoBitmapDecoder(microSecond),
                    bitmapPool,
                    DecodeFormat.PREFER_ARGB_8888);


            if(imageModel.getImagePath().contains(".jpg")||imageModel.getImagePath().contains(".jpge")
                    ||imageModel.getImagePath().contains(".png")||imageModel.getImagePath().contains(".JPG")){
                Glide.with(activity)
                        .load(imageModel.getImagePath())
                        .dontAnimate()
                        .centerCrop()
                        .crossFade()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(im_slider);

                //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
            }

            /*else if(post_model.getImagePath().contains(".gif")){
                Glide.with(activity)
                        .load(post_model.getImagePath())
                        .placeholder(R.drawable.gallery)
                        .dontAnimate()
                        .fitCenter()
                        .error(R.drawable.warning)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(im_slider);

                //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
            }
            else if(post_model.getImagePath().contains(".mp4")){

                        *//*Glide.with(context)
                                .load(imageModel.getAl_imagepath().get(y))
                                .asBitmap()
                                .videoDecoder(decoder)
                                .override(50,50)
                                .placeholder(R.drawable.gallery)
                                .error(R.drawable.warning)
                                .into(holder.image);*//*

                SuziLoader loader = new SuziLoader(); //Create it for once
                loader.with(activity) //Context
                        .load(post_model.getImagePath()) //Video path
                        .into(im_slider) // imageview to load the thumbnail
                        .type("mini") // mini or micro
                        .show(); // to show the thumbnail


                       *//* MediaController mc = new MediaController(context);
                        Uri uri = Uri.parse(imageModel.getAl_imagepath().get(y));
                        holder.videoView.setVideoURI(uri);
                        mc.setAnchorView(holder.videoView);
                        holder.videoView.setMediaController(mc);
                        holder.videoView.setVisibility(View.VISIBLE);
                        holder.image.setVisibility(View.GONE);

                     if(imageModel.getAl_imagepath().get(y)!=null){
                         holder.videoView.start();
                     }*//*

                //Log.e("imageModel :","" + post_model.getAl_imagepath().get(y));
            }*/



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



        ((ViewPager)container).addView(view);

        return view;
    }

    @Override
    public int getCount() {
        Log.e("GetImageCount",""+image_arraylist.size());
        return image_arraylist.size();

    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        ((ViewPager)container).removeView(view);
    }
}
