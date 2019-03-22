package com.app.hulchul.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.hulchul.R;
import com.app.hulchul.model.FiltersModel;
import com.app.hulchul.utils.FilterOverlays;
import com.app.hulchul.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */
public class VideoFilteroverlaysAdapter extends RecyclerView.Adapter<VideoFilteroverlaysAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<FiltersModel> filtersModelArrayList;
    private onFilterClickListener onFilterClickListener;

    public VideoFilteroverlaysAdapter(Context context, ArrayList<FiltersModel> filtersModelArrayList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.filtersModelArrayList=filtersModelArrayList;
    }

    public void setOnFilterClickListener(onFilterClickListener onFilterClickListener)
    {
        this.onFilterClickListener=onFilterClickListener;
    }

    @Override
    public VideoFilteroverlaysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filters_model, parent, false);

        return new VideoFilteroverlaysAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoFilteroverlaysAdapter.MyViewHolder holder, final int position)
    {
        final VideoFilteroverlaysAdapter.MyViewHolder myViewHolder=holder;
        FiltersModel model=filtersModelArrayList.get(position);
        Picasso.with(context).load(model.getThumbnailid())
                .error(R.mipmap.placeholder)
                .into(holder.iv_filterimage);

        if(model.isSelected())
            holder.layout_filtercontainer.setBackgroundResource(R.drawable.border_primary);
        else
            holder.layout_filtercontainer.setBackgroundColor(Color.TRANSPARENT);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onFilterClickListener!=null) {
                    setSelection(position);
                    Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),filtersModelArrayList.get(position).getFilter_layer());
                    bitmap= Utils.getResizedBitmap(bitmap,width,height);
                    onFilterClickListener.onFilterClick(bitmap);
                }
            }
        });
    }

    private void setSelection(int pos)
    {
        for(int i=0;i<filtersModelArrayList.size();i++)
        {
            if(i==pos)
                filtersModelArrayList.get(i).setSelected(true);
            else
                filtersModelArrayList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return filtersModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.layout_filtercontainer)
        RelativeLayout layout_filtercontainer;
        @BindView(R.id.iv_filterimage)
        ImageView iv_filterimage;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout_filtercontainer.getLayoutParams().width=(width/4);
            layout_filtercontainer.getLayoutParams().height=(width/4);
        }
    }

    public interface onFilterClickListener
    {
        void onFilterClick(Bitmap bitmap);
    }
}
