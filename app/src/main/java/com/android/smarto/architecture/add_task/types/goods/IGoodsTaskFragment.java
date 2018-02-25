package com.android.smarto.architecture.add_task.types.goods;

import com.android.smarto.architecture.base.BaseView;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface IGoodsTaskFragment extends BaseView {

    void showNoGroupToAddTaskError();
    void showEmptyTaskDescriptionError();
    void finishActivity();
    void updateSpinnerItems(List<String> groupNames);
}
