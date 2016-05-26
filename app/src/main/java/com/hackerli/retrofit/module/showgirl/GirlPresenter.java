package com.hackerli.retrofit.module.showgirl;

import android.support.annotation.NonNull;

import com.hackerli.retrofit.api.ApiServiceFactory;
import com.hackerli.retrofit.api.GankioService;
import com.hackerli.retrofit.data.GirlData;
import com.hackerli.retrofit.data.entity.Girl;
import com.hackerli.retrofit.db.AppDatabase;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by XoCier on 2016/3/20.
 */
public class GirlPresenter implements GirlContract.Presenter {

    private GirlData mGirlData = null;
    private GirlContract.View mView;
    private GankioService mService;
    private static ArrayList<Girl> mLocalGirls = new ArrayList<>();
    private boolean shouldLoadFromInternet = true;
    private int start = 1;

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
                mLocalGirls = (ArrayList<Girl>) tResult.toListClose();
                orderGirlByTime(mLocalGirls);
            }
        }).execute();
    }

    private static void orderGirlByTime(ArrayList<Girl> mLocalGirls) {
        Collections.sort(mLocalGirls);
    }

    @Override
    public void loadMore(int page) {

        if (shouldLoadFromInternet) {
            loadFromInternet(page);
        }else if (mLocalGirls.size()>=100){
            loadFromLocal(page);
        }

    }

    private void loadFromLocal(int page) {
        int i = 0;
        List<Girl> newGirl = new ArrayList<>();
        while(i<10){
            Girl girl = mLocalGirls.get((page-start)*10+i);
            newGirl.add(girl);
            i++;
        }
        mView.showMore(newGirl);
        mView.finishRefresh();
    }

    private void loadFromInternet(int page) {
        start = page;
        final Call<GirlData> girlDataCall = mService.getGirls(page);
        girlDataCall.enqueue(new Callback<GirlData>() {
            @Override
            public void onResponse(Call<GirlData> call, Response<GirlData> response) {
                mGirlData = response.body();
                List<Girl> newGirls = mGirlData.getGirls();
                // check if newGirls should be added into local girls
                if (checkShouldAdded(newGirls)){
                    addGirlsToDB(newGirls);
                }else{
                    shouldLoadFromInternet = false;
                }
                mView.showMore(newGirls);
                mView.finishRefresh();
            }

            @Override
            public void onFailure(Call<GirlData> call, Throwable t) {
                mView.finishRefresh();
                mView.showSnackBar();
            }
        });
    }

    private void addGirlsToDB(List<Girl> newGirls) {
        ProcessModelTransaction<Girl> processModelTransaction
                        = new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<Girl>() {
                    @Override
                    public void processModel(Girl model) {
                        model.save();
                        mLocalGirls.add(model);
                    }
                }).addAll(newGirls).build();
                Transaction transaction = FlowManager.getDatabase(AppDatabase.class).beginTransactionAsync(processModelTransaction).build();
                transaction.execute();
    }

    private boolean checkShouldAdded(List<Girl> newGirls) {
        for(int i=newGirls.size()-1;i>-1;i--){
            Girl newGirl = newGirls.get(i);
            for (Girl girl: mLocalGirls){
                if (girl.getUrl().equals(newGirl.getUrl()))
                    return false;
            }
        }
        return true;
    }


}
