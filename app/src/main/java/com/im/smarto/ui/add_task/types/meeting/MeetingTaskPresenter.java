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
import com.im.smarto.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 15.03.2018.
 */

public class MeetingTaskPresenter<V extends IMeetingTaskFragment> extends BasePresenter<V> {

    private static final String TAG = MeetingTaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private String mTargetDate;
    private String mTargetTime;
    private String mTargetContact;
    private String mTargetGroup;
    private int mCurrentGroupPosition;
    private TaskGroup mCurrentGroup;
    private boolean mIsActiveTimeButton = false;
    private boolean mIsActiveContactButton = false;

    private boolean isDateSet = false;
    private boolean isTimeSet = false;
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;
    private int seconds;

    @Inject
    public MeetingTaskPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreate(int groupPosition) {
        Log.i(TAG, groupPosition + " | " + mDataManager.taskManager().mData.get(groupPosition).getGroupName());
        mCurrentGroupPosition = groupPosition;
        mCurrentGroup = mDataManager.taskManager().mData.get(groupPosition);
        mView.setGroupNamePreview(mCurrentGroup.getGroupName());
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
        Log.i(TAG, "Year: " + year + "\n"
                + "Month: " + monthOfYear + "\n"
                + "Day: " + dayOfMonth);
        this.isDateSet = true;
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        mView.showTimePickerDialog();
    }

    public void onTimeSet(int hourOfDay, int minute) {
        Log.i(TAG, "Hour: " + hourOfDay + "\n"
                + "Minute: " + minute);

        this.isTimeSet = true;
        this.hours = hourOfDay;
        this.minutes = minute;

        mIsActiveTimeButton = true;
        mView.changeTimeButtonBackground(true);
        mView.showTargetDatePreview(DateUtils.convertToISOFormat(year, month, day),
                DateUtils.convertToISOFormat(hours, minutes));
    }

    public void onSingleChoiceClicked(String groupName) {
        mTargetGroup = groupName;
        mView.showGroupNamePreview(mTargetGroup);
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

        if (mCurrentGroup == null) return;

        int userId;
        if (mTargetContact == null) userId = 0;
        else userId = mDataManager.userManager().getId(mTargetContact);

        int groupId = (int) mCurrentGroup.getId();
        int groupPosition = mCurrentGroupPosition;
        Log.i(TAG, groupPosition + "");
        int type = Constants.MEETING_TASK_TYPE;
        int orderNum = mCurrentGroup.getSingleTaskList().size();

        if (!isDateSet || !isTimeSet) {
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

        String date = DateUtils.convertToISOFormat(year, month, day) + " "
                + DateUtils.convertToISOFormat(hours, minutes) + ":00";

        mDataManager.networkHelper().insertTask(groupId, userId, type, description, date, orderNum)
                .subscribeOn(Schedulers.io())
                .subscribe(success -> {
                            mDataManager.taskManager()
                                    .mData.get(groupPosition)
                                    .getSingleTaskList()
                                    .add(new SingleTask(success.getId(),
                                            userId,
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
