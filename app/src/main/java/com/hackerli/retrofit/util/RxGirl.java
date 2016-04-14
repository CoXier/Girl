package com.hackerli.retrofit.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/11.
 */
public class RxGirl {

    public static void saveBitamp(final Context context, final String url, final String desc){
        Observable observable = Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                Bitmap bitmap ;
                try {
                    bitmap = Picasso.with(context).load(url).get();

                    File appDir = new File(Environment.getExternalStorageDirectory(), "Retrofit");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    File file = new File(appDir, desc +".jpg");
                    try {
                        bitmap = Picasso.with(context).load(url).get();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        assert bitmap != null;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Uri uri = Uri.fromFile(file);
                    // 通知图库更新
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    context.sendBroadcast(scannerIntent);
                    subscriber.onNext(uri);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Uri>() {
            @Override
            public void onCompleted() {
                Log.d("TAG","Completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Uri uri) {
                Toast.makeText(context,"保存至"+uri.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
