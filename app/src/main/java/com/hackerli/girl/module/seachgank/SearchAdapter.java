package com.hackerli.girl.module.seachgank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackerli.girl.R;
import com.hackerli.girl.data.entity.SearchResult;

import java.util.List;

/**
 * Created by CoXier on 2016/5/28.
 */
public class SearchAdapter extends BaseAdapter {

    private List<SearchResult> mResults;

    public SearchAdapter(List<SearchResult> results) {
        this.mResults = results;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, null);
        TextView textView = (TextView) view.findViewById(R.id.search_item);
        textView.setText(mResults.get(position).getTitle());
        return view;
    }
}
