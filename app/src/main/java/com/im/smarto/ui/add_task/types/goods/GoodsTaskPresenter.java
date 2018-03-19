package com.im.smarto.ui.add_task.types.goods;

import android.text.TextUtils;
import android.util.Log;

import com.im.smarto.Constants;
import com.im.smarto.ui.base.BasePresenter;
import com.im.smarto.data.IDataManager;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public class GoodsTaskPresenter<V extends IGoodsTaskFragment> extends BasePresenter<V> {

    private static final String TAG = GoodsTaskPresenter.class.getSimpleName();

    private IDataManager mDataManager;

    private String mTargetGroup;

    @Inject
    public GoodsTaskPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    public void onCreate() {
    }

    public void onAddTaskClicked(String description) {
        if (TextUtils.isEmpty(description)) {
            mView.showEmptyTaskDescriptionError();
            return;
        }

        if (mTargetGroup == null) return;

        int groupId = mDataManager.taskManager().getGroupId(mTargetGroup);
        int groupPosition = mDataManager.taskManager().getGroupPosition(mTargetGroup);
        int type = Constants.GOODS_TASK_TYPE;
        int orderNum = mDataManager.taskManager().mData.get(groupPosition).getSingleTaskList().size();

        mDataManager.networkHelper().insertTask(groupId, type, description, orderNum)
                .subscribeOn(Schedulers.io())
                .subscribe(success -> {
                            mDataManager.taskManager()
                                    .mData.get(groupPosition)
                                    .getSingleTaskList()
                                    .add(new SingleTask(success.getId(), type, description));
                            Log.i(TAG, "insertTask success!");
                            mView.finishActivity();
                        },
                        error -> Log.i(TAG, error.getMessage()));
    }

    public void onAddGroupClicked() {
        mView.showAddGroupAlertDialog();
    }

    public void onChooseGroupClicked() {
        List<String> groupNames = mDataManager.taskManager().getGroupNames();
        mView.showChooseGroupAlertDialog(groupNames.toArray(new String[groupNames.size()]));
    }

    public void onSingleChoiceClicked(String groupName) {
        mTargetGroup = groupName;
        mView.showGroupNamePreview(groupName);
    }

    public void onDialogAddGroupClicked(String groupName) {
        mTargetGroup = groupName;
        mView.showGroupNamePreview(groupName);

        int orderNum = mDataManager.taskManager().mData.size();

        mDataManager.networkHelper().insertGroup(groupName, orderNum)
                .subscribeOn(Schedulers.io())
                .subscribe(success -> {
                            mDataManager.taskManager().mData.add(new TaskGroup(success.getId(), groupName));
                            Log.i(TAG, "insertGroup success!");
                        },
                        error -> Log.i(TAG, error.getMessage()));
    }
}
