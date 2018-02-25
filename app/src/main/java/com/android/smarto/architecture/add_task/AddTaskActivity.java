package com.android.smarto.architecture.add_task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.smarto.R;
import com.android.smarto.architecture.add_task.types.common.CommonTaskFragment;
import com.android.smarto.architecture.add_task.types.goods.GoodsTaskFragment;
import com.android.smarto.architecture.add_task.types.MeetingTaskFragment;
import com.android.smarto.architecture.base.BaseActivity;
import com.jaredrummler.materialspinner.MaterialSpinner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.smarto.Constants.COMMON_TASK;
import static com.android.smarto.Constants.GOODS_TASK;
import static com.android.smarto.Constants.MEETING_TASK;

/**
 * Created by Anatoly Chernyshev on 15.02.2018.
 */

public class AddTaskActivity extends BaseActivity implements IAddTaskActivity{

    @BindView(R.id.task_type_spinner) MaterialSpinner mTypeSpinner;

    @Inject
    AddTaskPresenter<IAddTaskActivity> mAddTaskPresenter;

    FragmentManager mFragmentManager;
    Fragment mCommonFragment;
    Fragment mMeetingFragment;
    Fragment mGoodsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        init();
        setupTypeSpinner();

    }

    private void setupTypeSpinner() {

        final String[] TYPES_LIST = {
                COMMON_TASK,
                MEETING_TASK,
                GOODS_TASK
        };

        mTypeSpinner.setItems(TYPES_LIST);
        mTypeSpinner.setBackground(getDrawable(R.drawable.spinner_background));
        mTypeSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            switch (item.toString()){
                case COMMON_TASK:
                    mAddTaskPresenter.onCommonTaskChoose();
                    break;
                case MEETING_TASK:
                    mAddTaskPresenter.onMeetingTaskChoose();
                    break;
                case GOODS_TASK:
                    mAddTaskPresenter.onGoodsTaskChoose();
                    break;
            }

        });

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

}
