package com.android.smarto.architecture.add_task.types.common;

import android.text.TextUtils;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.data.DataManager;
import com.android.smarto.data.IDataManager;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public class CommonTaskPresenter<V extends ICommonTaskFragment> extends BasePresenter<V> {

    private IDataManager mDataManager;
    private int mSelectedGroupId;
    private String mDate;
    private String mSelectedDay;
    private String mSelectedTime;

    @Inject
    public CommonTaskPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onAddCommonTaskClicked(String taskDescription){
        if (mDataManager.getTaskManager().getGroupArraySize() == 0) {
            mView.showNoGroupToAddTaskError();
            return;
        }

        if (TextUtils.isEmpty(taskDescription)) {
            mView.showEmptyTaskDescriptionError();
            return;
        }

        int childId = mDataManager.getTaskManager().getChildArraySize(mSelectedGroupId);

        SingleTask singleTask = new SingleTask();
        singleTask.setId(childId);
        singleTask.setTaskText(taskDescription);
        singleTask.setTaskType(Constants.COMMON_TASK_TYPE);
        singleTask.setDate(mDate);

        mDataManager.getTaskManager().addTaskToGroup(singleTask, mSelectedGroupId);
        mView.finishActivity();

    }

    public void onSpinnerItemClick(String itemName){
        mSelectedGroupId = mDataManager.getTaskManager().getGroupIdByName(itemName);
    }

    public void onCheckboxStateChanged(boolean stateChecked){

        if (stateChecked) {
            mView.showDatePickerDialog();
        } else {
            mView.hideDateViews();
        }

    }

    public void onSpinnerSetup(){
        if (mDataManager.getTaskManager().getGroupArraySize() != 0) {
            mView.updateSpinnerItems(mDataManager.getTaskManager().getGroupNames());
        }
    }

    public void onDateSet(int year, int monthOfYear, int dayOfMonth){
        mSelectedDay = monthOfYear + "/" + dayOfMonth + "/" + year;
        mView.showDateView(mSelectedDay);
        mView.showTimePickerDialog();

        mDate = mSelectedDay;
    }

    public void onTimeSet(int hours, int minutes){
        mSelectedTime = hours + ":" + minutes;
        mView.showTimeView(mSelectedTime);
    }

    public void onPositiveGroupDialogClick(String groupName){
        mDataManager.getTaskManager().addGroup(groupName);
        mView.updateSpinnerItems(mDataManager.getTaskManager().getGroupNames());
    }

}
