package com.app.zippnews.fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.zippnews.R;
import com.app.zippnews.adapters.LocalImagesAdapter;
import com.app.zippnews.model.Local_imagesmodel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesFragment extends Fragment implements LocalImagesAdapter.onImagesSelectionListener{

    @BindView(R.id.rv_imageslist)
    RecyclerView rv_imageslist;
    LocalImagesAdapter adapter;
    private ArrayList<Local_imagesmodel> listOfAllImages=new ArrayList<>();
    private ArrayList<String> selected_imageslist=new ArrayList<>();
    private onSelectionImagesListener onSelectionImagesListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    public void setonSelectionImagesListener(onSelectionImagesListener onSelectionImagesListener)
    {
        this.onSelectionImagesListener=onSelectionImagesListener;
    }

    private void init(View view)
    {
        adapter=new LocalImagesAdapter(getActivity(),listOfAllImages);
        adapter.setOnImagesSelectionListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        rv_imageslist.setLayoutManager(gridLayoutManager);
        rv_imageslist.setAdapter(adapter);

        new LoadImages().execute("");
    }

    public void getImagesPath(Activity activity) {
        Uri uri;
        listOfAllImages.clear();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            Local_imagesmodel local_imagesmodel=new Local_imagesmodel();
            local_imagesmodel.setPath(PathOfImage);
            listOfAllImages.add(local_imagesmodel);
        }
    }

    @Override
    public void onImageSelected(String path) {
       if(selected_imageslist.contains(path))
           selected_imageslist.remove(path);
       else
           selected_imageslist.add(path);

       if(onSelectionImagesListener!=null)
           onSelectionImagesListener.onImageSelected(selected_imageslist);
    }

    private class LoadImages extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "Loading please wait...",null);
        }

        @Override
        protected String doInBackground(String... strings) {
            getImagesPath(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public interface onSelectionImagesListener
    {
        void onImageSelected(ArrayList<String> selected_imageslist);
    }
}
