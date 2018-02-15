package com.android.smarto.architecture.adding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseActivity;
import com.android.smarto.architecture.task.model.TaskData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anatoly Chernyshev on 15.02.2018.
 */

public class AddTaskActivity extends BaseActivity {

    @BindView(R.id.edit_text_add_group_name) EditText mGroupName;
    @BindView(R.id.edit_text_add_group_prior) EditText mGroupPrior;
    @BindView(R.id.edit_text_add_task_text) EditText mTaskText;
    @BindView(R.id.edit_text_add_task_to) EditText mTaskToGroup;

    @Inject
    TaskData mTaskData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.button_add_group, R.id.button_add_task})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.button_add_group:

                String groupName = mGroupName.getText().toString();
                int groupPrior = Integer.parseInt(mGroupPrior.getText().toString());
                mTaskData.addGroup(groupName, groupPrior);
                finish();

                break;
            case R.id.button_add_task:

                String taskText = mTaskText.getText().toString();
                int groupId = Integer.parseInt(mTaskToGroup.getText().toString());
                mTaskData.addTaskToGroup(taskText, groupId);
                finish();

                break;
        }

    }

}
