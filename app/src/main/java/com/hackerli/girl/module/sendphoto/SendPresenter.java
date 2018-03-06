package com.hackerli.girl.module.sendphoto;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CoXier on 2016/5/29.
 */
public class SendPresenter implements SendContract.Presenter {
    private SendContract.View mView;

    public SendPresenter(SendContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void sendPhoto(String uri, final String desc, final String name) {
        mView.clearPhoto();
        mView.showProgressDialog();
        String fileName = System.nanoTime() + ".jpg";
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(fileName, uri);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("url", file.getUrl())
                                    .add("desc", desc)
                                    .add("who", name)
                                    .add("type", "福利")
                                    .add("debug", "false")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("https://gank.io/api/add2gank")
                                    .post(requestBody)
                                    .build();
                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                emitter.onNext(response.body().string());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if (s.contains("true")) {
                                mView.dismissProgressDialog("上传失败");
                            } else {
                                mView.dismissProgressDialog("上传成功");

                            }
                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
