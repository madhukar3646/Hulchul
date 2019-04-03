package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by admin on 4/20/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>
{
    private Context context;
    public NotificationsAdapter(Context context)
    {
        this.context=context;
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
    }

    @Override
    public int getItemCount()
    {
        return 30;
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
