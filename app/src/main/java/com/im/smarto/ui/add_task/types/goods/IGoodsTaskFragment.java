package com.im.smarto.ui.add_task.types.goods;

import com.im.smarto.ui.base.BaseView;

/**
 * Created by Anatoly Chernyshev on 21.02.2018.
 */

public interface IGoodsTaskFragment extends BaseView {

    void showEmptyTaskDescriptionError();
    void finishActivity();
    void showChooseGroupAlertDialog(String[] groupNames);
    void showAddGroupAlertDialog();
    void showGroupNamePreview(String groupName);
}
