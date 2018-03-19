package com.im.smarto.ui.task;

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
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskPresenter<V extends ITaskFragment> extends BasePresenter<V>
        implements TaskLoadListener{

    private static final String TAG = TaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;
    private int mSelectedGroup;
    private int mSelectedTask;

    @Inject
    public TaskPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onAddTaskClicked(){
        mView.openAddActivity();
    }

    public void onCreateView(){
        mView.setupRecyclerView();
    }

    public void onChildItemClicked(int groupPosition, int childPosition){
        mSelectedGroup = groupPosition;
        mSelectedTask = childPosition;
        TaskGroup group = mDataManager.taskManager().mData.get(groupPosition);
        SingleTask singleTask = mDataManager.taskManager().mData
                .get(groupPosition).getSingleTaskList().get(childPosition);
        mView.showDialog(group, singleTask);
    }

    @Override
    public void onSuccess(List<TaskGroup> groupList) {
        Log.i(TAG, "onSuccess - " + mDataManager.taskManager().getData().toString());
        mView.updateList(mDataManager.taskManager().getData());
        mView.hideProgressBar();
    }

    public void onResume() {
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
            Log.i(TAG, "onResume() else - " + mDataManager.taskManager().getData().toString());
            mView.updateList(mDataManager.taskManager().getData());
            mView.hideProgressBar();
        }
    }

    public void onChildItemRemoved(int groupPosition, int childPosition) {
        final SingleTask item = mDataManager.taskManager()
                .mData.get(groupPosition).getSingleTaskList().get(childPosition);
        mDataManager.taskManager().mData.get(groupPosition).getSingleTaskList().remove(childPosition);
        mView.updateList(mDataManager.taskManager().getData());
        mDataManager.networkHelper().deleteTask((int)item.getId())
                .subscribeOn(Schedulers.io())
                .subscribe(rows -> Log.i(TAG, rows.getRowsAffected() + "rows affected"),
                        error -> Log.i(TAG, error.getMessage()));

    }

    public void onGroupItemRemoved(int groupPosition) {
        final TaskGroup item = mDataManager.taskManager().mData.get(groupPosition);

        mDataManager.taskManager().mData.remove(groupPosition);
        mView.updateList(mDataManager.taskManager().getData());
        mDataManager.networkHelper().deleteGroup((int)item.getId())
                .subscribeOn(Schedulers.io())
                .subscribe(rows -> Log.i(TAG, rows.getRowsAffected() + "rows affected"),
                        error -> Log.i(TAG, error.getMessage()));
    }

    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final TaskGroup from = mDataManager.taskManager().mData.remove(fromGroupPosition);
        final TaskGroup to = mDataManager.taskManager().mData.remove(toGroupPosition);
        mDataManager.taskManager().mData.add(toGroupPosition, from);
    }

    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final TaskGroup fromGroup = mDataManager.taskManager().mData.get(fromGroupPosition);
        final TaskGroup toGroup = mDataManager.taskManager().mData.get(toGroupPosition);

        final SingleTask item = fromGroup.getSingleTaskList().remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = toGroup.generateNewChildId();
            item.setId(newId);
        }

        toGroup.getSingleTaskList().add(toChildPosition, item);
    }

    public void onTaskChanged(int id, int groupId, String saveDescription, int type) {
        final SingleTask item = mDataManager.taskManager()
                .mData.get(mSelectedGroup).getSingleTaskList().get(mSelectedTask);
        if (type == Constants.DESCRIPTION) item.setTaskText(saveDescription);
        else item.setDate(saveDescription);

        mView.updateList(mDataManager.taskManager().getData());
        mDataManager.networkHelper().changeTask(id, saveDescription, type);
    }
}
