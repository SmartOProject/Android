package com.android.smarto.architecture.add_task.types.common;

import com.android.smarto.architecture.base.BaseView;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface ICommonTaskFragment extends BaseView {

    void showNoGroupToAddTaskError();
    void showEmptyTaskDescriptionError();
    void showDatePickerDialog();
    void showTimePickerDialog();
    void hideDateViews();
    void updateSpinnerItems(List<String> groupNames);
    void showDateView(String date);
    void showTimeView(String time);
    void finishActivity();

}
