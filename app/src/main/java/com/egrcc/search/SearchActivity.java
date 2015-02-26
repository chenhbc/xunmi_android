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



public class SearchActivity extends ActionBarActivity {

    private ViewPager pager;
    private ActionBar actionBar;
    private Toolbar mToolbar;
    private ViewPagerAdapter adapter;
    private String keyword;
    private PagerSlidingTabStrip tabs;
    private ResultFragment baiduyunFragment;
    private ResultFragment weiyunFragment;
    private ResultFragment huaweiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("搜索结果");
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabs.setViewPager(pager);
//        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // when user do a swipe the selected tab change
//                tabHost.setSelectedNavigationItem(position);
//                Log.v("debug", String.valueOf(position));
//
//            }
//        });

        // insert all tabs from pagerAdapter data
//        for (int i = 0; i < adapter.getCount(); i++) {
//            tabHost.addTab(
//                    tabHost.newTab()
//                            .setText(adapter.getPageTitle(i))
//                            .setTabListener(this)
//            );
//
//        }
        keyword = getIntent().getStringExtra("keyword");
        baiduyunFragment = ResultFragment.newInstance("BAIDUYUN", keyword);
        weiyunFragment = ResultFragment.newInstance("WEIYUN", keyword);
        huaweiFragment = ResultFragment.newInstance("HUAWEI", keyword);
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
                case 0: return baiduyunFragment;
                case 1: return weiyunFragment;
                case 2: return huaweiFragment;
                default: return ResultFragment.newInstance("Default", keyword);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "百度云";
            }else if (position == 1) {
                return "微盘";
            }
            return "华为网盘";
        }

    }
}
