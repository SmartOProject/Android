package com.im.smarto.ui.group.adapter;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.group.GroupPresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anatoly Chernyshev on 23.03.2018.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {

    private static final String TAG = TaskListAdapter.class.getSimpleName();

    private List<SingleTask> mData;
    private GroupPresenter mGroupPresenter;

    public TaskListAdapter(List<SingleTask> data, GroupPresenter groupPresenter) {
        mData = data;
        mGroupPresenter = groupPresenter;
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_recycler_view_item, parent, false);
        return new TaskListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {

        final SingleTask item = mData.get(position);

        holder.mTaskOptionsButton.setOnClickListener(v -> {
            mGroupPresenter.onTaskOptionsClicked(item);
        });

        holder.mTaskDescription.setText(item.getTaskText());

        if (item.getDate() != null) {
            Log.i(TAG, DateUtils.checkDate(item.getDate()));
            holder.mTaskTargetDate.setText(DateUtils.checkDate(item.getDate()));
            holder.mTaskTargetDate.setVisibility(View.VISIBLE);
        }

        int bgRes = 0;
        switch (item.getTaskType()) {
            case Constants.COMMON_TASK_TYPE:
                bgRes = R.drawable.ic_access_time_orange_24dp;
                break;
            case Constants.MEETING_TASK_TYPE:
                bgRes = R.drawable.ic_person_orange_24dp;
                break;
            case Constants.GOODS_TASK_TYPE:
                bgRes = R.drawable.ic_shopping_cart_orange_24dp;
                break;
        }
        holder.mTaskType.setImageResource(bgRes);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        Bundle bundle = (Bundle) payloads.get(0);

        String newDescription = bundle.getString("TASK_DESCRIPTION_CHANGE");
        if (newDescription != null) {
            holder.mTaskDescription.setText(newDescription);
        }

        String newDate = bundle.getString("TASK_DATE_CHANGE");
        if (newDate != null) {
            holder.mTaskTargetDate.setText(newDate);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<SingleTask> singleTaskList) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TaskDiffCallback(mData, singleTaskList));
        diffResult.dispatchUpdatesTo(this);

        mData.clear();
        mData.addAll(singleTaskList);
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.task_target_date) TextView mTaskTargetDate;
        @BindView(R.id.task_options_button) ImageView mTaskOptionsButton;
        @BindView(R.id.task_description) TextView mTaskDescription;
        @BindView(R.id.task_icon_type) ImageView mTaskType;
        public RelativeLayout mTaskBackground;
        public RelativeLayout mTaskForeground;

        public TaskListViewHolder(View itemView) {
            super(itemView);
            mTaskBackground = itemView.findViewById(R.id.task_view_background);
            mTaskForeground = itemView.findViewById(R.id.task_view_foreground);
            ButterKnife.bind(this, itemView);
        }
    }

}
