package com.hackerli.retrofit.content;

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

import com.hackerli.retrofit.GirlPhotoFragment;
import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.presenter.GirlPresenter;
import com.hackerli.retrofit.ui.GirlAdapter;
import com.hackerli.retrofit.ui.listener.GirlOnClickListener;
import com.hackerli.retrofit.util.SnackBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/2.
 */
public class GirlFragment extends BaseFragment implements GirlOnClickListener {

    @Bind(R.id.recl)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private List<Girl> mGirls = new ArrayList<>();
    private GirlAdapter girlAdapter;
    private final GirlPresenter girlPresenter = new GirlPresenter(this);

    private boolean mIsFirstTouchedBottom = true;
    private boolean mIsFirstCreated = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        ButterKnife.bind(this, view);

        setRecyclerView();
        setSwipeRefreshLayout();

        // 进入之后先加载，故refresh
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        if (mIsFirstCreated){
            onRefresh();
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void setRecyclerView() {
        girlAdapter = new GirlAdapter(this, mGirls);
        recyclerView.setAdapter(girlAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(getOnBottomListener(gridLayoutManager));
    }

    @Override
    public void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_process1, R.color.refresh_process2, R.color.refresh_process3);

    }

    @Override
    public void showMore(@Nullable List list) {
        int size = mGirls.size();
        for (Object girl : list) {
            mGirls.add((Girl) girl);
        }
        recyclerView.requestLayout();
        if (mGirls.size()-size==10){
            page++;
        }
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
        girlPresenter.loadMore(page);
        mIsFirstCreated = false;
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

    // if user have reached bottom ,we should load more
    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        RecyclerView.OnScrollListener bottomListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int[] lastVisiblePositions = new int[2];
                lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePositions);
                int right = lastVisiblePositions[1];
                boolean isBottom = right > girlAdapter.getItemCount() - 7;
                if (isBottom && !swipeRefreshLayout.isRefreshing()) {
                    if (!mIsFirstTouchedBottom) {
                        onRefresh();
                    } else mIsFirstTouchedBottom = false;
                }
            }
        };
        return bottomListener;
    }
}
