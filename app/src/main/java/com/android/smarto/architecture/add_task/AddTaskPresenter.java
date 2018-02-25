package com.android.smarto.architecture.add_task;

import com.android.smarto.architecture.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 16.02.2018.
 */
public class AddTaskPresenter<V extends IAddTaskActivity> extends BasePresenter<V> {

    @Inject
    public AddTaskPresenter(){}

    public void onCreate(){
        mView.showCommonTypeFragment();
    }

    public void onCommonTaskChoose(){
        mView.showCommonTypeFragment();
    }

    public void onMeetingTaskChoose(){
        mView.showMeetingTypeFragment();
    }

    public void onGoodsTaskChoose(){
        mView.showGoodsTypeFragment();
    }

}
