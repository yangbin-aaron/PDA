package com.toughegg.teorderpo.activitys.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.toughegg.andytools.systemUtil.SharePrefenceUtils;
import com.toughegg.andytools.systemUtil.SystemTool;
import com.toughegg.teorderpo.R;
import com.toughegg.teorderpo.TEOrderPoConstans;
import com.toughegg.teorderpo.activitys.BaseActivity;
import com.toughegg.teorderpo.adapters.SelectLangAdapter;
import com.toughegg.teorderpo.modle.bean.Language;
import com.toughegg.teorderpo.view.MyTopActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toughegg on 16/2/26.
 */
public class SelectLanguageActivity extends BaseActivity implements MyTopActionBar.OnTopActionBarClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<Language> mLanguageList;// title_lang语言,sub_lang当前语言翻译,select是否选择,content语言标示
    private SelectLangAdapter mAdapter;
    private List<Language> mSelectLangList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_select_language);
        initTopActionBar ();
        initView ();
    }

    private void initTopActionBar () {
        MyTopActionBar mMyTopActionBar = (MyTopActionBar) findViewById (R.id.top_action_bar);
        mMyTopActionBar.setTitleTextView (R.string.activity_setting_select_lang);
        mMyTopActionBar.setLeftImageView (R.drawable.back_btn);
        mMyTopActionBar.setOnTopActionBarClickListener (this);
    }

    private void initView () {
        initList ();
        mListView = (ListView) findViewById (R.id.select_language_listview);
        mAdapter = new SelectLangAdapter (this, mLanguageList);
        mListView.setAdapter (mAdapter);
        mListView.setOnItemClickListener (this);
    }

    private void initList () {
        mSelectLangList = new ArrayList<> ();
        String langs = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_LAN, "");
        String[] langList = langs.split (",");
        int marCount = 0;// 已经匹配的数量
        mLanguageList = new ArrayList<Language> ();
        Language languageEn = new Language ("English", R.string.app_lang_en, TEOrderPoConstans.LANGUAGE_ENGLISH, false);
        mLanguageList.add (languageEn);
        Language languageZh = new Language ("简体中文", R.string.app_lang_zh, TEOrderPoConstans.LANGUAGE_CHINESE, false);
        mLanguageList.add (languageZh);
        Language languageZhTw = new Language ("繁體中文", R.string.app_lang_zh_tw, TEOrderPoConstans.LANGUAGE_CHINESE_TW, false);
        mLanguageList.add (languageZhTw);
        for (int i = 0; i < mLanguageList.size (); i++) {
            Language language = mLanguageList.get (i);
            for (int j = 0; j < langList.length; j++) {
                if (langList[j].equals (language.getContent ())) {
                    language.setSelect (true);
                    mSelectLangList.add (language);
                    marCount++;
                    break;
                }
            }
            if (marCount >= 2) {
                break;
            }
        }
    }

    @Override
    public void onTopActionBarLeftClicked () {
        toBack ();
    }

    @Override
    public void onTopActionBarRightClicked () {

    }

    /**
     * 返回按钮
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toBack ();
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    private void toBack () {
        if (mSelectLangList.size () == 2) {
            boolean flag = false;
            String lang = SharePrefenceUtils.readString (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN, "");
            for (Language language : mSelectLangList) {
                if (language.getContent ().equals (lang)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                SharePrefenceUtils.write (
                        this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_CHANGE_LAN, mSelectLangList.get (0).getContent ());
                SystemTool.switchLanguage (getApplication (), mSelectLangList.get (0).getContent ());
                sendBroadcast (new Intent (TEOrderPoConstans.ACTION_CHANGE_LANG));
            }
            String langs = mSelectLangList.get (0).getContent () + "," + mSelectLangList.get (1).getContent ();
            SharePrefenceUtils.write (this, TEOrderPoConstans.SHAREPREFERENCE_NAME, TEOrderPoConstans.SP_SELECT_LAN, langs);
        }
        finish ();
        overridePendingTransition (R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        Language language = mLanguageList.get (position);
        if (language.isSelect ()) {
            language.setSelect (false);
            mLanguageList.set (position, language);
            for (int i = 0; i < mSelectLangList.size (); i++) {
                if (language.getContent ().equals (mSelectLangList.get (i).getContent ())) {
                    mSelectLangList.remove (i);
                }
            }
            mAdapter.setLanguageList (mLanguageList);
        } else {
            if (mSelectLangList.size () < 2) {
                language.setSelect (true);
                mLanguageList.set (position, language);
                mSelectLangList.add (language);
                mAdapter.setLanguageList (mLanguageList);
            } else {
                Toast.makeText (SelectLanguageActivity.this, R.string.app_lang_error, Toast.LENGTH_SHORT).show ();
            }
        }
    }
}
