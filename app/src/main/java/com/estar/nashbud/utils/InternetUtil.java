package com.estar.nashbud.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 12/4/16.
 */
public class InternetUtil {
    private static NetworkInfo networkInfo;
    private static int countryCode;

    /**
     * Is there internet connection
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // test for connection for WIFI
        if (networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected()) {
            return true;
        }

        networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // test for connection for Mobile
        return networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected();

    }
}
