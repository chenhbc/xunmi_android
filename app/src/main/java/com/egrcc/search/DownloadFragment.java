package com.egrcc.search;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.egrcc.search.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by egrcc on 2/26/15.
 */
public class DownloadFragment extends Fragment {
    private ListView listView;
    private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();;
    private MyDatabaseHelper dbHelper;
    private SimpleAdapter mSimpleAdapter;
    private int[] imageId = new int[]{
            R.drawable.folder, R.drawable.video, R.drawable.music, R.drawable.pdf, R.drawable.picture,
            R.drawable.ppt, R.drawable.word, R.drawable.excel, R.drawable.unknown, R.drawable.text};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);
        listView = (ListView) v.findViewById(R.id.downloadListView);

        dbHelper = new MyDatabaseHelper(getActivity(), "DownloadStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Download",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                Long size = cursor.getLong(cursor.getColumnIndex("size"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                Log.v("app", name);
                Log.v("app", Long.toString(size));

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemTitle", name);
                map.put("ItemText", size);
                map.put("Image", imageId[1]);
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();




        mSimpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.listview_item,
                new String[]{"ItemTitle", "ItemText", "Image"},
                new int[]{R.id.tv_title, R.id.tv_content, R.id.fileImage});

        listView.setAdapter(mSimpleAdapter);

        return v;
    }

//    public static DownloadFragment newInstance(String wangpan, String keyword) {
//
//        DownloadFragment f = new DownloadFragment();
//        Bundle b = new Bundle();
//        b.putString("keyword", keyword);
//        b.putString("wangpan", wangpan);
//        f.setArguments(b);
//
//        return f;
//    }
}
