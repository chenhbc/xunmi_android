package com.egrcc.search;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.egrcc.search.utils.GfsosoSearchUtil;
import com.egrcc.search.utils.VdiskSearchUtil;
import com.egrcc.search.utils.WangpanUtils;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.HashMap;


public class ResultFragment extends Fragment {


    private Handler dataHandler;
    private Handler moreDataHandler;
    private ArrayList<HashMap<String, Object>> list;
    private String wangpan;
    private String keyword;
    private View moreView;
    private View searchTitle;
    private Button loadmoreButton;
    private ProgressBarCircularIndeterminate loadProgressBar;
    private ListView listView;
    private ProgressBarCircularIndeterminate mainProgressBar;
    private SimpleAdapter mSimpleAdapter;
    private int count = 2;
    private int searchCount = 0;
    private int[] imageId = new int[]{
            R.drawable.folder, R.drawable.video, R.drawable.music, R.drawable.pdf, R.drawable.picture,
            R.drawable.ppt, R.drawable.word, R.drawable.excel, R.drawable.unknown, R.drawable.text};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        listView = (ListView) v.findViewById(R.id.listView);
        mainProgressBar = (ProgressBarCircularIndeterminate) v.findViewById(R.id.mainProgressBar);
        moreView = inflater.inflate(R.layout.moredata, null);
        loadmoreButton = (Button) moreView.findViewById(R.id.loadmoreButton);
        loadProgressBar = (ProgressBarCircularIndeterminate) moreView.findViewById(R.id.loadProgressBar);
        searchTitle = inflater.inflate(R.layout.listview_header, null);
        wangpan = getArguments().getString("wangpan");
        keyword = getArguments().getString("keyword");
        mainProgressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        dataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {


                if (msg.what == 0x1233 || msg.what == 0x3455 || msg.what == 0x5677) {

                    list = new ArrayList<HashMap<String, Object>>();
                    ArrayList<String> resultUrl = new ArrayList<String>();
                    ArrayList<String> resultContent = new ArrayList<String>();
                    resultUrl = msg.getData().getStringArrayList("RESULTURL");
                    resultContent = msg.getData().getStringArrayList("RESULTCONTENT");
                    for (int i = 0; i < resultUrl.size(); i++) {
                        Log.v("debug", resultUrl.get(i));
                        Log.v("debug", resultContent.get(i));
                    }

                    if (resultUrl.size() == 0) {
                        String W = new String();
                        switch (wangpan) {
                            case "BAIDUYUN":
                                W = "百度云";
                                break;
                            case "WEIYUN":
                                W = "微盘";
                                break;
                            case "HUAWEI":
                                W = "华为网盘";
                                break;
                        }
                        Toast.makeText(getActivity(), W + "没有找到资源！", Toast.LENGTH_LONG).show();
                        mainProgressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < resultUrl.size(); i++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemTitle", resultContent.get(i));
                            map.put("ItemText", resultUrl.get(i));
                            map.put("Image", imageId[i % 10]);
//                            if (resultContent.get(i).contains("pdf_")) {
//                                map.put("Image", imageId[3]);
//                            } else if (resultContent.get(i).endsWith("的分享") ||
//                                    resultContent.get(i).contains("等_")) {
//                                map.put("Image", imageId[0]);
//                            } else if (resultContent.get(i).contains("mkv") ||
//                                    resultContent.get(i).contains("mp4") ||
//                                    resultContent.get(i).contains("flv") ||
//                                    resultContent.get(i).contains("rmvb")) {
//                                map.put("Image", imageId[1]);
//                            } else if (resultContent.get(i).contains("doc_") ||
//                                    resultContent.get(i).contains("docx_")) {
//                                map.put("Image", imageId[5]);
//                            } else {
//                                map.put("Image", imageId[7]);
//                            }

                            list.add(map);
                        }
                        mSimpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.listview_item,
                                new String[]{"ItemTitle", "ItemText", "Image"},
                                new int[]{R.id.tv_title, R.id.tv_content, R.id.fileImage});


                        if (listView.getFooterViewsCount() == 0) {
                            listView.addFooterView(moreView);
                        }
                        if (listView.getHeaderViewsCount() > 0) {
                            listView.removeHeaderView(searchTitle);
                        }
                        if (listView.getHeaderViewsCount() == 0 && wangpan.equals("WEIYUN") == false) {
                            listView.addHeaderView(searchTitle, null, false);
                        }
                        listView.setAdapter(mSimpleAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {

                                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                                intent.setAction("android.intent.action.VIEW");
//                                Uri url;
//
                                if (wangpan.equals("WEIYUN") == false) {
//                                    url = Uri.parse((String) (list.get(position - 1).get("ItemText")));
                                    intent.putExtra("url", (String) (list.get(position - 1).get("ItemText")));
                                    intent.putExtra("title", (String) (list.get(position - 1).get("ItemTitle")));
                                } else {
//                                    url = Uri.parse((String) (list.get(position).get("ItemText")));
                                    intent.putExtra("url", (String) (list.get(position).get("ItemText")));
                                    intent.putExtra("title", (String) (list.get(position).get("ItemTitle")));
                                }
//                                intent.setData(url);

                                startActivity(intent);

                            }

                        });
                        mainProgressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(
                                    AdapterView<?> parent, View view,
                                    int position, long id) {

                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                if (wangpan != "WEIYUN") {
                                    clipboard.setPrimaryClip(ClipData.newPlainText("data", (String) (list.get(position - 1).get("ItemText"))));
                                } else {
                                    clipboard.setPrimaryClip(ClipData.newPlainText("data", (String) (list.get(position).get("ItemText"))));
                                }
                                Toast.makeText(getActivity(), "链接已复制！", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                        });


                        loadmoreButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                v.setVisibility(View.GONE);
                                loadProgressBar.setVisibility(View.VISIBLE);
//                                Message moreDataMsg = new Message();
//                                if (wangpan == "BAIDUYUN") {
//                                    moreDataMsg.what = 0x234;
//                                } else if (wangpan == "WEIYUN") {
//                                    moreDataMsg.what = 0x456;
////            	        			System.out.println("button2");
//                                } else if (wangpan == "HUAWEI") {
//                                    moreDataMsg.what = 0x678;
//                                }
//                                moreDataMsg.setData(bundle);
//                                downloadThread.mHandler.sendMessage(moreDataMsg);
                                moreDataHandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what == 0x2344 || msg.what == 0x4566 || msg.what == 0x6788) {
                                            ArrayList<String> moreResultUrl = new ArrayList<String>();
                                            ArrayList<String> moreResultContent = new ArrayList<String>();
                                            moreResultUrl = msg.getData().getStringArrayList("RESULTURL");
                                            moreResultContent = msg.getData().getStringArrayList("RESULTCONTENT");
                                            if (moreResultUrl.size() == 0) {
                                                Toast.makeText(getActivity(), "没有更多了！", Toast.LENGTH_LONG).show();
                                                loadmoreButton.setVisibility(View.GONE);
                                                loadProgressBar.setVisibility(View.GONE);
                                            } else {
                                                loadMoreData(moreResultUrl, moreResultContent);
                                                loadmoreButton.setVisibility(View.VISIBLE);
                                                loadProgressBar.setVisibility(View.GONE);
                                                mSimpleAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                };
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        ArrayList<String> resultUrl;
                                        ArrayList<String> resultContent;
                                        if (wangpan == "BAIDUYUN") {
                                            String[] keywords = keyword.split(" ");

                                            String html = GfsosoSearchUtil.getHtml(keywords, count, WangpanUtils.BAIDU_YUN);
                                            resultUrl = GfsosoSearchUtil.getResultUrl(html);
                                            resultContent = GfsosoSearchUtil.getResultContent(html);
                                            System.out.println("GfsosoSearch");

                                            Bundle resultBundle = new Bundle();
                                            resultBundle.putStringArrayList("RESULTURL", resultUrl);
                                            resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                                            Message msgData = new Message();
                                            msgData.what = 0x2344;
                                            msgData.setData(resultBundle);
                                            moreDataHandler.sendMessage(msgData);
                                            count++;
                                        } else if (wangpan == "WEIYUN") {
                                            String[] keywords = keyword.split(" ");
                                            String html = VdiskSearchUtil.getHtml(keywords, count);
                                            resultUrl = VdiskSearchUtil.getResultUrl(html);
                                            resultContent = VdiskSearchUtil.getResultContent(html);
                                            Bundle resultBundle = new Bundle();
                                            resultBundle.putStringArrayList("RESULTURL", resultUrl);
                                            resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                                            Message msgData = new Message();
                                            msgData.what = 0x4566;
                                            msgData.setData(resultBundle);
                                            moreDataHandler.sendMessage(msgData);
                                            count++;
                                        } else if (wangpan == "HUAWEI") {
                                            String[] keywords = keyword.split(" ");
                                            String html = GfsosoSearchUtil.getHtml(keywords, count, WangpanUtils.DBANK);
                                            resultUrl = GfsosoSearchUtil.getResultUrl(html);
                                            resultContent = GfsosoSearchUtil.getResultContent(html);
                                            Bundle resultBundle = new Bundle();
                                            resultBundle.putStringArrayList("RESULTURL", resultUrl);
                                            resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                                            Message msgData = new Message();
                                            msgData.what = 0x6788;
                                            msgData.setData(resultBundle);
                                            moreDataHandler.sendMessage(msgData);
                                            count++;
                                        }
                                    }
                                }).start();
                            }
                        });
                    }

                }

            }
        };
        new DownloadThread().start();
        searchCount++;
        return v;
    }

    public static ResultFragment newInstance(String wangpan, String keyword) {

        ResultFragment f = new ResultFragment();
        Bundle b = new Bundle();
        b.putString("keyword", keyword);
        b.putString("wangpan", wangpan);
        f.setArguments(b);

        return f;
    }

    class DownloadThread extends Thread {


        private ArrayList<String> resultUrl;
        private ArrayList<String> resultContent;

        @Override
        public void run() {
            if (wangpan == "BAIDUYUN") {
                count = 2;
                String[] keywords = keyword.split(" ");
                Log.v("dea", "sdff");
                String html = GfsosoSearchUtil.getHtml(keywords, 1, WangpanUtils.BAIDU_YUN);
                resultUrl = GfsosoSearchUtil.getResultUrl(html);
                resultContent = GfsosoSearchUtil.getResultContent(html);
                System.out.println("GfsosoSearch");

                Bundle resultBundle = new Bundle();
                resultBundle.putStringArrayList("RESULTURL", resultUrl);
                resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                Message msgData = new Message();
                msgData.what = 0x1233;
                msgData.setData(resultBundle);
                dataHandler.sendMessage(msgData);
            } else if (wangpan == "WEIYUN") {
                count = 2;
                String[] keywords = keyword.split(" ");
                String html = VdiskSearchUtil.getHtml(keywords, 1);
                resultUrl = VdiskSearchUtil.getResultUrl(html);
                resultContent = VdiskSearchUtil.getResultContent(html);
                Bundle resultBundle = new Bundle();
                resultBundle.putStringArrayList("RESULTURL", resultUrl);
                resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                Message msgData = new Message();
                msgData.what = 0x3455;
                msgData.setData(resultBundle);
                dataHandler.sendMessage(msgData);
            } else if (wangpan == "HUAWEI") {
                count = 2;
                String[] keywords = keyword.split(" ");
                String html = GfsosoSearchUtil.getHtml(keywords, 1, WangpanUtils.DBANK);
                resultUrl = GfsosoSearchUtil.getResultUrl(html);
                resultContent = GfsosoSearchUtil.getResultContent(html);
                Bundle resultBundle = new Bundle();
                resultBundle.putStringArrayList("RESULTURL", resultUrl);
                resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                Message msgData = new Message();
                msgData.what = 0x5677;
                msgData.setData(resultBundle);
                dataHandler.sendMessage(msgData);
            }

        }

    }

    private void loadMoreData(ArrayList<String> moreResultUrl, ArrayList<String> moreResultContent) {
        for (int i = 0; i < moreResultUrl.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", moreResultContent.get(i));
            map.put("ItemText", moreResultUrl.get(i));
            map.put("Image", imageId[i % 10]);
//            if (moreResultContent.get(i).contains("pdf_")) {
//                map.put("Image", imageId[3]);
//            } else if (moreResultContent.get(i).endsWith("的分享") ||
//                    moreResultContent.get(i).contains("等_")) {
//                map.put("Image", imageId[0]);
//            } else if (moreResultContent.get(i).contains("mkv") ||
//                    moreResultContent.get(i).contains("mp4") ||
//                    moreResultContent.get(i).contains("flv") ||
//                    moreResultContent.get(i).contains("rmvb")) {
//                map.put("Image", imageId[1]);
//            } else if (moreResultContent.get(i).contains("doc_") ||
//                    moreResultContent.get(i).contains("docx_")) {
//                map.put("Image", imageId[5]);
//            } else {
//                map.put("Image", imageId[7]);
//            }
            list.add(map);
        }
    }

//    private void search() {

//
////        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////        View view = getActivity().getCurrentFocus();
////        if (view != null) {
////            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
////        }

//    }
}
