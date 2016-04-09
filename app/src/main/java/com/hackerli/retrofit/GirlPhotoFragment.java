package com.hackerli.retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hackerli.retrofit.util.NetWordUtils;
import com.squareup.picasso.Picasso;

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


    public GirlPhotoFragment(){
        // note that empty method is requried for dialogfragment
    }

    public static GirlPhotoFragment newInstance(String photoUrl){
        GirlPhotoFragment fragment = new GirlPhotoFragment();
        Bundle args = new Bundle();
        args.putString("photoUrl",photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.girl_photo_fragment,container);
        ButterKnife.bind(this,view);
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
                Log.d("NET",netWordUtils.getSubbNetType());
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
}
