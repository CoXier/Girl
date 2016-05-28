package com.hackerli.retrofit.module.seachgank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.data.entity.SearchResult;

import java.util.List;

/**
 * Created by CoXier on 2016/5/28.
 */
public class SearchAdapter extends BaseAdapter{

    List<SearchResult> results;

    public SearchAdapter(List<SearchResult> results) {
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,null);
        TextView textView = (TextView) view.findViewById(R.id.search_item);
        textView.setText(results.get(position).getTitle());
        return view;
    }
}
