package com.hackerli.retrofit.module.sendphoto;

import android.graphics.Bitmap;

import com.hackerli.retrofit.base.BaseView;

/**
 * Created by CoXier on 2016/5/29.
 */
public interface SendContract {
    interface View extends BaseView<SendContract.Presenter>{
        void showPickedPhoto(Bitmap bitmap);
        void clearPhoto();
        void showProgressDialog();
        void dismissProgressDialog(String result);
    }

    interface Presenter {
        void sendPhoto(String uri,String desc,String name);
    }
}
