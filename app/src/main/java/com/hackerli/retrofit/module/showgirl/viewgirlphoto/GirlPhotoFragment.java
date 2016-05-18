package com.hackerli.retrofit.module.showgirl.viewgirlphoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.util.NetWordUtil;
import com.hackerli.retrofit.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
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
    @Bind(R.id.iv_fr_girl)
    PhotoView photoView;


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
        View view = inflater.inflate(R.layout.girl_photo_fragment, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Picasso.with(getActivity())
                .load(getArguments().getString("photoUrl"))
                .into(photoView);
        setupPhotoEvent();

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupPhotoEvent() {
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                dismiss();
            }
        });

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NetWordUtil netWordUtil = new NetWordUtil(getActivity());
                if (netWordUtil.isNetConnected()) {
                    // 获取存储权限
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        saveBitmap(getArguments().getString("photoUrl"), getArguments().getString("desc"));
                    }
                }
                return false;
            }
        });
    }

    private void saveBitmap(String photoUrl, final String desc) {
        Observable observable = Observable.just(photoUrl).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    return Picasso.with(getActivity()).load(s).get();
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
                    getActivity().sendBroadcast(scannerIntent);

                    return uri;
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                ToastUtil.showToast(getActivity(),"保存至" + uri.toString());
            }
        });

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
                ToastUtil.showToast(getActivity(),"没有相关权限");
            }

        }
    }

}
