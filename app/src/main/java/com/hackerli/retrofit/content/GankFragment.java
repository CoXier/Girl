package com.hackerli.retrofit.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.WebActivity;
import com.hackerli.retrofit.data.entity.AndroidWrapper;
import com.hackerli.retrofit.presenter.GankPresenter;
import com.hackerli.retrofit.ui.AndroidAdapter;
import com.hackerli.retrofit.ui.listener.GankOnClickListener;
import com.hackerli.retrofit.util.SnackBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankFragment extends BaseFragment implements GankOnClickListener {
    @Bind(R.id.gank_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycle_gank)
    RecyclerView recyclerView;

    private int page = 1;
    private List<AndroidWrapper> androidWrappers = new ArrayList<>();
    private AndroidAdapter androidAdapter;
    private GankPresenter gankPresenter = new GankPresenter(this);

    private boolean mIsFirstTouchedBottom = true;
    private boolean mIsFirstCreated = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, v);

        setRecyclerView();
        setSwipeRefreshLayout();

        // 进入之后先加载，故refresh
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
        int size = androidWrappers.size();
        for (Object wrapper : list) {
            androidWrappers.add((AndroidWrapper) wrapper);
        }
        recyclerView.requestLayout();
        if (androidWrappers.size()-size==10){
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
        swipeRefreshLayout.setRefreshing(true);
        gankPresenter.loadMore(page);
        mIsFirstCreated = false;
    }

    @Override
    public void setRecyclerView() {
        androidAdapter = new AndroidAdapter(this,androidWrappers);
        recyclerView.setAdapter(androidAdapter);
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
                boolean isBttom = lastVisiblePosition > androidAdapter.getItemCount() - 4;
                if (isBttom && !swipeRefreshLayout.isRefreshing()) {
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
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
