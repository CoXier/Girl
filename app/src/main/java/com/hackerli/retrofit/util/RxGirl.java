package com.hackerli.retrofit.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by CoXier on 2016/4/11.
 */
public class RxGirl {

    public static void saveBitmap(final Context context, final String url, final String desc) {
        Observable observable = Observable.just(url).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    return Picasso.with(context).load(s).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).map(new Func1<Bitmap, Uri>() {
            @Override
            public Uri call(Bitmap bitmap) {
                if (bitmap != null) {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Girl");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    File file = new File(appDir, desc + ".jpg");
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
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

                    return uri;
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                Toast.makeText(context, "保存至" + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
