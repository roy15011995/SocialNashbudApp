package com.estar.nashbud.camera_package;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.estar.nashbud.R;

import java.io.File;
import java.util.ArrayList;

import xyz.neocrux.suziloader.SuziLoader;

/**
 * Created by User on 22-02-2018.
 */

public class Photo_GridAdapter1 extends BaseAdapter {

    Context context;
    int [] imageId;
    ArrayList<ImageModel> photo= new ArrayList<>();
    private static LayoutInflater inflater=null;
    private CropView myCropView;
    public Photo_GridAdapter1(Context ctx, ArrayList<ImageModel>list) {
        // TODO Auto-generated constructor stub
        context=ctx;
        photo=list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return photo.size();
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
        ImageView image;
        VideoView videoView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        convertView = inflater.inflate(R.layout.photo_grid_row1, null);
        holder.image=(ImageView)convertView.findViewById(R.id.image_show_gallery1);
        holder.videoView = (VideoView) convertView.findViewById(R.id.video_View1);
        myCropView = new CropView(context);

        Log.e("image","" + photo.get(position).getAl_imagepath());

        ImageModel imageModel = photo.get(position);

       /* File file = new File(imageModel.getImagePath().toString());
        Log.e("FilePath :",""+file);*/


        /*if(file.exists()){

            Bitmap myBitmap = decodeSampledBitmapFromUri(imageModel.getAl_imagepath().toString(),220,220);

            holder.image.setImageBitmap(myBitmap);

        }
*/

        try {
                /*for(int y=0; y < imageModel.getAl_imagepath().size(); y++){

                }*/

            int microSecond = 6000000;// 6th second as an example
            BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
            FileDescriptorBitmapDecoder decoder = new FileDescriptorBitmapDecoder(
                    new VideoBitmapDecoder(microSecond),
                    bitmapPool,
                    DecodeFormat.PREFER_ARGB_8888);

            if(imageModel.getImagePath().contains(".jpg")||imageModel.getImagePath().contains(".jpge")
                    ||imageModel.getImagePath().contains(".png")||imageModel.getImagePath().contains(".JPG")){
                Object mContext;
                Glide.with(context)
                        .load(imageModel.getImagePath())
                        .placeholder(R.drawable.gallery)
                        .dontAnimate()

                        .fitCenter()
                        .error(R.drawable.warning)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(holder.image);

                //Log.e("imageModel :","" + imageModel.getAl_imagepath().get(y));
            }
            else if(imageModel.getImagePath().contains(".gif")){
                Glide.with(context)
                        .load(imageModel.getImagePath())
                        .placeholder(R.drawable.gallery)
                        .dontAnimate()
                        .fitCenter()
                        .error(R.drawable.warning)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(holder.image);

                //Log.e("imageModel :","" + imageModel.getAl_imagepath().get(y));
            }
            else if(imageModel.getImagePath().contains(".mp4")){

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
                        .load(imageModel.getImagePath()) //Video path
                        .into(holder.image) // imageview to load the thumbnail
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

                //Log.e("imageModel :","" + imageModel.getAl_imagepath().get(y));
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


       /* if (convertView == null) {  // if it's not recycled, initialize some attributes
            holder.image.setLayoutParams(new GridView.LayoutParams(220, 220));
            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.image.setPadding(8, 8, 8, 8);
        } else {
            holder.image = (ImageView) convertView;
        }

        Bitmap bm = decodeSampledBitmapFromUri(photo.get(position), 220, 220);

        holder.image.setImageBitmap(bm);*/

        return convertView;

    }


    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }
}
