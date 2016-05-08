package com.hackerli.retrofit.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.module.showgank.GankFragment;
import com.hackerli.retrofit.module.showgirl.GirlFragment;
import com.hackerli.retrofit.module.showvideo.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.fla_add)
    FloatingActionButton flaAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("gank.io");
        setTabs();


    }

    private void setTabs() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GirlFragment());
        fragments.add(new GankFragment());
        fragments.add(new VideoFragment());

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
