package com.app.hulchul.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.model.EffectsModel;
import com.app.hulchul.utils.ApiUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */
public class VideoEffectsAdapter extends RecyclerView.Adapter<VideoEffectsAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<EffectsModel> effectsModelArrayList;
    private onEffectClickListener onEffectClickListener;
    private int effectThumbnails[]={R.drawable.normal,R.drawable.bilateral,R.drawable.box_blur,R.drawable.bulge_distortion,
                                    R.drawable.cga_color_space,R.drawable.gaussian_blur,R.drawable.glay_scale,R.drawable.invert,
                                    R.drawable.lookup_table,R.drawable.monochrome,R.drawable.overlay,R.drawable.sepia,R.drawable.sharpen,
                                    R.drawable.sphere_refraction,R.drawable.tone_curve,R.drawable.tone,R.drawable.vignette,R.drawable.weak_pixelin_clusion,R.drawable.filter_group};

    public VideoEffectsAdapter(Context context,ArrayList<EffectsModel> effectsModelArrayList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.effectsModelArrayList=effectsModelArrayList;
    }

    public void setOnEffectClickListener(onEffectClickListener onEffectClickListener)
    {
        this.onEffectClickListener=onEffectClickListener;
    }

    @Override
    public VideoEffectsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.effects_model, parent, false);

        return new VideoEffectsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoEffectsAdapter.MyViewHolder holder, final int position)
    {
        final VideoEffectsAdapter.MyViewHolder myViewHolder=holder;
        EffectsModel model=effectsModelArrayList.get(position);
        Picasso.with(context).load(effectThumbnails[position]).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(holder.iv_filterimage);

        holder.tv_filtername.setText(model.getFiltername().toString());
        if(model.isSelected())
            holder.layout_filtercontainer.setBackgroundResource(R.drawable.border_primary);
        else
            holder.layout_filtercontainer.setBackgroundColor(Color.TRANSPARENT);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onEffectClickListener!=null) {
                    setSelection(position);
                    onEffectClickListener.onEffectClick(position);
                }
            }
        });
    }

    private void setSelection(int pos)
    {
        for(int i=0;i<effectsModelArrayList.size();i++)
        {
            if(i==pos)
                effectsModelArrayList.get(i).setSelected(true);
            else
                effectsModelArrayList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return effectsModelArrayList.size();
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
            layout_filtercontainer.getLayoutParams().height=(width/4);
        }
    }

    public interface onEffectClickListener
    {
        void onEffectClick(int pos);
    }
}
