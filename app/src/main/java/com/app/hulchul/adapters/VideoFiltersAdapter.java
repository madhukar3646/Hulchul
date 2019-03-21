package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hulchul.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */
public class VideoFiltersAdapter extends RecyclerView.Adapter<VideoFiltersAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private CharSequence[] charList;
    private onFilterClickListener onFilterClickListener;

    public VideoFiltersAdapter(Context context,CharSequence[] charList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.charList=charList;
    }

    public void setOnFilterClickListener(onFilterClickListener onFilterClickListener)
    {
        this.onFilterClickListener=onFilterClickListener;
    }

    @Override
    public VideoFiltersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filters_model, parent, false);

        return new VideoFiltersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoFiltersAdapter.MyViewHolder holder, final int position)
    {
        final VideoFiltersAdapter.MyViewHolder myViewHolder=holder;
        holder.tv_filtername.setText(charList[position].toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onFilterClickListener!=null)
                    onFilterClickListener.onFilterClick(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return charList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_filtername)
        TextView tv_filtername;
        @BindView(R.id.layout_filtercontainer)
        RelativeLayout layout_filtercontainer;
        @BindView(R.id.iv_filterimage)
        ImageView iv_filterimage;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout_filtercontainer.getLayoutParams().width=(width/4);
            layout_filtercontainer.getLayoutParams().height=(width/3);
        }
    }

    public interface onFilterClickListener
    {
        void onFilterClick(int pos);
    }
}
