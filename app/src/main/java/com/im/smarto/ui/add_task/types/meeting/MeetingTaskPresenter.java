package com.im.smarto.ui.add_task.types.meeting;

import android.text.TextUtils;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.data.DataManager;
import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.add_task.types.common.CommonTaskPresenter;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 15.03.2018.
 */

public class MeetingTaskPresenter<V extends IMeetingTaskFragment> extends BasePresenter<V> {

    private static final String TAG = CommonTaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private String mTargetDate;
    private String mTargetTime;
    private String mTargetContact;
    private String mTargetGroup;
    private boolean mIsActiveTimeButton = false;
    private boolean mIsActiveContactButton = false;

    @Inject
    public MeetingTaskPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreate() {
        mView.setupCalendar();
    }

    public void onTimeButtonClicked() {
        if (mIsActiveTimeButton) {
            mView.changeTimeButtonBackground(false);
            mIsActiveTimeButton = false;
            mView.hideTargetDatePreview();
        }
        else mView.showDatePickerDialog();
    }

    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {
        String month;
        if (monthOfYear < 10) month = "0" + monthOfYear;
        else month = String.valueOf(monthOfYear);
        mTargetDate = dayOfMonth + "/" + month + "/" + year;
        mView.showTimePickerDialog();
    }

    public void onTimeSet(int hourOfDay, int minute) {
        String time;

        if (hourOfDay < 10) time = "0" + String.valueOf(hourOfDay);
        else time = String.valueOf(hourOfDay);

        if(minute < 10) time = time + ":" + "0" + minute;
        else time = time + ":" + minute;

        mTargetTime = time;
        mIsActiveTimeButton = true;
        mView.changeTimeButtonBackground(true);
        mView.showTargetDatePreview(mTargetDate, mTargetTime);
    }

    public void chooseGroupButtonClicked() {
        List<String> groupNames = mDataManager.taskManager().getGroupNames();
        mView.showChooseGroupDialog(groupNames.toArray(new String[groupNames.size()]));
    }

    public void onSingleChoiceClicked(String groupName) {
        mTargetGroup = groupName;
        mView.showGroupNamePreview(mTargetGroup);
    }

    public void addGroupButtonClicked() {
        mView.showAddGroupDialog();
    }

    public void onDialogAddGroupClicked(String groupName) {
        mTargetGroup = groupName;
        mView.showGroupNamePreview(groupName);

        int orderNum = mDataManager.taskManager().mData.size();

        mDataManager.networkHelper().insertGroup(groupName, orderNum)
                .subscribeOn(Schedulers.io())
                .subscribe(success -> {
                            Log.i(TAG, "insertGroup success!");
                            mDataManager.taskManager().mData.add(new TaskGroup(success.getId(), groupName));
                        },
                        error -> Log.i(TAG, error.getMessage()));
    }

    public void onAddMeetingTaskClicked(String description) {

        if (TextUtils.isEmpty(description)) {
            mView.showEmptyDescriptionError();
            return;
        }

        if (mTargetGroup == null) return;

        int userId;
        if (mTargetContact == null) userId = 0;
        else userId = mDataManager.userManager().getId(mTargetContact);

        int groupId = mDataManager.taskManager().getGroupId(mTargetGroup);
        int groupPosition = mDataManager.taskManager().getGroupPosition(mTargetGroup);
        Log.i(TAG, groupPosition + "");
        int type = Constants.MEETING_TASK_TYPE;
        int orderNum = mDataManager.taskManager().mData.get(groupPosition).getSingleTaskList().size();

        if (mTargetTime == null || mTargetDate == null) {
            mDataManager.networkHelper().insertTask(groupId, userId, type, description, orderNum)
                    .subscribeOn(Schedulers.io())
                    .subscribe(success -> {
                                mDataManager.taskManager()
                                        .mData.get(groupPosition)
                                        .getSingleTaskList()
                                        .add(new SingleTask(success.getId(),
                                                userId,
                                                type, description));
                                Log.i(TAG, "insertTask success!");
                                mView.finishActivity();
                            },
                            error -> Log.i(TAG, error.getMessage()));
            return;
        }

        String date = mTargetDate + " " + mTargetTime;

        mDataManager.networkHelper().insertTask(groupId, userId, type, description, date, orderNum)
                .subscribeOn(Schedulers.io())
                .subscribe(success -> {
                            mDataManager.taskManager()
                                    .mData.get(groupPosition)
                                    .getSingleTaskList()
                                    .add(new SingleTask(success.getId(),
                                            mDataManager.userManager().getId(mTargetContact),
                                            type, description, date));
                            Log.i(TAG, "insertTask success!");
                            mView.finishActivity();
                        },
                        error -> Log.i(TAG, error.getMessage()));
    }

    public void chooseContactButtonClicked() {
        if (mIsActiveContactButton) {
            mView.changeContactButtonBackground(false);
            mIsActiveContactButton = false;
            mView.hideTargetContactPreview();
        }
        else mView.showChooseContactDialog(mDataManager.userManager().getContactNames());
    }

    public void onSingleContactChoiceClicked(String contactName) {
        mTargetContact = contactName;
        mIsActiveContactButton = true;
        mView.changeContactButtonBackground(true);
        mView.showTargetContactPreview(contactName);
    }
}
