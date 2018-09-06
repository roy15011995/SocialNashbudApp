package com.estar.nashbud.thread;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.upload_photo.Message;
import com.estar.nashbud.utils.InternetUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/*
 * Created by Mahmoud on 3/13/2017.
 */

class MessagesAdapter extends FirebaseRecyclerAdapter<Message, MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_SENT_WITH_DATE = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_RECEIVED_WITH_DATE = 3;

    private final String ownerUid;
    private final Context context;
    private ArrayList<Integer> selectedPositions;
    private Boolean isConnect;
    InternetUtil internetUtil;
    DatabaseReference databaseReference;

    MessagesAdapter(Context context, String ownerUid, Query ref) {
        super(Message.class, R.layout.item_message_sent, MessageViewHolder.class, ref);
        this.context = context;
        this.ownerUid = ownerUid;
        selectedPositions = new ArrayList<>();
    }

    @Override
    protected void populateViewHolder(final MessageViewHolder holder, Message message, int position) {


        holder.setMessage(message);

        String GetKey = MessagesAdapter.this.getRef(0).getKey();
        Log.e("GetKeyMessage ",""+GetKey);


       /* isConnect = internetUtil.isConnected(context);
        if(!isConnect){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        holder.clock.setVisibility(VISIBLE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },1000);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        holder.Double_Tick_Unseen.setVisibility(VISIBLE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },1000);
        }
*/



    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message.getFrom().equals(ownerUid)) {
            if (position == getItemCount() - 1 || selectedPositions.contains(position) ||
                    getItem(position + 1).getDayTimestamp() != message.getDayTimestamp()) {

                return VIEW_TYPE_SENT_WITH_DATE;
            } else {
                return VIEW_TYPE_SENT;
            }
        } else {
            if (position == getItemCount() - 1 || selectedPositions.contains(position) ||
                    getItem(position + 1).getDayTimestamp() != message.getDayTimestamp()) {
                return VIEW_TYPE_RECEIVED_WITH_DATE;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_SENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                break;
            case VIEW_TYPE_SENT_WITH_DATE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                break;
            case VIEW_TYPE_RECEIVED:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                break;
            case VIEW_TYPE_RECEIVED_WITH_DATE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
        }
        return new MessageViewHolder(itemView);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.item_message_date_text_view)
        TextView itemMessageDateTextView;
        @BindView(R.id.item_message_body_text_view)
        TextView itemMessageBodyTextView;
        @BindView(R.id.item_message_Image)
        ImageView itemMessageImage;
        @BindView(R.id.item_message_parent)
        LinearLayout itemMessageParent;
        @Nullable
        @BindView(R.id.message_time)
        TextView Message_Time;
        @Nullable
        @BindView(R.id.linear_message_body)
        LinearLayout linear_message_body;
        @Nullable
        @BindView(R.id.single_tick)
        ImageView Single_Tick;
        @Nullable
        @BindView(R.id.double_tick_unseen)
        ImageView Double_Tick_Unseen;
        @Nullable
        @BindView(R.id.clock)
        ImageView clock;

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*itemMessageBodyTextView.setOnClickListener(this);
            itemMessageBodyTextView.setOnLongClickListener(this);*/

            itemMessageBodyTextView.setOnClickListener(this);
            itemMessageBodyTextView.setOnLongClickListener(this);
        }

        void setMessage(final Message message) {
            int viewType = MessagesAdapter.this.getItemViewType(getLayoutPosition());

            if(message.getCaptionphotoUrl()==null || message.getCaptionphotoUrl().equals(""))

            {
                itemMessageImage.setVisibility(GONE);
                itemMessageBodyTextView.setText(message.getBody());
            }
            else if(message.getBody().equals("")){
                /*itemMessageBodyTextView.setText(message.getBody());*/

                Glide.with(context)
                        .load(message.getCaptionphotoUrl())
                        .placeholder(R.drawable.gallery)
                        .centerCrop()
                        .dontAnimate()
                        .crossFade()
                        .fitCenter()
                        .into(itemMessageImage);
                itemMessageImage.setVisibility(VISIBLE);
                itemMessageBodyTextView.setVisibility(GONE);
                itemMessageImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("GetMessageImage ",""+message.getCaptionphotoUrl());
                        Log.e("GetMessageBody ",""+message.getBody());
                    }
                });
            }
            else
            {
                itemMessageBodyTextView.setText(message.getBody());

                Glide.with(context)
                        .load(message.getCaptionphotoUrl())
                        .placeholder(R.drawable.gallery)
                        .centerCrop()
                        .dontAnimate()
                        .crossFade()
                        .fitCenter()
                        .into(itemMessageImage);
                itemMessageImage.setVisibility(VISIBLE);
                itemMessageImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("GetMessageImage ",""+message.getCaptionphotoUrl());
                        Log.e("GetMessageBody ",""+message.getBody());
                        Log.e("GetDisplayName ",""+message.getDisplayName());

                        Intent intent = new Intent(context,ShowMessageImageDetails.class);
                        intent.putExtra("MessageImage",message.getCaptionphotoUrl());
                        intent.putExtra("MessageBody",message.getBody());
                        intent.putExtra("Name",message.getDisplayName());
                        context.startActivity(intent);
                    }
                });
            }


            Log.e("GetCaptionPhotoUrl",""+"\n"+message.getCaptionphotoUrl());

            Log.e("message is","" + message.getBody());
            try{
                Message_Time.setText(getTimePretty(message.getTimestamp()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            boolean shouldHideDate = viewType == VIEW_TYPE_SENT || viewType == VIEW_TYPE_RECEIVED;
            itemMessageDateTextView.setVisibility(shouldHideDate ? GONE : VISIBLE);
            /*Message_Time.setVisibility(shouldHideDate ? GONE : VISIBLE);*/
            if (!shouldHideDate) {
                itemMessageDateTextView.setText(getDatePretty(message.getTimestamp(), true));

            }
        }
        @Nullable
        @Override
        public void onClick(View v) {
            //if (selectedPositions.contains(getLayoutPosition())) {
            //    selectedPositions.remove(Integer.valueOf(getLayoutPosition()));
            //    setDateVisibility(GONE);
            //} else {
            //    selectedPositions.add(getLayoutPosition());
            //    setDateVisibility(VISIBLE);
            //}
        }

        private void setDateVisibility(int visibility) {
            TransitionManager.beginDelayedTransition(itemMessageParent);
            itemMessageDateTextView.setVisibility(visibility);
        }
        @Nullable
        @Override
        public boolean onLongClick(View v) {
            Message message = getItem(getLayoutPosition());
            if (message != null) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(
                        context.getString(R.string.clipboard_title_copied_message),
                        message.getBody());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, R.string.message_message_copied, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    private String getDatePretty(long timestamp, boolean showTimeOfDay) {
        DateTime yesterdayDT = new DateTime(DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
        yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
        Interval today = new Interval(DateTime.now().withTimeAtStartOfDay(), Days.ONE);
        Interval yesterday = new Interval(yesterdayDT, Days.ONE);
        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.mediumDate();
        if (today.contains(timestamp)) {
            /*if (showTimeOfDay) {
                return timeFormatter.print(timestamp);

            } else {
                return context.getString(R.string.today);
            }*/
            return context.getString(R.string.today);

        } else if (yesterday.contains(timestamp)) {
            return context.getString(R.string.yesterday);
        } else {
            return dateFormatter.print(timestamp);
        }
    }

    private String getTimePretty(long timestamp) {
        DateTime yesterdayDT = new DateTime(DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
        yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
        Interval today = new Interval(DateTime.now().withTimeAtStartOfDay(), Days.ONE);
        Interval yesterday = new Interval(yesterdayDT, Days.ONE);
        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
        if (today.contains(timestamp)) {
            return timeFormatter.print(timestamp);
        }
        else if(yesterday.contains(timestamp)){
            return timeFormatter.print(timestamp);
        }
        else {
            return timeFormatter.print(timestamp);
        }


    }
}
