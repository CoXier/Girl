package com.hackerli.retrofit.module.sendphoto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.util.GetPathFromUri;
import com.hackerli.retrofit.util.ToastUtil;

import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendPhotoActivity extends AppCompatActivity implements SendContract.View {

    @Bind(R.id.toolbar_actionbar)
    Toolbar mToolbar;

    @Bind(R.id.imageView_pick)
    ImageView mImageViewPick;
    @Bind(R.id.text_photo_desc)
    TextInputEditText mTextPhotoDesc;
    @Bind(R.id.text_photo_name)
    TextInputEditText mTextPhotoName;
    @Bind(R.id.imageView_send)
    ImageView mImageViewSend;
    @Bind(R.id.cardView_wrapper)
    CardView mCardViewWrapper;

    private SendPresenter mPresenter;
    private Uri mPhotoUri;
    private static int RESULT_LOAD_IMG = 1;
    private boolean mHasSelectedPhoto = false;
    private ProgressDialog mDialog;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enterFromBottomAnimation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_photo);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Send Photo");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new SendPresenter(this);
        mDialog = new ProgressDialog(this);
    }

    @Override
    protected void onPause() {
        exitToBottomAnimation();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_pick) {
            loadImageFromGallery();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadImageIfGranted();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void loadImageIfGranted() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择文件"), RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            mPhotoUri = data.getData();
            ContentResolver contentResolver = getContentResolver();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 4;
                mBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(mPhotoUri),null,options);
                showPickedPhoto(mBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImageIfGranted();
            } else {
                ToastUtil.showToast(this, "没有相关权限");
            }

        }
    }

    protected void enterFromBottomAnimation() {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
    }

    protected void exitToBottomAnimation() {
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
    }

    @Override
    public void showPickedPhoto(Bitmap bitmap) {
        mCardViewWrapper.setVisibility(View.VISIBLE);
        mImageViewPick.setImageBitmap(bitmap);
        mHasSelectedPhoto = true;
    }

    @Override
    public void clearPhoto() {
        mCardViewWrapper.setVisibility(View.INVISIBLE);
        mTextPhotoDesc.setText(null);
        mTextPhotoName.setText(null);
        mTextPhotoDesc.clearFocus();
        mTextPhotoName.clearFocus();
        mBitmap.recycle();
    }

    @Override
    public void showProgressDialog() {
        mDialog.show();
        mDialog.setMessage("上传中");
    }

    @Override
    public void dismissProgressDialog(String result) {
        mDialog.setMessage(result);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,400);

    }

    @Override
    public void setPresenter(SendContract.Presenter presenter) {
        mPresenter = (SendPresenter) presenter;
    }

    @OnClick(R.id.imageView_send)
    public void onClick() {
        if (mHasSelectedPhoto){
            String desc = mTextPhotoDesc.getText().toString();
            String name = mTextPhotoName.getText().toString();
            if (!desc.isEmpty()&&!name.isEmpty()){
                GetPathFromUri getPathFromUri = new GetPathFromUri();
                String uri = getPathFromUri.getPath(mPhotoUri,this);
                mPresenter.sendPhoto(uri,desc,name);
            }else {
                ToastUtil.showToast(this,"请输入信息");
            }
        }else {
            ToastUtil.showToast(this,"未添加照片");
        }
    }
}
