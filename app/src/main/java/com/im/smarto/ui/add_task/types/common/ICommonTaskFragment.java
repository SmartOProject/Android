package com.im.smarto.ui.add_task.types.common;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface ICommonTaskFragment extends BaseView {

    void showDatePickerDialog();
    void setupCalendar();
    void showEmptyDescriptionError();
    void changeTimeButtonBackground(boolean isActive);
    void showTargetDatePreview(String targetDate, String targetTime);
    void hideTargetDatePreview();
    void showChooseGroupDialog(String[] groupNames);
    void showGroupNamePreview(String groupName);
    void showAddGroupDialog();
    void showTimePickerDialog();
    void finishActivity();

    void setGroupNamePreview(String groupName);
}
