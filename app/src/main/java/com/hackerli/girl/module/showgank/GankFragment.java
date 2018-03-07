package com.hackerli.girl.module.showgank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.girl.base.BaseFragment;
import com.hackerli.girl.R;
import com.hackerli.girl.data.entity.AndroidWrapper;
import com.hackerli.girl.module.showgank.adapter.GankAdapter;
import com.hackerli.girl.module.showgank.adapter.GankOnClickListener;
import com.hackerli.girl.util.SnackBarUtil;
import com.hackerli.girl.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GankFragment extends BaseFragment implements GankOnClickListener, GankContract.View {
    @BindView(R.id.gank_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle_gank)
    RecyclerView mRecyclerView;

    private int mPage = 1;
    private List<AndroidWrapper> mWrappers = new ArrayList<>();
    private GankAdapter mAdapter;
    private GankContract.Presenter mPresenter = new GankPresenter(this);

    private boolean mIsFirstTouchedBottom = true;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gank, container, false);
        mUnbinder = ButterKnife.bind(this, v);

        setRecyclerView();
        setSwipeRefreshLayout();

        mSwipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        // 第一次调用createView

        onRefresh();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void showMore(@Nullable List list) {
        int size = mWrappers.size();
        for (Object wrapper : list) {
            mWrappers.add((AndroidWrapper) wrapper);
        }
        mAdapter.notifyDataSetChanged();
        if (mWrappers.size() - size == 10) {
            mPage++;
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        mPresenter.unSubscribe();
        super.onDestroyView();
    }

    @Override
    public void finishRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showSnackBar() {
        SnackBarUtil.showSnackBar(mRecyclerView, this);
    }

    @Override
    public void onRefresh() {
        // we limit mPage size < 10
        if (mPage < 10) {
            mSwipeRefreshLayout.setRefreshing(true);
            mPresenter.loadMore(mPage);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setRecyclerView() {
        mAdapter = new GankAdapter(this, mWrappers);
        SlideInBottomAnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        animationAdapter.setDuration(800);
        mRecyclerView.setAdapter(animationAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    @Override
    public void setSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);
    }

    // if user have reached bottom ,we should load more
    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        RecyclerView.OnScrollListener bottomListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                boolean isBottom = lastVisiblePosition > mAdapter.getItemCount() - 4;
                if (isBottom && !mSwipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBottom) {
                        onRefresh();
                    } else {
                        mIsFirstTouchedBottom = false;
                    }
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
