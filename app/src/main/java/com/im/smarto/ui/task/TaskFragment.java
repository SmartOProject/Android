package com.im.smarto.ui.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.helper.GroupItemTouchHelper;
import com.im.smarto.helper.TaskItemTouchHelper;
import com.im.smarto.helper.RecyclerItemTouchHelperListener;
import com.im.smarto.ui.add_task.AddTaskActivity;
import com.im.smarto.ui.base.BaseFragment;
import com.im.smarto.ui.group.GroupActivity;
import com.im.smarto.ui.group.adapter.TaskListAdapter;
import com.im.smarto.ui.task.adapter.TaskRecyclerViewAdapter;
import com.im.smarto.ui.task.model.TaskGroup;
import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

public class TaskFragment extends BaseFragment implements ITaskFragment, RecyclerItemTouchHelperListener {

    private static final String TAG = TaskFragment.class.getSimpleName();

    @BindView(R.id.task_progress) ProgressWheel mProgressWheel;
    @BindView(R.id.task_top_helper_text_view) TextView mGroupAmountTextView;
    @BindView(R.id.group_recycler_view) RecyclerView mTaskRecyclerView;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;

    @Inject
    TaskPresenter<ITaskFragment> mTaskPresenter;

    private List<TaskGroup> mData;
    private TaskRecyclerViewAdapter mAdapter;

    @OnClick(R.id.fab)
    void onClick(){
        mTaskPresenter.fabClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTaskPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskPresenter.onCreateView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTaskPresenter.onDetach();
    }

    @Override
    public void showProgressBar() {
        mProgressWheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressWheel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initRecyclerView() {
        mData = new ArrayList<>();
        mAdapter = new TaskRecyclerViewAdapter(getActivity(), mData, mTaskPresenter);
        mTaskRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                LinearLayoutManager.VERTICAL, false));
        mTaskRecyclerView.setAdapter(mAdapter);
        mTaskRecyclerView.setHasFixedSize(true);
        mTaskRecyclerView.setItemAnimator(new ScaleInBottomAnimator());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new GroupItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mTaskRecyclerView);
    }

    @Override
    public void update(List<TaskGroup> data) {
        mAdapter.update(data);
    }

    @Override
    public void showAddActivity() {
        startActivity(new Intent(getActivity(), AddTaskActivity.class));
    }

    @Override
    public void updateGroupAmount(int size) {
        String amount = "Amount: " + size;
        mGroupAmountTextView.setText(amount);
    }

    @Override
    public void showGroupActivity(int position) {
        Intent intent = new Intent(getContext(), GroupActivity.class);
        intent.putExtra(Constants.GROUP_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void showSnackBar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.task_root_layout), "Task was removed", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", v -> {
            mTaskPresenter.onRestoreClicked();
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void restoreItem(TaskGroup removedItem, int removedIndex) {
    }

    @Override
    public void removeItem(int deletedIndex) {

    }

    @Override
    public void showAddGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Create group");
        builder.setMessage("Enter the name of group that you want to create");
        builder.setIcon(R.drawable.ic_playlist_add_black_24dp);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_add_group_layout, null);
        MaterialEditText mAlertDialogEditText =
                view.findViewById(R.id.alert_dialog_add_group_edit_text);
        mAlertDialogEditText.setSingleLine();

        builder.setView(view);
        builder.setPositiveButton("Add", (dialog, which) -> {
                if (TextUtils.isEmpty(mAlertDialogEditText.getText()
                .toString())) return;
                mTaskPresenter.onDialogAddGroupClicked(mAlertDialogEditText.getText()
                        .toString());
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void addItem(int id, String groupName) {

    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TaskRecyclerViewAdapter.TaskViewHolder) {
            mTaskPresenter.onSwiped(viewHolder.getAdapterPosition());
        }
    }
}
