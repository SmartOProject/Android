package com.android.smarto.architecture.add_task;

import com.android.smarto.architecture.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 16.02.2018.
 */

public interface IAddTaskActivity extends BaseView {

    void showCommonTypeFragment();
    void showMeetingTypeFragment();
    void showGoodsTypeFragment();

}
