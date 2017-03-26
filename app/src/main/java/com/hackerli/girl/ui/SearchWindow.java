package com.hackerli.girl.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.hackerli.girl.data.entity.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CoXier on 16-12-22.
 */

public class SearchWindow extends PopupWindow {
    private MaterialSearchView contentView;


    public SearchWindow(final Context context) {
        super(context);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
//        setOutsideTouchable(true);
        contentView = new MaterialSearchView(context);
        contentView.setPopupWindow(this);
        contentView.setFocusable(true);
        setContentView(contentView);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initContentView();
    }

    private void initContentView() {
        contentView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // search data by keyword
                if (!query.isEmpty()) {
                    searchData(query);
                    contentView.hideKeyboard();
                } else {
                    contentView.clearData();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @Override
    public void dismiss() {
        contentView.closeSearch();
        contentView.clearData();
        super.dismiss();
    }

    public void show(View view) {
        if (!isShowing()) {
            showAtLocation(view, Gravity.TOP, 0, 0);
            contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    contentView.removeOnLayoutChangeListener(this);
                    contentView.showSearch();
                }
            });
        }

    }

    private void searchData(final String newText) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("keyword", newText).build();
        Request request = new Request.Builder()
                .url("http://gankio.herokuapp.com/search")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                SearchResult[] searchResultData = gson.fromJson(json, SearchResult[].class);
                final List<SearchResult> searchResults = new ArrayList<>();
                Collections.addAll(searchResults, searchResultData);
                if (searchResults.size() != 0) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            contentView.setSuggestions(searchResults);
                        }
                    });

                }
            }
        });
    }

}
