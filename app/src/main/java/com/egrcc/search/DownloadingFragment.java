package com.egrcc.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by egrcc on 2/26/15.
 */
public class DownloadingFragment extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_downloading, container, false);
        listView = (ListView) v.findViewById(R.id.downloadingListView);
        return v;
    }

    public static DownloadingFragment newInstance(String wangpan, String keyword) {

        DownloadingFragment f = new DownloadingFragment();
        Bundle b = new Bundle();
        b.putString("keyword", keyword);
        b.putString("wangpan", wangpan);
        f.setArguments(b);

        return f;
    }
}
