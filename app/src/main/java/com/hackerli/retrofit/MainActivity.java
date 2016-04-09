package com.hackerli.retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.presenter.GirlPresenter;
import com.hackerli.retrofit.presenter.GirlView;
import com.hackerli.retrofit.ui.GirlAdapter;
import com.hackerli.retrofit.ui.GirlOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,GirlOnClickListener,GirlView {
    @Bind(R.id.recl)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    int page = 0;
    List<Girl> mGirls = new ArrayList<>();
    GirlAdapter girlAdapter;
    final GirlPresenter girlPresenter = new GirlPresenter(this);

    private boolean mIsFirstTouchedBttom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setRecyclerView();
        setSwipeRefreshLayout();

        // 进入之后先加载，故refresh
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        onRefresh();

        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);
    }

    public void setRecyclerView() {
        girlAdapter = new GirlAdapter(MainActivity.this, mGirls);
        recyclerView.setAdapter(girlAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(getOnBottomListener(gridLayoutManager));

    }


    // if user have reached bottom ,we should load more
    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        RecyclerView.OnScrollListener bottomListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] lastVisiblePositions = new int[2];
                lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePositions);
                int right = lastVisiblePositions[1];
                boolean isBttom = right > girlAdapter.getItemCount() - 7;
                if (isBttom && !swipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBttom) {
                        onRefresh();
                    } else mIsFirstTouchedBttom = false;
                }
            }
        };
        return bottomListener;
    }

    // swipelayout uses this method to refresh
    @Override
    public void onRefresh() {
        page++;
        swipeRefreshLayout.setRefreshing(true);
        girlPresenter.showMoreGirls(page);
    }


    @Override
    public void viewGirlPhoto(String photoUrl) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GirlPhotoFragment fragment = GirlPhotoFragment.newInstance(photoUrl);
        fragment.show(fragmentManager,"fragment_girl_photo");
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    public void viewCSMaterial() {

    }

    @Override
    public void loadMore(@Nullable List<Girl> girls) {
        for (Girl girl:girls){
         mGirls.add(girl);
        }
        recyclerView.requestLayout();
    }

    @Override
    public void finishRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSnackBar() {
        Snackbar.make(recyclerView,"加载失败，请重试",Snackbar.LENGTH_LONG)
                .setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefresh();
                    }
                })
                .show();
    }
}
