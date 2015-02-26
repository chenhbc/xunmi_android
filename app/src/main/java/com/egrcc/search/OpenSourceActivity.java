package com.egrcc.search;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by egrcc on 2/25/15.
 */
public class OpenSourceActivity extends ActionBarActivity {

    private ActionBar actionBar;
    private Toolbar mToolbar;
    private TextView license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("开源许可");
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        license = (TextView) findViewById(R.id.license);
        AssetManager mgr=getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/SourceCodePro-Regular.ttf");//根据路径得到Typeface
        license.setTypeface(tf);//设置字体
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(this, SettingActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            finish();
        }
        return true;
    }
}
