package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zippnews.R;

import java.util.ArrayList;
/**
 * Created by admin on 4/20/2017.
 */

public class HomeScreenHashtagsAdapter extends RecyclerView.Adapter<HomeScreenHashtagsAdapter.MyViewHolder>
{
    private Context context;
    private OnHashtagClickListener onHashtagClickListener;
    private ArrayList<String> tagslist;

    public HomeScreenHashtagsAdapter(Context context, ArrayList<String> tagslist)
    {
      this.context=context;
      this.tagslist=tagslist;
    }

    public void setOnHashtagClickListener(OnHashtagClickListener onHashtagClickListener)
    {
        this.onHashtagClickListener=onHashtagClickListener;
    }

    @Override
    public HomeScreenHashtagsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homescreen_hashtagsmodel, parent, false);

        return new HomeScreenHashtagsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeScreenHashtagsAdapter.MyViewHolder holder, final int position)
    {
        final HomeScreenHashtagsAdapter.MyViewHolder myViewHolder=holder;
        holder.tv_hashtagname.setText(tagslist.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onHashtagClickListener!=null)
                    onHashtagClickListener.onHashtagclicked(tagslist.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return tagslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_hashtagname;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv_hashtagname=(TextView)itemView.findViewById(R.id.tv_hashtagname);
        }
    }

    public interface OnHashtagClickListener
    {
        void onHashtagclicked(String hashtag);
    }

}
