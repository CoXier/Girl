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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.github.fabtransitionactivity.SheetLayout;
import com.google.gson.Gson;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.SearchResult;
import com.hackerli.retrofit.ui.MaterialSearchView;
import com.hackerli.retrofit.module.sendphoto.SendPhotoActivity;
import com.hackerli.retrofit.module.showgank.GankFragment;
import com.hackerli.retrofit.module.showgirl.GirlFragment;
import com.hackerli.retrofit.module.showvideo.VideoFragment;
import com.hackerli.retrofit.ui.SearchWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener{
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.vp)
    ViewPager mVP;
    @Bind(R.id.fla_upload)
    FloatingActionButton mFab;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.bottom_sheet)
    SheetLayout mSheetLayout;

    SearchWindow mPopupWindow;

    List<Fragment> mFragments = new ArrayList<>();
    String[] mTitles;

    private static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("gank.io");
        setTabs();

        initFabEvent();
        initPopupWindow();
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

    private void initPopupWindow(){
        mPopupWindow = new SearchWindow(this);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFab.show();
            }
        });
    }

    private void setTabs() {
        mFragments.add(new GirlFragment());
        mFragments.add(new GankFragment());
        mFragments.add(new VideoFragment());

        mTitles = getResources().getStringArray(R.array.tabs_title);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        mVP.setAdapter(adapter);
        mVP.setOffscreenPageLimit(3);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            mPopupWindow.show(mToolbar);
            mFab.hide();
        }
        return super.onOptionsItemSelected(item);
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
