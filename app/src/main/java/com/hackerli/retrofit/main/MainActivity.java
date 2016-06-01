package com.hackerli.retrofit.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;
import com.google.gson.Gson;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.SearchResult;
import com.hackerli.retrofit.module.sendphoto.SendPhotoActivity;
import com.hackerli.retrofit.module.seachgank.MaterialSearchView;
import com.hackerli.retrofit.module.showgank.GankFragment;
import com.hackerli.retrofit.module.showgirl.GirlFragment;
import com.hackerli.retrofit.module.showvideo.VideoFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{

    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.vp)
    ViewPager mVP;
    @Bind(R.id.fla_upload)
    FloatingActionButton mFab;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.search_view)
    MaterialSearchView mSearchView;
    @Bind(R.id.bottom_sheet)
    SheetLayout mSheetLayout;

    List<Fragment> mFragments = new ArrayList<>();

    private static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("gank.io");
        setTabs();

        initSearchView();
        initFabEvent();

    }

    private void initFabEvent() {
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });

    }

    private void setTabs() {

        mFragments.add(new GirlFragment());
        mFragments.add(new GankFragment());
        mFragments.add(new VideoFragment());

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), mFragments);
        mVP.setAdapter(adapter);
        mTab.setupWithViewPager(mVP);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(menuItem);
        return true;
    }

    private void initSearchView() {
        mSearchView.setHintTextColor(Color.GRAY);
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mFab.hide();
            }

            @Override
            public void onSearchViewClosed() {
                mFab.show();
                mSearchView.clearData();
            }
        });

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // search data by keyword
                if (!query.isEmpty()) {
                    searchData(query);
                }else{
                    mSearchView.clearData();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    private void searchData(final String newText) {
        Observable observable = Observable.create(new Observable.OnSubscribe<List<SearchResult>>() {
            @Override
            public void call(Subscriber<? super List<SearchResult>> subscriber) {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().add("keyword", newText).build();
                Request request = new Request.Builder()
                        .url("http://gankio.herokuapp.com/search")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    Gson gson = new Gson();
                    SearchResult[] searchResultData = gson.fromJson(json,SearchResult[].class);
                    List<SearchResult> searchResults = new ArrayList<>();
                    Collections.addAll(searchResults,searchResultData);
                    subscriber.onNext(searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<List<SearchResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG",e.toString());
            }

            @Override
            public void onNext(List<SearchResult> searchResults) {
                if (searchResults.size()!=0){
                    mSearchView.setSuggestions(searchResults);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        }else{
            super.onBackPressed();

        }
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(MainActivity.this, SendPhotoActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }
}
