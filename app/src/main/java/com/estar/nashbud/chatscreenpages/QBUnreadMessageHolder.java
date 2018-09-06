package com.estar.nashbud.chatscreenpages;

import android.os.Bundle;

/**
 * Created by User on 21-03-2018.
 */

public class QBUnreadMessageHolder {

    private static QBUnreadMessageHolder Instance;
    private Bundle bundle;

    public static synchronized QBUnreadMessageHolder getInstance(){
        QBUnreadMessageHolder qbUnreadMessageHolder = null;
        synchronized (QBUnreadMessageHolder.class)
        {
            if(Instance==null){
                Instance=new QBUnreadMessageHolder();
                qbUnreadMessageHolder=Instance;
            }
            return qbUnreadMessageHolder;
        }

    }

    private QBUnreadMessageHolder(){
        bundle=new Bundle();
    }

    public void setBundle(Bundle bundle){
        this.bundle=bundle;
    }

    public Bundle getBundle(){
        return this.bundle;
    }

    public int getUnreadMessageByDialogId(String id){
        return this.bundle.getInt(id);
    }

}
