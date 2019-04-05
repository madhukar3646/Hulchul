package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.model.NotificationModel;

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

    public NotificationsAdapter(Context context,ArrayList<NotificationModel> notificationModelArrayList)
    {
        this.context=context;
        this.notificationModelArrayList=notificationModelArrayList;
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
        holder.tv_notification.setText("@User"+userid.substring(userid.length()-4)+" posted a video");
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

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
