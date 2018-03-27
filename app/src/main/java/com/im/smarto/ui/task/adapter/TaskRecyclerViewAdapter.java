package com.im.smarto.ui.task.adapter;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.im.smarto.R;
import com.im.smarto.ui.task.TaskPresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 21.03.2018.
 */

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    private static final String TAG = TaskRecyclerViewAdapter.class.getSimpleName();

    private List<TaskGroup> mData;
    private TaskPresenter mTaskPresenter;

    public TaskRecyclerViewAdapter(List<TaskGroup> data, TaskPresenter presenter) {
        mData = data;
        mTaskPresenter = presenter;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        final TaskGroup item = mData.get(holder.getAdapterPosition());

        holder.mGroupName.setText(item.getGroupName());
        String taskAmount = "Amount of tasks: " + item.getSingleTaskList().size();
        holder.mTaskAmount.setText(taskAmount);

        if (item.getSingleTaskList().size() >= 3) {
            holder.mFirstPreview.setText(item.getSingleTaskList().get(0).getTaskText());
            holder.mSecondPreview.setText(item.getSingleTaskList().get(1).getTaskText());
            holder.mThirdPreview.setText(item.getSingleTaskList().get(2).getTaskText());
            holder.mFirstPreview.setVisibility(View.VISIBLE);
            holder.mSecondPreview.setVisibility(View.VISIBLE);
            holder.mThirdPreview.setVisibility(View.VISIBLE);
        } else if (item.getSingleTaskList().size() >= 2) {
            holder.mFirstPreview.setText(item.getSingleTaskList().get(0).getTaskText());
            holder.mSecondPreview.setText(item.getSingleTaskList().get(1).getTaskText());
            holder.mFirstPreview.setVisibility(View.VISIBLE);
            holder.mSecondPreview.setVisibility(View.VISIBLE);
        } else if (item.getSingleTaskList().size() >= 1) {
            holder.mFirstPreview.setText(item.getSingleTaskList().get(0).getTaskText());
            holder.mFirstPreview.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> mTaskPresenter.onGroupItemClicked(holder.getAdapterPosition()));

    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            // if empty, do full binding
            onBindViewHolder(holder, position);
            return;
        }

        Bundle bundle = (Bundle) payloads.get(0);

        String newTitle = bundle.getString("GROUP_NAME_CHANGE");
        if (newTitle != null) {
            // add some animation if you want
            holder.mGroupName.setText(newTitle);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_text_view) TextView mGroupName;
        @BindView(R.id.group_task_amount) TextView mTaskAmount;
        @BindView(R.id.first_preview) TextView mFirstPreview;
        @BindView(R.id.second_preview) TextView mSecondPreview;
        @BindView(R.id.third_preview) TextView mThirdPreview;

        public RelativeLayout mGroupBackground;
        public RelativeLayout mGroupForeground;

        public TaskViewHolder(View itemView) {
            super(itemView);

            mGroupBackground = itemView.findViewById(R.id.group_view_background);
            mGroupForeground = itemView.findViewById(R.id.group_view_foreground);

            ButterKnife.bind(this, itemView);
        }
    }

    public void update(List<TaskGroup> groupList){

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GroupDiffCallback(mData, groupList));
        diffResult.dispatchUpdatesTo(this);

        mData.clear();
        mData.addAll(groupList);
    }

}
