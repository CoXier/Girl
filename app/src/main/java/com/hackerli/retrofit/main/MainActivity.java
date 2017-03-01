package com.hackerli.retrofit.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import com.github.fabtransitionactivity.SheetLayout;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.module.sendphoto.SendPhotoActivity;
import com.hackerli.retrofit.module.showgank.GankFragment;
import com.hackerli.retrofit.module.showgirl.GirlFragment;
import com.hackerli.retrofit.module.showgirl.GirlPresenter;
import com.hackerli.retrofit.module.showvideo.VideoFragment;
import com.hackerli.retrofit.ui.SearchWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        // load data from db while app is starting
        GirlPresenter.setLocalGirl();
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
