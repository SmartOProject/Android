package com.android.smarto.architecture.task;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.data.IDataManager;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskPresenter<V extends ITaskFragment> extends BasePresenter<V> {

    private static final String TAG = TaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    public TaskPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onAddTaskClicked(){
        mView.openAddActivity();
    }

    public void onCreateView(){
        mView.setupRecyclerView(mDataManager.getTaskManager());
    }

    public void onChildItemClicked(int groupPosition, int childPosition){
        String groupName = mDataManager.getTaskManager().mData.get(groupPosition).getGroupName();
        SingleTask singleTask = mDataManager.getTaskManager().mData
                .get(groupPosition).getSingleTaskList().get(childPosition);
        mView.showDialog(groupName, singleTask);
    }

}
