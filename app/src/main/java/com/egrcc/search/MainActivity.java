package com.egrcc.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, SearchView.OnQueryTextListener, ShareActionProvider.OnShareTargetSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    private Toolbar mToolbar;
    private String[] mPlanetTitles;
    private ListView mDrawerList;
    private EditText editText;
    private LinearLayout focusView;
    private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();;
    private SimpleAdapter mSimpleAdapter;
    private int[] imageId = new int[]{R.drawable.home, R.drawable.download, R.drawable.share,R.drawable.donate,  R.drawable.settings};
    private String[] textId = new String[]{"主页", "下载", "分享", "打赏", "设置"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

//                    case R.id.setting:
//                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
//                        startActivity(settingIntent);
//                        break;
//                    case R.id.donate:
//                        Intent donateIntent = new Intent(MainActivity.this, DonateActivity.class);
//                        donateIntent.putExtra("from", "main");
//                        startActivity(donateIntent);
//                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		/* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText", textId[i]);
            map.put("Image", imageId[i]);
            list.add(map);
        }
        mSimpleAdapter = new SimpleAdapter(this, list, R.layout.drawer_list_item,
                new String[]{"ItemText", "Image"},
                new int[]{R.id.drawerText, R.id.drawerImage});

//        if (listView.getHeaderViewsCount() > 0) {
//            listView.removeHeaderView(searchTitle);
//        }
//        if (listView.getHeaderViewsCount() == 0 && wangpan.equals("WEIYUN") == false) {
//            listView.addHeaderView(searchTitle, null, false);
//        }
        mDrawerList.setAdapter(mSimpleAdapter);

        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        focusView = (LinearLayout)findViewById(R.id.focusView);
        focusView.setFocusable(true);
        focusView.setFocusableInTouchMode(true);
        focusView.requestFocus();
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e) {
            // presumably, not relevant
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.searchView).getActionView();
        searchView.setOnQueryTextListener(MainActivity.this);
        searchView.setSubmitButtonEnabled(true);
//        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        mShareActionProvider.setShareHistoryFileName(null);
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Test Text");
//        sendIntent.setType("text/plain");
//
//        setShareIntent(sendIntent);
//        mShareActionProvider.setOnShareTargetSelectedListener(this);

        return true;
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        // start activity ourself to prevent search history
        startActivity(intent);
        return false;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        editText = (EditText) findViewById(R.id.editText);
        String keyword = editText.getText().toString();
        intent.putExtra("keyword", keyword);
        startActivity(intent);
        Log.v("debug", "aaaaaaaa");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("keyword", query);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .content("确定退出吗？")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        MainActivity.this.finish();
                    }

                })
                .show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(downloadIntent);
                    break;
                case 3:
                    mDrawerLayout.closeDrawers();
                    Intent donateIntent = new Intent(MainActivity.this, DonateActivity.class);
                    donateIntent.putExtra("from", "main");
                    startActivity(donateIntent);
                    break;
                case 2:
                    mDrawerLayout.closeDrawers();
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Test Text");
//                    ComponentName comp = new ComponentName("com.tencent.mm",
//                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                    sendIntent.setComponent(comp);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
//                    Intent intent = new Intent();
//                    ComponentName comp = new ComponentName("com.tencent.mm",
//                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                    intent.setComponent(comp);
//                    intent.setAction("android.intent.action.SEND");
//                    intent.setType("image/*");
//                    intent.putExtra(Intent.EXTRA_TEXT,"我是文字");
//                    File file = new File("searchlogo3.png");
//                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//                    startActivity(intent);
                    break;
                case 4:
                    mDrawerLayout.closeDrawers();
                    Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(settingIntent);
                    break;
                default:
                    break;
            }
        }
    }

}
