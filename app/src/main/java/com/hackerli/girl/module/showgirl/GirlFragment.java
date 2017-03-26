package com.hackerli.girl.module.showgirl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackerli.girl.base.BaseFragment;
import com.hackerli.girl.R;
import com.hackerli.girl.data.entity.Girl;
import com.hackerli.girl.module.showgirl.adapter.GirlAdapter;
import com.hackerli.girl.module.showgirl.adapter.GirlOnClickListener;
import com.hackerli.girl.module.showgirl.viewgirlphoto.GirlPhotoFragment;
import com.hackerli.girl.util.SnackBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GirlFragment extends BaseFragment implements GirlOnClickListener, GirlContract.View {

    @Bind(R.id.recl)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mPage = 1;
    private List<Girl> mGirls = new ArrayList<>();
    private GirlAdapter mGirlAdapter;
    private GirlContract.Presenter mPresenter = new GirlPresenter(this);

    private boolean mIsFirstTouchedBottom = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        ButterKnife.bind(this, view);

        setRecyclerView();
        setSwipeRefreshLayout();

        // 进入之后先加载，故refresh
        mSwipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        onRefresh();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void setRecyclerView() {
        mGirlAdapter = new GirlAdapter(this, mGirls);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mGirlAdapter));
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnScrollListener(getOnBottomListener(gridLayoutManager));
    }

    @Override
    public void setSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);

    }

    @Override
    public void showMore(@Nullable List list) {
        int size = mGirls.size();
        for (Object girl : list) {
            mGirls.add((Girl) girl);
        }
        if (mRecyclerView != null) {
            mRecyclerView.requestLayout();
        }
        if (mGirls.size() - size == 10) {
            mPage++;
        }
    }

    @Override
    public void finishRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSnackBar() {
        SnackBarUtil.showSnackBar(mRecyclerView, this);
    }

    @Override
    public void onRefresh() {
        int pageLimit = 10;
        if (mPage <= pageLimit) {
            mSwipeRefreshLayout.setRefreshing(true);
            mPresenter.loadMore(mPage);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void viewGirlPhoto(Girl girl) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        GirlPhotoFragment fragment = GirlPhotoFragment.newInstance(girl.getUrl(), girl.getDesc());
        fragment.show(fragmentManager, "fragment_girl_photo");
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * The code segment is I learn from Meizhi created by drakeet
     * Meizhi is under the terms of the GNU General Public License as published by
     * the Free Software Foundation, either version 3 of the License, or
     * (at your option) any later version.
     */
    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] lastVisiblePositions = new int[2];
                lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePositions);
                int right = lastVisiblePositions[1];
                boolean isBottom = right > mGirlAdapter.getItemCount() - 7;
                if (isBottom && !mSwipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBottom) {
                        onRefresh();
                    } else mIsFirstTouchedBottom = false;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mGirlAdapter.setScrollState(true);
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        mGirlAdapter.setScrollState(false);
                        break;
                }
            }
        };
    }


    @Override
    public void setPresenter(GirlContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
