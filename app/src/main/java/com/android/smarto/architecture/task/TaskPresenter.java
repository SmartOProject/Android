package com.android.smarto.architecture.task;

import android.util.Log;

import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.data.DataManager;
import com.android.smarto.data.IDataManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 07.02.2018.
 */

public class TaskPresenter<V extends ITaskFragment> extends BasePresenter<V> {

    private static final String TAG = TaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    @Inject
    TaskData mTaskData;

    @Inject
    public TaskPresenter(IDataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void onAddTaskClicked(){
        mView.openAddActivity();
    }

    public void onAddGroupClicked(){
        mView.openAddActivity();
    }

    public void updateListOnDb(){
        mDataManager.addTaskList(mTaskData.getList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( e -> Log.i(TAG, "List updated"));
    }

}
