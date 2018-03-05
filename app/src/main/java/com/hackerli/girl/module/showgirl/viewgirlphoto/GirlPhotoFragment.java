package com.hackerli.girl.module.showgirl.viewgirlphoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.hackerli.girl.R;
import com.hackerli.girl.network.NetworkComponent;
import com.hackerli.girl.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GirlPhotoFragment extends DialogFragment {
    @BindView(R.id.iv_fr_girl)
    PhotoView mPhotoView;


    public GirlPhotoFragment() {
        // note that empty method is requried for dialogfragment
    }

    public static GirlPhotoFragment newInstance(String photoUrl, String desc) {
        GirlPhotoFragment fragment = new GirlPhotoFragment();
        Bundle args = new Bundle();
        args.putString("photoUrl", photoUrl);
        args.putString("desc", desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl_photo, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Glide.with(getActivity())
                .load(getArguments().getString("photoUrl"))
                .into(mPhotoView);
        setupPhotoEvent();

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupPhotoEvent() {
        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                dismiss();
            }
        });

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (NetworkComponent.isNetConnected(getActivity())) {
                    // 获取存储权限
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        saveBitmap(getArguments().getString("photoUrl"), getArguments().getString("desc"));
                    }
                }
                return false;
            }
        });
    }

    private void saveBitmap(final String photoUrl, final String desc) {
        Observable.create(new CallOnSubscribe(photoUrl))
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Response, Observable<File>>() {
                    @Override
                    public Observable<File> call(Response response) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), "Girl");
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        final File file = new File(appDir, desc + ".jpg");
                        okio.BufferedSink bufferedSink = null;
                        try {
                            bufferedSink = okio.Okio.buffer(okio.Okio.sink(file));
                            bufferedSink.writeAll(response.body().source());
                            bufferedSink.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(file);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        Uri uri = Uri.fromFile(file);
                        // 通知图库更新
                        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        if (getActivity() != null) {
                            getActivity().sendBroadcast(scannerIntent);
                            ToastUtil.showToast(getActivity(), "保存至" + uri.toString());
                        }
                    }
                });
    }

    private static class CallOnSubscribe implements Observable.OnSubscribe<Response> {
        String mPhotoUrl;


        CallOnSubscribe(String photoUrl) {
            this.mPhotoUrl = photoUrl;
        }

        @Override
        public void call(Subscriber<? super Response> subscriber) {
            OkHttpClient client = NetworkComponent.getInstance().getClient();
            Request request = new okhttp3.Request.Builder().url(mPhotoUrl).build();
            try {
                Response response = client.newCall(request).execute();
                subscriber.onNext(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveBitmap(getArguments().getString("photoUrl"), getArguments().getString("desc"));
            } else {
                ToastUtil.showToast(getActivity(), "没有相关权限");
            }

        }
    }

}
