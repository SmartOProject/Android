package com.android.smarto.architecture.add_task.types.goods;

import android.text.TextUtils;

import com.android.smarto.Constants;
import com.android.smarto.architecture.base.BasePresenter;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.data.IDataManager;

import javax.inject.Inject;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public class GoodsTaskPresenter<V extends IGoodsTaskFragment> extends BasePresenter<V> {

    private IDataManager mDataManager;
    private int mSelectedGroupId;

    @Inject
    public GoodsTaskPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onAddGoodsTask(String goodsName){

        if (mDataManager.getTaskManager().getGroupArraySize() == 0) {
            mView.showNoGroupToAddTaskError();
            return;
        }

        if (TextUtils.isEmpty(goodsName)) {
            mView.showEmptyTaskDescriptionError();
            return;
        }

        int childId = mDataManager.getTaskManager().getChildArraySize(mSelectedGroupId);

        SingleTask singleTask = new SingleTask();
        singleTask.setId(childId);
        singleTask.setTaskText(goodsName);
        singleTask.setTaskType(Constants.GOODS_TASK_TYPE);

        mDataManager.getTaskManager().addTaskToGroup(singleTask, mSelectedGroupId);

    }

    public void onSpinnerItemClick(String groupName){
        mSelectedGroupId = mDataManager.getTaskManager().getGroupIdByName(groupName);
    }

    public void onSpinnerSetup(){
        if (mDataManager.getTaskManager().getGroupArraySize() != 0) {
            mView.updateSpinnerItems(mDataManager.getTaskManager().getGroupNames());
        }
    }

    public void onPositiveGroupDialogClick(String groupName){
        mDataManager.getTaskManager().addGroup(groupName);
        mView.updateSpinnerItems(mDataManager.getTaskManager().getGroupNames());
    }

}
