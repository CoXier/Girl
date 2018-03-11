package com.hackerli.girl.data.provider;

import com.hackerli.girl.data.GirlData;
import com.hackerli.girl.network.api.ApiServiceFactory;
import com.hackerli.girl.network.api.GankIoService;


import io.reactivex.Observable;

/**
 * Created by lijianxin on 2018/3/11.
 */

public class GirlProvider implements IProvider<GirlData> {

    private GankIoService mService;

    public GirlProvider() {
        mService = ApiServiceFactory.buildGankioService();
    }

    @Override
    public Observable<GirlData> loadMore(int page) {
        return mService.getGirls(page);
    }
}
