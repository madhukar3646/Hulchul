package com.app.hulchul.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.hulchul.R;
import com.app.hulchul.adapters.LocalVideothumbnailsAdapter;
import com.app.hulchul.model.LocalVideo_Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LocalVideos_fragment extends Fragment {

    @BindView(R.id.rv_videoslist)
    RecyclerView rv_videoslist;
    LocalVideothumbnailsAdapter adapter;
    private ArrayList<LocalVideo_Model> videoModelArrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_local_videos_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        adapter=new LocalVideothumbnailsAdapter(getActivity(),videoModelArrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
       rv_videoslist.setLayoutManager(gridLayoutManager);
       rv_videoslist.setAdapter(adapter);

        new LoadVideos().execute("");
    }

    private class LoadVideos extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "Loading please wait...",null);
        }

        @Override
        protected String doInBackground(String... strings) {

            getAllMedia();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public void getAllMedia() {
        videoModelArrayList.clear();
        HashSet<String> videoItemHashSet = new HashSet<>();
        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            do{
                String path=(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                boolean add=videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
                if(add)
                {
                    retriever.setDataSource(getActivity(), Uri.fromFile(new File(path)));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    Bitmap thumb=retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    LocalVideo_Model video_model = new LocalVideo_Model();
                    video_model.setVideoPath(path);
                    video_model.setBitmap(thumb);
                    video_model.setDuration(""+getDurationFormat(timeInMillisec));
                    videoModelArrayList.add(video_model);
                }

            }while(cursor.moveToNext());
            retriever.release();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDurationFormat(long millis)
    {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }
}
