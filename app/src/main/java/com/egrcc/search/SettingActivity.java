package com.egrcc.search;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.egrcc.search.widget.FolderSelectorDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;


/**
 * Created by egrcc on 2/24/15.
 */
public class SettingActivity extends ActionBarActivity implements FolderSelectorDialog.FolderSelectCallback {

    private Toolbar mToolbar;
    private ActionBar actionBar;
    private Preference downloadPref;
    private SettingsFragment mSettingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.settings_container, mSettingsFragment).commit();
//            downloadPref = mSettingsFragment.findPreference("downloadFolder");
//            downloadPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    new FolderSelectorDialog().show(SettingActivity.this);
////                    preference.setSummary();
//                    return true;
//                }
//            });
        }

    }

    @Override
    public void onFolderSelection(File folder) {

        downloadPref = mSettingsFragment.findPreference("downloadFolder");
        downloadPref.setSummary("当前为：" + folder.getAbsolutePath());
        SharedPreferences.Editor editor = getSharedPreferences("data",
                MODE_PRIVATE).edit();
        editor.putString("downloadFolder", folder.getAbsolutePath());
        editor.commit();

        Toast.makeText(this, folder.getAbsolutePath(), Toast.LENGTH_SHORT).show();
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

    public static class SettingsFragment extends PreferenceFragment {
        private Handler mHandler;
        final private int UPDATE = 1;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == UPDATE){
                        String jsonData = (String) msg.obj;
                        try {
                            Log.v("version", jsonData);
                            JSONObject jsonObject = new JSONObject(jsonData);
                            String version = jsonObject.getString("version");
                            Log.v("version", version);
                            PackageManager pm = getActivity().getPackageManager();
                            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
                            String versionName = pi.versionName;
                            Log.v("version", versionName);
                            if (version.equals(versionName) == true){
                                Toast.makeText(getActivity(), "已是最新版", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "有更新，请前往各大市场下载", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            };
            Preference myPref = (Preference) findPreference("update");

            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpGet httpGet = new HttpGet("http://xunmiweb.sinaapp.com/version");
                                HttpResponse httpResponse = httpClient.execute(httpGet);
                                if (httpResponse.getStatusLine().getStatusCode() == 200){
                                    HttpEntity httpEntity = httpResponse.getEntity();
                                    String response = EntityUtils.toString(httpEntity, "utf-8");
                                    Message msg = new Message();
                                    msg.what = UPDATE;
                                    msg.obj = response;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    return true;
                }
            });
            Preference downloadPref = findPreference("downloadFolder");
            final SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
            String downloadFolder = pref.getString("downloadFolder", "");
            if (!downloadFolder.equals("")){
                downloadPref.setSummary("当前为：" + downloadFolder);
            }
            downloadPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new FolderSelectorDialog().show(getActivity());
                    return true;
                }
            });

            Preference downloaderPref = findPreference("downloader");
            String downloader = pref.getString("downloader", "");
            if (!downloader.equals("")){
                downloaderPref.setSummary("当前使用" + downloader);
            }
            downloaderPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final Preference downloaderPref2 = preference;
                    new MaterialDialog.Builder(getActivity())
                            .title("选择下载工具")
                            .items(R.array.downloaders)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                    Toast.makeText(getActivity(), which + ": " + text, Toast.LENGTH_SHORT).show();
                                    downloaderPref2.setSummary("当前使用" + text);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("downloader", (String)text);
                                    editor.commit();
                                }
                            })
                            .positiveText("选择")
                            .show();
                    return true;
                }
            });

        }




    }
}
