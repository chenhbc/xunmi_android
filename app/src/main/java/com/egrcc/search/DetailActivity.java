package com.egrcc.search;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.egrcc.search.utils.MyDatabaseHelper;
import com.gc.materialdesign.views.ProgressBarDeterminate;

import java.net.URLDecoder;


public class DetailActivity extends ActionBarActivity implements DownloadListener {
    private WebView mWebView;
    private Toolbar mToolbar;
    private ActionBar actionBar;
    private String downloadUrl;
    private MyDatabaseHelper dbHelper;
    private ProgressBarDeterminate mProgressBarDeterminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getIntent().getStringExtra("title");
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mProgressBarDeterminate = (ProgressBarDeterminate)findViewById(R.id.progressDeterminate);
        mWebView = (WebView) findViewById(R.id.mWebView);
        String url = getIntent().getStringExtra("url");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress) {

                if(progress < 100 && mProgressBarDeterminate.getVisibility() == ProgressBar.GONE){
                    mProgressBarDeterminate.setVisibility(ProgressBar.VISIBLE);
                }
                mProgressBarDeterminate.setProgress(progress);
                if(progress == 100) {
                    mProgressBarDeterminate.setVisibility(ProgressBar.GONE);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setDownloadListener(this);
        mWebView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(this, SearchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        downloadUrl = url;
        String fileName= URLUtil.guessFileName(url, contentDisposition, mimetype);
        try {
            fileName = URLDecoder.decode(fileName, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Applog", "fileName:" + fileName);
        Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        Log.d("Applog", Long.toString(contentLength));

        dbHelper = new MyDatabaseHelper(this, "DownloadStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("size", contentLength);
//        values.put("name", fileName);
//        db.insert("Download", null, values);
        db.execSQL("insert into Download (size, name, created_date) values(?, ?, ?)",
                new String[] { Long.toString(contentLength), fileName, "datetime()" });

        new MaterialDialog.Builder(this)
                .title("是否下载？")
                .content("可在设置界面设置下载目录和下载工具。")
                .positiveText("下载")
                .negativeText("取消")
                .positiveColorRes(R.color.material_blue)
                .negativeColorRes(R.color.material_blue)
                .contentColorRes(R.color.material_grey)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Uri uri = Uri.parse(downloadUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                })
                .show();



    }
}
