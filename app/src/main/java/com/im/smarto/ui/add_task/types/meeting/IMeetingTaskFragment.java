package com.im.smarto.ui.add_task.types.meeting;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 15.03.2018.
 */

public interface IMeetingTaskFragment extends BaseView {

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

    void showChooseContactDialog(String[] contactNames);

    void changeContactButtonBackground(boolean b);

    void hideTargetContactPreview();

    void showTargetContactPreview(String contactName);
}
