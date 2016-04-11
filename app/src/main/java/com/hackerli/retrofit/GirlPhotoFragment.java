package com.hackerli.retrofit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hackerli.retrofit.util.NetWordUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
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
                NetWordUtils netWordUtils = new NetWordUtils(getActivity());
                if (netWordUtils.isNetConnected()) {
                    // 获取存储权限
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }else {
                        new SaveBitmapTask().execute(getArguments().getString("photoUrl"));
                    }
                }
                return false;
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
        if (requestCode==1){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                new SaveBitmapTask().execute(getArguments().getString("photoUrl"));
            }else {
                Log.d("TAG","failure");
            }

        }
    }

    class SaveBitmapTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d("TAG","saved");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Bitmap bitmap = null;
            File appDir = new File(Environment.getExternalStorageDirectory(), "Retrofit");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, getArguments().getString("desc")+".jpg");
            try {
                bitmap = Picasso.with(getActivity()).load(params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
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

            Log.d("TAG",file.toString());
            Uri uri = Uri.fromFile(file);
            // 通知图库更新
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            getActivity().sendBroadcast(scannerIntent);
            return true;
        }
    }

}
