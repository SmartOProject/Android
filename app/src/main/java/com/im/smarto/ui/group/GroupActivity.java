package com.im.smarto.ui.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.helper.TaskItemTouchHelper;
import com.im.smarto.helper.RecyclerItemTouchHelperListener;
import com.im.smarto.ui.add_task.AddTaskActivity;
import com.im.smarto.ui.base.BaseActivity;
import com.im.smarto.ui.group.adapter.TaskListAdapter;
import com.im.smarto.ui.task.model.SingleTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

public class GroupActivity extends BaseActivity implements IGroupActivity, RecyclerItemTouchHelperListener {

    @BindView(R.id.single_group_name) TextView mGroupName;
    @BindView(R.id.task_recycler_view) RecyclerView mTaskRecyclerView;

    @Inject
    GroupPresenter<IGroupActivity> mGroupPresenter;

    private TaskListAdapter mTaskListAdapter;
    private List<SingleTask> mData;

    private AlertDialog mOptionsDialog;

    @OnClick(R.id.fab_task)
    void onClick(){
        mGroupPresenter.onAddTaskClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        mGroupPresenter.onAttach(this);

        int groupPosition = getIntent().getIntExtra(Constants.GROUP_POSITION, 0);

        mGroupPresenter.onCreate(groupPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroupPresenter.onResume();
    }

    @Override
    public void setGroupName(String groupName) {
        mGroupName.setText(groupName);
    }

    @Override
    public void setupRecyclerView() {
        mData = new ArrayList<>();
        mTaskListAdapter = new TaskListAdapter(mData, mGroupPresenter);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mTaskRecyclerView.setAdapter(mTaskListAdapter);
        mTaskRecyclerView.setHasFixedSize(true);
        mTaskRecyclerView.setItemAnimator(new ScaleInBottomAnimator());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new TaskItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mTaskRecyclerView);
    }

    @Override
    public void updateList(List<SingleTask> singleTaskList) {
        mTaskListAdapter.update(singleTaskList);
    }

    @Override
    public void showSnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.group_root_layout), "Task was removed", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", v -> mGroupPresenter.onRestoreClicked());
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showAddTaskActivity(int position) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(Constants.GROUP_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void scrollToPosition(int position) {
        mTaskRecyclerView.scrollToPosition(position);
    }

    @Override
    public void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.task_options_dialog, null);
        builder.setView(v);
        TextView textView = v.findViewById(R.id.task_options_send_option);
        textView.setOnClickListener(v1 -> mGroupPresenter.onSendTaskToFriendClicked());

        mOptionsDialog = builder.create();
        mOptionsDialog.show();
    }

    @Override
    public void dismissOptionsDialog() {
        mOptionsDialog.dismiss();
    }

    @Override
    public void showContactListDialog(String[] contactNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Choose contact");
        builder.setIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setSingleChoiceItems(contactNames, -1, (dialog, which) -> {
            mGroupPresenter.onSingleContactChoiceClicked(contactNames[which]);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            mGroupPresenter.onPositiveButtonClicked();
            dialog.dismiss();
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TaskListAdapter.TaskListViewHolder) {
            mGroupPresenter.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

}
