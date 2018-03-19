package com.im.smarto.ui.add_task;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.im.smarto.R;
import com.im.smarto.ui.add_task.types.common.CommonTaskFragment;
import com.im.smarto.ui.add_task.types.goods.GoodsTaskFragment;
import com.im.smarto.ui.add_task.types.meeting.MeetingTaskFragment;
import com.im.smarto.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Anatoly Chernyshev on 15.02.2018.
 */

public class AddTaskActivity extends BaseActivity implements IAddTaskActivity, MaterialTabListener {

    @BindView(R.id.tabHost) MaterialTabHost mTabHost;
    @BindView(R.id.add_task_view_pager) ViewPager mViewPager;

    @Inject
    AddTaskPresenter<IAddTaskActivity> mAddTaskPresenter;

    private ViewPagerAdapter mViewPagerAdapter;

    FragmentManager mFragmentManager;
    Fragment mCommonFragment;
    Fragment mMeetingFragment;
    Fragment mGoodsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        init();
        viewPager();

    }

    private void viewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }
    }

    private void init() {
        ButterKnife.bind(this);
        mAddTaskPresenter.onAttach(this);

        mFragmentManager = getSupportFragmentManager();
        mCommonFragment = new CommonTaskFragment();
        mMeetingFragment = new MeetingTaskFragment();
        mGoodsFragment = new GoodsTaskFragment();

        mAddTaskPresenter.onCreate();

    }

    private FragmentTransaction createFragmentTransaction(){
        return mFragmentManager.beginTransaction();
    }

    @Override
    public void showCommonTypeFragment() {
        createFragmentTransaction()
                .replace(R.id.task_variable_content, mCommonFragment, CommonTaskFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showMeetingTypeFragment() {
        createFragmentTransaction()
                .replace(R.id.task_variable_content, mMeetingFragment, MeetingTaskFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void showGoodsTypeFragment() {
        createFragmentTransaction()
                .replace(R.id.task_variable_content, mGoodsFragment, GoodsTaskFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddTaskPresenter.onDetach();
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int num) {

            Fragment fragment = null;
            switch (num) {
                case 0:
                    fragment = new CommonTaskFragment();
                    break;
                case 1:
                    fragment = new GoodsTaskFragment();
                    break;
                case 2:
                    fragment = new MeetingTaskFragment();
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "tab 1";
                case 1: return "tab 2";
                case 2: return "tab 3";
                default: return null;
            }
        }
    }

    private Drawable getIcon(int position) {
        switch(position) {
            case 0:
                return getResources().getDrawable(R.drawable.ic_query_builder_black_24dp);
            case 1:
                return getResources().getDrawable(R.drawable.ic_kitchen_black_24dp);
            case 2:
                return getResources().getDrawable(R.drawable.ic_accessibility_black_24dp);
        }
        return null;
    }

}
