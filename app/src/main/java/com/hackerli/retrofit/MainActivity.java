package com.hackerli.retrofit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.modle.GirlModle;
import com.hackerli.retrofit.ui.GirlAdapter;
import com.hackerli.retrofit.ui.GirlOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recl)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    int page = 0;
    List<Girl> girls = new ArrayList<>();
    GirlAdapter girlAdapter;
    final GirlModle girlModle = new GirlModle();

    private boolean mIsFirstTouchedBttom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setRecyclerView();
        setSwipeRefeshLayout();

        // 进入之后先加载，故refresh
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        onRefresh();

        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void setSwipeRefeshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);
    }

    public void setRecyclerView() {
        girlAdapter = new GirlAdapter(MainActivity.this, girls);
        recyclerView.setAdapter(girlAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(getOnBttomListener(gridLayoutManager));


        girlAdapter.setOnClickListener(new GirlOnClickListener() {
            @Override
            public void viewGirlPhoto(String photoUrl) {
               FragmentManager fragmentManager = getSupportFragmentManager();
                GirlPhotoFragment fragment = GirlPhotoFragment.newInstance(photoUrl);
                fragment.show(fragmentManager,"fragment_girl_photo");
                fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);

//                Intent intent = new Intent(MainActivity.this,PhotoActivity.class);
//                intent.putExtra(PhotoActivity.EXTRA_IAMGE_URL,photoUrl);
//                startActivity(intent);
            }

            @Override
            public void viewCSMaterial() {

            }
        });
    }


    // swipelayout uses this method to refresh
    @Override
    public void onRefresh() {
        page++;
        swipeRefreshLayout.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
        girlModle.getGirs(page, recyclerView, girls);
    }


    // if user have reached bttom ,we should load more
    RecyclerView.OnScrollListener getOnBttomListener(final StaggeredGridLayoutManager layoutManager) {
        RecyclerView.OnScrollListener bttomListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] lastVisiblePositions = new int[2];
                lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePositions);
                int right = lastVisiblePositions[1];
                boolean isBttom = right > girlAdapter.getItemCount() - 7;
                Log.d("TAG",girlAdapter.getItemCount()+"");
                if (isBttom && !swipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBttom) {
                        onRefresh();
                    } else mIsFirstTouchedBttom = false;
                }
            }
        };
        return bttomListener;
    }


}
