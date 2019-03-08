package com.app.hulchul.fragments;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.adapters.SimpleAdapter;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

public class Home_fragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tv_recommended)
    TextView tv_recommended;
    @BindView(R.id.tv_trending)
    TextView tv_trending;
    @BindView(R.id.player_container)
    Container container;
    private Dialog dialog;

    String urls[]={"http://testingmadesimple.org/samplevideo.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
    };
    SimpleAdapter adapter;
    LinearLayoutManager layoutManager;
    private ArrayList<VideoModel> modelArrayList=new ArrayList<>();

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_fragment, container, false);
        ButterKnife.bind(this,view);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);

        tv_recommended.setOnClickListener(this);
        tv_trending.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(getActivity());
        SnapHelper snapHelper = new PagerSnapHelper();
        container.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(container);

        adapter = new SimpleAdapter(getActivity(),modelArrayList);
        container.setAdapter(adapter);
        setDataToContainer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_recommended:
                Utils.callToast(getContext(),"Recommend");
                tv_recommended.setTypeface(Typeface.DEFAULT_BOLD);
                tv_trending.setTypeface(Typeface.DEFAULT);
                break;
            case R.id.tv_trending:
                Utils.callToast(getContext(),"trending");
                tv_recommended.setTypeface(Typeface.DEFAULT);
                tv_trending.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }
    }

    private void setDataToContainer()
    {
        modelArrayList.clear();
        for(int i=0;i<urls.length;i++)
        {
            VideoModel model=new VideoModel();
            model.setVideo_url(urls[i]);
            modelArrayList.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
