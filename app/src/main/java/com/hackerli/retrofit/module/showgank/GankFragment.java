package com.hackerli.retrofit.module.showgank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.retrofit.BaseFragment;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.AndroidWrapper;
import com.hackerli.retrofit.module.showgank.adapter.AndroidAdapter;
import com.hackerli.retrofit.module.showgank.adapter.GankOnClickListener;
import com.hackerli.retrofit.util.SnackBarUtil;
import com.hackerli.retrofit.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankFragment extends BaseFragment implements GankOnClickListener, GankContract.View {
    @Bind(R.id.gank_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycle_gank)
    RecyclerView recyclerView;

    private int page = 1;
    private int limt = 10;
    private List<AndroidWrapper> mWrappers = new ArrayList<>();
    private AndroidAdapter mAdapter;
    private GankContract.Presenter mPresenter = new GankPresenter(this);

    private boolean mIsFirstTouchedBottom = true;
    private boolean mIsFirstCreated = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, v);

        setRecyclerView();
        setSwipeRefreshLayout();

        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        // 第一次调用createView
        if (mIsFirstCreated) {
            onRefresh();
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void showMore(@Nullable List list) {
        int size = mWrappers.size();
        for (Object wrapper : list) {
            mWrappers.add((AndroidWrapper) wrapper);
        }
        recyclerView.requestLayout();
        if (mWrappers.size() - size == 10) {
            page++;
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void finishRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSnackBar() {
        SnackBarUtil.showSnackBar(recyclerView, this);
    }

    @Override
    public void onRefresh() {
        if (page < limt) {
            swipeRefreshLayout.setRefreshing(true);
            mPresenter.loadMore(page);
            mIsFirstCreated = false;
        }else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setRecyclerView() {
        mAdapter = new AndroidAdapter(this, mWrappers);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        animationAdapter.setDuration(800);
        recyclerView.setAdapter(animationAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    @Override
    public void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);
    }

    // if user have reached bottom ,we should load more
    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        RecyclerView.OnScrollListener bottomListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                boolean isBottom = lastVisiblePosition > mAdapter.getItemCount() - 4;
                if (isBottom && !swipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBottom) {
                        onRefresh();
                    } else mIsFirstTouchedBottom = false;
                }
            }
        };
        return bottomListener;
    }

    @Override
    public void viewGankOnWebView(String url) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void setPresenter(GankContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
