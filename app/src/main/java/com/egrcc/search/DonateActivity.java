package com.egrcc.search;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by egrcc on 2/24/15.
 */
public class DonateActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("打赏作者");
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Intent intent;
//            try {
//                if (getIntent().getStringExtra("from").equals("main") == true ) {
//                    intent = new Intent(this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
//            }
//            catch (Exception e) {
//                System.out.println("遭遇了一个异常！");
//                intent = new Intent(this, SettingActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
            finish();

        }
        return true;
    }


}
