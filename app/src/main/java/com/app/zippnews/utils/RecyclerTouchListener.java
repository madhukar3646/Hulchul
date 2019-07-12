package com.app.zippnews.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.app.zippnews.adapters.SimplePlayerViewHolder;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private RecyclerviewTapListeners clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final RecyclerviewTapListeners clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                SimplePlayerViewHolder holder= (SimplePlayerViewHolder) recycleView.getChildViewHolder(child);
                if(child!=null && clicklistener!=null){
                    clicklistener.onDoubletap(holder,recycleView.getChildAdapterPosition(child));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                SimplePlayerViewHolder holder= (SimplePlayerViewHolder) recycleView.getChildViewHolder(child);
                if(child!=null && clicklistener!=null){
                    clicklistener.onLongClick(holder,recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        try {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            SimplePlayerViewHolder holder= (SimplePlayerViewHolder) rv.getChildViewHolder(child);
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(holder,rv.getChildAdapterPosition(child));
            }
        }
        catch (Exception ex)
        {

        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}