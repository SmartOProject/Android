package com.im.smarto.ui.group;

import android.util.Log;

import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 22.03.2018.
 */

public class GroupPresenter<V extends IGroupActivity> extends BasePresenter<V> {

    private static final String TAG = GroupPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private SingleTask mRemovedItem;
    private int mCurrentGroupPosition;

    private int mTargetContactId;

    private SingleTask mChosenTask;

    @Inject
    public GroupPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreate(int position) {
        mCurrentGroupPosition = position;
        final TaskGroup item = mDataManager.taskManager().mData.get(position);
        mView.setGroupName(item.getGroupName());
        mView.setupToolbar();
        mView.setupRecyclerView();
        update();

    }

    public void onSwiped(int deletedIndex) {
        mRemovedItem = mDataManager.taskManager()
                .mData.get(mCurrentGroupPosition).getSingleTaskList().remove(deletedIndex);
        update();
        mView.showSnackBar();

        mCompositeDisposable.add(
                mDataManager
                .networkHelper()
                .deleteTask((int)mRemovedItem.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rows -> Log.i(TAG, rows.getRowsAffected() + "rows affected"),
                        error -> Log.i(TAG, error.getMessage())));

    }

    public void update(){
        mView.updateList(mDataManager.taskManager().mData.get(mCurrentGroupPosition).getSingleTaskList());
    }

    public void onRestoreClicked() {
        final TaskGroup group = mDataManager.taskManager().mData.get(mCurrentGroupPosition);

        if (mRemovedItem.getDate() == null) {
            mCompositeDisposable.add(
                    mDataManager
                    .networkHelper()
                    .insertTask((int)group.getId(), mRemovedItem.getTaskType(), mRemovedItem.getTaskText())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        mDataManager
                                .taskManager()
                                .mData
                                .get(mCurrentGroupPosition)
                                .getSingleTaskList()
                                .add(new SingleTask(success.getId(), mRemovedItem.getTaskType(),
                                        mRemovedItem.getTaskText()));
                        update();
                    }));
        } else {
            mCompositeDisposable.add(
                    mDataManager
                    .networkHelper()
                    .insertTask((int)group.getId(), mRemovedItem.getTaskType(),
                            mRemovedItem.getTaskText(), mRemovedItem.getDate())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(success -> {
                        mDataManager
                                .taskManager()
                                .mData
                                .get(mCurrentGroupPosition)
                                .getSingleTaskList()
                                .add(new SingleTask(success.getId(), mRemovedItem.getTaskType(),
                                        mRemovedItem.getTaskText(), mRemovedItem.getDate()));
                        update();
                    }));
        }
    }

    public void onAddTaskClicked() {
        mView.showAddTaskActivity(mCurrentGroupPosition);
    }

    public void onResume() {
        update();
    }

    public void onTaskOptionsClicked(SingleTask item) {
        Log.i(TAG, item.getTaskText() + " clicked!");
        mChosenTask = item;
        mView.showOptionsDialog();
    }

    public void onSendTaskToFriendClicked() {
        Log.i(TAG, "onSendTaskToFriendClicked()");
        mView.dismissOptionsDialog();
        mView.showContactListDialog(mDataManager.userManager().getContactNames());
    }

    public void onSingleContactChoiceClicked(String contactName) {
        mTargetContactId = mDataManager.userManager().getId(contactName);
    }

    public void onPositiveButtonClicked() {
        mCompositeDisposable.add(
                mDataManager
                .networkHelper()
                .changeTask((int) mChosenTask.getId(), mTargetContactId)
                .subscribeOn(Schedulers.io())
                .subscribe(rowsAffectedResponse -> Log.i(TAG, rowsAffectedResponse.toString()),
                        error -> {
                            Log.i(TAG, error.getMessage());
                            mView.showErrorToast(error.getMessage());
                        }));
    }
}
