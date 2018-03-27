package com.im.smarto.ui.task;

import android.text.TextUtils;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.network.models.GetGroupResponse;
import com.im.smarto.network.models.GetTaskResponse;
import com.im.smarto.network.models.GroupAndTaskResponse;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskPresenter<V extends ITaskFragment> extends BasePresenter<V>
        implements TaskLoadListener{

    private static final String TAG = TaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private int mRemovedIndex;
    private TaskGroup mRemovedItem;

    @Inject
    public TaskPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    @Override
    public void onSuccess(List<TaskGroup> groupList) {
        Log.i(TAG, "onSuccess - " + mDataManager.taskManager().getData().toString());
        update();
        mView.updateGroupAmount(mDataManager.taskManager().mData.size());
        mView.hideProgressBar();
    }

    public void onCreateView() {
        mView.initRecyclerView();
        mView.showProgressBar();
        if (mDataManager.taskManager().getData().size() == 0) {
            Observable<List<GetGroupResponse>> groupResponse = mDataManager.networkHelper().getGroupList().toObservable();
            Observable<List<GetTaskResponse>> taskResponse = mDataManager.networkHelper().getTaskList().toObservable();

            Observable.zip(groupResponse, taskResponse,
                    (getGroupResponses, getTaskResponses) -> {
                        GroupAndTaskResponse groupAndTaskResponse = new GroupAndTaskResponse();
                        groupAndTaskResponse.setGroupListResponse(getGroupResponses);
                        groupAndTaskResponse.setTaskListResponse(getTaskResponses);
                        return groupAndTaskResponse;
                    }).subscribeOn(Schedulers.io())
                    .subscribe(groupAndTaskResponse -> {
                        Log.i(TAG, groupAndTaskResponse.getGroupListResponse().toString() + "\n"
                                + groupAndTaskResponse.getTaskListResponse().toString());
                        mDataManager.taskManager().onResponse(groupAndTaskResponse, this);
                    });
        } else {
            Log.i(TAG, "onCreateView() else - " + mDataManager.taskManager().getData().toString());
            update();
            mView.updateGroupAmount(mDataManager.taskManager().mData.size());
            mView.hideProgressBar();
        }
    }

    public void fabClicked() {
        mView.showAddGroupDialog();
    }

    public void onGroupItemClicked(int position) {
        mView.showGroupActivity(position);
    }

    public void onSwiped(int deletedIndex) {
        mRemovedIndex = deletedIndex;
        mRemovedItem = mDataManager.taskManager().mData.remove(deletedIndex);
        update();
        mView.updateGroupAmount(mDataManager.taskManager().mData.size());
        mView.showSnackBar();

        mDataManager.networkHelper().deleteGroup((int)mRemovedItem.getId())
                .subscribeOn(Schedulers.io())
                .subscribe(rows -> Log.i(TAG, rows.getRowsAffected() + "rows affected"),
                        error -> Log.i(TAG, error.getMessage()));


    }

    private void update() {
        mView.update(mDataManager.taskManager().mData);
    }

    public void onRestoreClicked() {

        int orderNum = mDataManager.taskManager().mData.size();

        mDataManager.networkHelper().insertGroup(mRemovedItem.getGroupName(), orderNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                            Log.i(TAG, "insertGroup success!");
                            mDataManager.taskManager().mData.add(new TaskGroup(success.getId(),
                                    mRemovedItem.getGroupName()));
                            mView.updateGroupAmount(mDataManager.taskManager().mData.size());
                            update();
                        },
                        error -> Log.i(TAG, error.getMessage()));

    }

    public void onDialogAddGroupClicked(String groupName) {

        int orderNum = mDataManager.taskManager().mData.size();

        mDataManager.networkHelper().insertGroup(groupName, orderNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                            Log.i(TAG, "insertGroup success!");
                            mDataManager.taskManager().mData.add(new TaskGroup(success.getId(), groupName));
                            update();
                        },
                        error -> Log.i(TAG, error.getMessage()));

    }
}
