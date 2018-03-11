package com.hackerli.girl.data.provider;


import io.reactivex.Observable;

/**
 * Created by lijianxin on 2018/3/11.
 */

public interface IProvider<T> {
    Observable<T> loadMore(int page);
}
