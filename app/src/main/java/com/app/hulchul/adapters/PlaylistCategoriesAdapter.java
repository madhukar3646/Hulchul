package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;

/**
 * Created by admin on 4/20/2017.
 */

public class PlaylistCategoriesAdapter extends RecyclerView.Adapter<PlaylistCategoriesAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;

    public PlaylistCategoriesAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @Override
    public PlaylistCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlistcategoriesmodel, parent, false);

        return new PlaylistCategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistCategoriesAdapter.MyViewHolder holder, final int position)
    {
        final PlaylistCategoriesAdapter.MyViewHolder myViewHolder=holder;
    }

    @Override
    public int getItemCount()
    {
        return 9;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_category;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            tv_category=(TextView) itemView.findViewById(R.id.tv_category);
        }
    }
}
