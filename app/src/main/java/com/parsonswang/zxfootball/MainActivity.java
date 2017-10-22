package com.parsonswang.zxfootball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.parsonswang.common.base.BaseActivity;
import com.parsonswang.common.network.JsonCallback;
import com.parsonswang.common.network.OkHttpUtil;
import com.parsonswang.common.utils.BarUtils;
import com.parsonswang.zxfootball.bean.MatchesBean;
import com.parsonswang.zxfootball.data.DataFragment;
import com.parsonswang.zxfootball.matches.MatchesFragment;
import com.parsonswang.zxfootball.price.PriceFragment;
import com.tencent.bugly.crashreport.CrashReport;

import okhttp3.Call;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_MATCHFRAGMENT = "match";
    private static final String TAG_DATAFRAGMENT = "data";
    private static final String TAG_PRICEFRAGMENT = "price";

    private int mCurrMenuItemId;
    private Fragment mCurrFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final int itemId = item.getItemId();
            switch (itemId) {
                case R.id.navigation_match:
                    Timber.i("navigation_match click");
                    switchFragment(itemId, TAG_MATCHFRAGMENT);
                    return true;
                case R.id.navigation_data:
                    Timber.i("navigation_data click");
                    switchFragment(itemId, TAG_DATAFRAGMENT);
                    return true;
                case R.id.navigation_price:
                    Timber.i("navigation_price click");
                    switchFragment(itemId, TAG_PRICEFRAGMENT);
                    return true;
            }
            return false;
        }
    };

    private void switchFragment(int menuItemId, String tag) {
        //如果已经是当前Fragment了也就不用添加了
        if (mCurrMenuItemId == menuItemId) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (transaction == null) {
            return;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        //刚开始就没有该fragment
        if (fragment == null) {
            fragment = createFragment(menuItemId);
            if (fragment == null) {
                return;
            }

            transaction.add(R.id.mFlContent, fragment, tag).commitAllowingStateLoss();
        } else {
            transaction.replace(R.id.mFlContent, fragment, tag).commitAllowingStateLoss();
        }

        mCurrFragment = fragment;
        mCurrMenuItemId = menuItemId;

    }

    private Fragment createFragment(int menuItemId) {
        switch (menuItemId) {
            case R.id.navigation_match:
                Log.i(TAG, "navigation_match createFragment");
                return new MatchesFragment();
            case R.id.navigation_data:
                Log.i(TAG, "navigation_data createFragment");
                return new DataFragment();
            case R.id.navigation_price:
                Log.i(TAG, "navigation_price createFragment");
                return new PriceFragment();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        switchFragment(R.id.navigation_match, TAG_MATCHFRAGMENT);

        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorCommonBackground), 255);

        OkHttpUtil.get().url("http://www.tzuqiu.cc/matches/queryFixture.json")
                .addParams("comeptitionId", "3")
                .addParams("date", "2017.10.01+至+2017.10.31")
                .build().execute(new JsonCallback<MatchesBean>() {
            @Override
            protected void onSucess(MatchesBean matchBean) {
                Timber.i(matchBean.toString());
            }

            @Override
            protected void onFail(Call call, String reson) {
                Timber.e(reson);
            }
        });
    }
}
