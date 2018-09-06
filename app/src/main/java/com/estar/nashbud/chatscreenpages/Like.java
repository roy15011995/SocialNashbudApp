package com.estar.nashbud.chatscreenpages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Like {
    private static final String TAG = "Heart";
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    ImageView HeartWhite,HeartSky;

    public Like(ImageView heartWhite, ImageView heartSky) {
        HeartWhite = heartWhite;
        HeartSky = heartSky;
    }

    public void ToggleLike()
    {
        Log.e(TAG,"ToggleLike : toggling heart");

        AnimatorSet animatorSet = new AnimatorSet();

        if(HeartSky.getVisibility() == View.VISIBLE)
        {
            Log.e(TAG,"ToggleLike : toggling red heart off");
            HeartSky.setScaleX(0.1f);
            HeartSky.setScaleX(0.1f);
            ObjectAnimator ScaleDownY = ObjectAnimator.ofFloat(HeartSky,"scaleY",1f,0f);
            ScaleDownY.setDuration(300);
            ScaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator ScaleDownX = ObjectAnimator.ofFloat(HeartSky,"scaleX",1f,0f);
            ScaleDownX.setDuration(300);
            ScaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);
            HeartSky.setVisibility(View.GONE);
            HeartWhite.setVisibility(View.VISIBLE);

            animatorSet.playTogether(ScaleDownX,ScaleDownY);
        }
       else if(HeartSky.getVisibility() == View.GONE)
        {
            Log.e(TAG,"ToggleLike : toggling red heart on");
            HeartSky.setScaleX(0.1f);
            HeartSky.setScaleX(0.1f);
            ObjectAnimator ScaleDownY = ObjectAnimator.ofFloat(HeartSky,"scaleY",0.1f,1f);
            ScaleDownY.setDuration(300);
            ScaleDownY.setInterpolator(DECELERATE_INTERPOLATOR);

            ObjectAnimator ScaleDownX = ObjectAnimator.ofFloat(HeartSky,"scaleX",0.1f,1f);
            ScaleDownX.setDuration(300);
            ScaleDownX.setInterpolator(DECELERATE_INTERPOLATOR);
            HeartSky.setVisibility(View.VISIBLE);
            HeartWhite.setVisibility(View.GONE);

            animatorSet.playTogether(ScaleDownX,ScaleDownY);
        }

        animatorSet.start();
    }
}
