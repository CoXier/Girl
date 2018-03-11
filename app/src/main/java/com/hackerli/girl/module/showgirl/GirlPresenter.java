package com.hackerli.girl.module.showgirl;

import android.support.annotation.NonNull;

import com.hackerli.girl.network.api.ApiServiceFactory;
import com.hackerli.girl.network.api.GankIoService;
import com.hackerli.girl.data.GirlData;
import com.hackerli.girl.data.entity.Girl;
import com.hackerli.girl.db.AppDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by XoCier on 2016/3/20.
 */
public class GirlPresenter implements GirlContract.Presenter {

    private GirlData mGirlData = null;
    private GirlContract.View mView;
    private GankIoService mService;
    private static ArrayList<Girl> sLocalGirls = new ArrayList<>();
    private boolean mShouldLoadFromInternet = true;
    private int mStart = 1;

    private Disposable mDisposable;

    public GirlPresenter(GirlContract.View baseView) {
        this.mView = baseView;
        init();
    }

    private void init() {
        mService = ApiServiceFactory.buildGankioService();
        mView.setPresenter(this);
    }

    // init local girl from db
    public static void setLocalGirl() {
        SQLite.select().from(Girl.class).async().queryResultCallback(new QueryTransaction.QueryResultCallback<Girl>() {
            @Override
            public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<Girl> tResult) {
                sLocalGirls = (ArrayList<Girl>) tResult.toListClose();
                orderGirlByTime(sLocalGirls);
            }
        }).execute();
    }

    private static void orderGirlByTime(ArrayList<Girl> mLocalGirls) {
        Collections.sort(mLocalGirls);
    }

    @Override
    public void loadMore(int page) {

        if (mShouldLoadFromInternet || sLocalGirls.size() < 100) {
            loadFromInternet(page);
        } else {
            loadFromLocal(page);
        }

    }

    @Override
    public void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void loadFromLocal(int page) {
        int i = 0;
        List<Girl> newGirl = new ArrayList<>();
        while (i < 10) {
            Girl girl = sLocalGirls.get((page - mStart) * 10 + i);
            newGirl.add(girl);
            i++;
        }
        mView.showMore(newGirl);
        mView.finishRefresh();
    }

    private void loadFromInternet(int page) {
        mStart = page;
        mService.getGirls(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        girlData -> {
                            mGirlData = girlData;
                            List<Girl> newGirls = mGirlData.getGirls();
                            // check if newGirls should be added into local girls
                            if (checkShouldAdded(newGirls)) {
                                addGirlsToDB(newGirls);
                            } else {
                                mShouldLoadFromInternet = false;
                            }
                            mView.showMore(newGirls);
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

    private void addGirlsToDB(List<Girl> newGirls) {
        ProcessModelTransaction<Girl> processModelTransaction
                = new ProcessModelTransaction.Builder<>((ProcessModelTransaction.ProcessModel<Girl>) model -> {
                    model.save();
                    sLocalGirls.add(model);
                }).addAll(newGirls).build();
        Transaction transaction = FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(processModelTransaction).build();
        transaction.execute();
    }

    private boolean checkShouldAdded(List<Girl> newGirls) {
        for (int i = newGirls.size() - 1; i > -1; i--) {
            Girl newGirl = newGirls.get(i);
            for (Girl girl : sLocalGirls) {
                if (girl.getUrl().equals(newGirl.getUrl()))
                    return false;
            }
        }
        return true;
    }

}
