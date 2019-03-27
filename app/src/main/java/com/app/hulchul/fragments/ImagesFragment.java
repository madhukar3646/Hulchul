package com.app.hulchul.fragments;
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

import com.app.hulchul.R;
import com.app.hulchul.adapters.LocalImagesAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesFragment extends Fragment {

    @BindView(R.id.rv_imageslist)
    RecyclerView rv_imageslist;
    LocalImagesAdapter adapter;
    private ArrayList<String> listOfAllImages=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_images, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        adapter=new LocalImagesAdapter(getActivity(),listOfAllImages);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        rv_imageslist.setLayoutManager(gridLayoutManager);
        rv_imageslist.setAdapter(adapter);

        new LoadImages().equals("");
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

            listOfAllImages.add(PathOfImage);
        }
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
}
