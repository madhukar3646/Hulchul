package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.model.NotificationModel;
import com.app.zippnews.utils.ApiUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by admin on 4/20/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private OnNotificationClickListener onNotificationClickListener;

    public NotificationsAdapter(Context context,ArrayList<NotificationModel> notificationModelArrayList)
    {
        this.context=context;
        this.notificationModelArrayList=notificationModelArrayList;
    }

    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener)
    {
        this.onNotificationClickListener=onNotificationClickListener;
    }

    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_model, parent, false);
        return new NotificationsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationsAdapter.MyViewHolder holder, final int position)
    {
        final NotificationsAdapter.MyViewHolder myViewHolder=holder;
        NotificationModel model=notificationModelArrayList.get(position);
        String userid=model.getUserId();
       // holder.tv_notification.setText("@User"+userid.substring(userid.length()-4)+" posted a video");
         String notificationText=new String("likes a video");
         if(model.getType()!=null)
           switch (model.getType().toLowerCase()){
               case "like":
                   notificationText="likes a video";
                   break;
               case "comment":
                   notificationText="commented on  video "+ "<font color=\"#e20000\">" +model.getComment()+ "</font>";
                   break;
               case "video":
                   notificationText="posted a  video";
                   break;
           }

        holder.tv_notification.setText(Html.fromHtml("" + "<font color=\"#000000\">" + "@User"+userid+ "</font>"
                +" "+notificationText));

        Picasso.with(context).load(ApiUrls.PROFILEBASEPATH+model.getPhoto()).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(holder.profile_image);
        String gif=model.getVideo().substring(0,model.getVideo().length()-3);
        gif=gif+"jpeg";

        Picasso.with(context).load(ApiUrls.VIDEOSHAREBASEPATH+gif).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(holder.im_video_thumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
             if(onNotificationClickListener!=null)
                 onNotificationClickListener.onNotificationClick(notificationModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return notificationModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.tv_notification)
        TextView tv_notification;
        @BindView(R.id.im_video_thumb)
        ImageView im_video_thumb;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnNotificationClickListener
    {
        void onNotificationClick(NotificationModel model);
    }
}
