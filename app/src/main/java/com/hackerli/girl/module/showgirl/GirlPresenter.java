package com.hackerli.girl.module.showgirl;


import com.hackerli.girl.data.provider.GirlProvider;
import com.hackerli.girl.data.provider.IProvider;
import com.hackerli.girl.data.GirlData;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by XoCier on 2016/3/20.
 */
public class GirlPresenter implements GirlContract.Presenter {

    private GirlContract.View mView;

    private Disposable mDisposable;
    private IProvider<GirlData> mDataProvider;

    public GirlPresenter(GirlContract.View baseView) {
        this.mView = baseView;
        init();
    }

    private void init() {
        mView.setPresenter(this);
        mDataProvider = new GirlProvider();
    }

    @Override
    public void loadMore(int page) {
        mDataProvider.loadMore(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        girlData -> {
                            mView.showMore(girlData.getGirls());
                            mView.finishRefresh();
                        },
                        throwable -> {
                            mView.finishRefresh();
                            mView.showSnackBar();
                        },
                        () -> {
                        },
                        disposable -> mDisposable = disposable);
    }

    @Override
    public void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
