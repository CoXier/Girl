package com.hackerli.retrofit.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hackerli.retrofit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoXier on 2016/5/2.
 */
public class VideoFragment extends Fragment {


    @Bind(R.id.wv_video)
    WebView wvVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        wvVideo.getSettings().setJavaScriptEnabled(true);
        wvVideo.setWebChromeClient(new WebChromeClient());
        wvVideo.setWebViewClient(new WebViewClient());
        wvVideo.loadUrl("http://v.rpsofts.com/i.php?url=http://v.youku.com/v_show/id_XMTg5MDE4ODg=.html?f=2254942&o=1");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
