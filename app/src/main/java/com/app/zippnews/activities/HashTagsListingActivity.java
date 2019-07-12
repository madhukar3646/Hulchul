package com.app.zippnews.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.adapters.HashtagSearchAdapter;
import com.app.zippnews.model.HashtagSearchResponse;
import com.app.zippnews.model.Hashtagsearchdata;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashTagsListingActivity extends AppCompatActivity implements HashtagSearchAdapter.OnHashtagClickListener{

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_hashtags)
    RecyclerView rv_hashtags;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.et_search)
    EditText et_search;
    private HashtagSearchAdapter hashtagSearchAdapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<Hashtagsearchdata> hashtagsearchdataList=new ArrayList<>();
    private ArrayList<Hashtagsearchdata> hashtagsearchdataListTemp=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tags_listing);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
     connectionDetector=new ConnectionDetector(HashTagsListingActivity.this);
     hashtagSearchAdapter=new HashtagSearchAdapter(HashTagsListingActivity.this,hashtagsearchdataList);
     hashtagSearchAdapter.setOnHashtagClickListener(this);
     rv_hashtags.setLayoutManager(new LinearLayoutManager(HashTagsListingActivity.this,LinearLayoutManager.VERTICAL,false));
     rv_hashtags.setAdapter(hashtagSearchAdapter);

      if(connectionDetector.isConnectingToInternet())
          getListofHashtags();
      else
          Utils.callToast(HashTagsListingActivity.this,getResources().getString(R.string.internet_toast));

      et_search.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              Log.e("search string",editable.toString());
              filter(editable.toString());
          }
      });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void filter(String text){
        ArrayList<Hashtagsearchdata> temp = new ArrayList();
        for(Hashtagsearchdata d: hashtagsearchdataListTemp){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getHashTag().contains(text)){
                temp.add(d);
            }
        }
        if(text.trim().length()==0)
            hashtagSearchAdapter.updateList(hashtagsearchdataListTemp);
        else
          hashtagSearchAdapter.updateList(temp);
    }

    public void getListofHashtags(){
        hashtagsearchdataList.clear();
        hashtagsearchdataListTemp.clear();
        Utils.showDialog(HashTagsListingActivity.this);
        Call<HashtagSearchResponse> call= RetrofitApis.Factory.createTemp(HashTagsListingActivity.this).hashTagList("0");
        call.enqueue(new Callback<HashtagSearchResponse>() {

            @Override
            public void onResponse(Call<HashtagSearchResponse> call, Response<HashtagSearchResponse> response) {
                Utils.dismissDialog();
                HashtagSearchResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==1) {
                        if(body.getData()!=null)
                            hashtagsearchdataList.addAll(body.getData());
                            hashtagsearchdataListTemp.addAll(body.getData());
                    } else {
                        //Utils.callToast(getActivity(), body.getMessage());
                        tv_nodata.setText(body.getMessage());
                    }
                }
                else {
                    Utils.callToast(HashTagsListingActivity.this, "Null data came");
                }
                if(hashtagsearchdataList.size()>0)
                    tv_nodata.setVisibility(View.GONE);
                else
                    tv_nodata.setVisibility(View.VISIBLE);
                hashtagSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HashtagSearchResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("searchuser onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onHashtagClick(Hashtagsearchdata hashtagsearchdata) {
        Intent intent=new Intent();
        intent.putExtra("hashtag",hashtagsearchdata.getHashTag());
        intent.putExtra("hashtagid",hashtagsearchdata.getId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
