package com.egrcc.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by egrcc on 2/24/15.
 */
public class DownloadActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private ActionBar actionBar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("我的下载");
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.downloadTabs);
        pager = (ViewPager) this.findViewById(R.id.downloadPager);

        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(0);
        tabs.setViewPager(pager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            switch(num) {
                case 0: return DownloadingFragment.newInstance("a", "v");
                case 1: return new DownloadFragment();
                default: return new DownloadFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "下载中";
            } else {
                return "已下载";
            }
        }

    }
}
