package com.im.smarto.ui.group;

import android.util.Log;

import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 22.03.2018.
 */

public class GroupPresenter<V extends IGroupActivity> extends BasePresenter<V> {

    private static final String TAG = GroupPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private SingleTask mRemovedItem;
    private int mRemovedIndex;
    private int mCurrentGroupPosition;

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

    public void onItemRestored(SingleTask item, int position) {
        mDataManager.taskManager().mData.get(mCurrentGroupPosition).getSingleTaskList().add(mRemovedIndex, mRemovedItem);
        mView.scrollToPosition(position);
    }

    public void onSwiped(int deletedIndex) {
        mRemovedIndex = deletedIndex;
        mView.removeItem(deletedIndex);
        mRemovedItem = mDataManager.taskManager()
                .mData.get(mCurrentGroupPosition).getSingleTaskList().remove(deletedIndex);
        mView.showSnackBar();
    }

    public void update(){
        mView.updateList(mDataManager.taskManager().mData.get(mCurrentGroupPosition).getSingleTaskList());
    }

    public void onRestoreClicked() {
        mView.restoreItem(mRemovedItem, mRemovedIndex);
    }

    public void onAddTaskClicked() {
        mView.showAddTaskActivity(mCurrentGroupPosition);
    }

    public void onResume() {
        update();
    }

    public void onTaskOptionsClicked(SingleTask item) {
        Log.i(TAG, item.getTaskText() + " clicked!");
        mView.showOptionsDialog();
    }

    public void onSendTaskToFriendClicked() {
        Log.i(TAG, "onSendTaskToFriendClicked()");
    }
}
