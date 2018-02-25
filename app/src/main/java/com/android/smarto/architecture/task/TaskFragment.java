package com.android.smarto.architecture.task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.smarto.Constants;
import com.android.smarto.R;
import com.android.smarto.adapters.tasks.TaskListAdapter;
import com.android.smarto.architecture.add_task.AddTaskActivity;
import com.android.smarto.architecture.base.BaseFragment;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.data.TaskManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.melnykov.fab.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskFragment extends BaseFragment implements ITaskFragment,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener,
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = TaskFragment.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    TimePickerDialog mTimePickerDialog;
    DatePickerDialog mDatePickerDialog;

    ImageView closeImageBtn;
    ImageView iconImage;
    TextView dialogGroupName;
    EditText descriptionText;
    TextView dataText;
    Button saveBtn;

    private Dialog mDialog;
    private SingleTask mSingleTask;

    private String date;
    private String time;

    @BindView(R.id.task_list_recycler_view)
    RecyclerView mTaskList;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Inject
    TaskPresenter<ITaskFragment> mTaskPresenter;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //do something
        }

        mTaskPresenter.onAttach(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTaskPresenter.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        mDialog = new Dialog(mContext);

        ButterKnife.bind(this, view);
        mFloatingActionButton.setOnClickListener(e -> mTaskPresenter.onAddTaskClicked());
        mTaskPresenter.onCreateView();
        mFloatingActionButton.attachToRecyclerView(mTaskList);

        return view;
    }

    private void setupCalendar() {
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(
                this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

        mTimePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        mTimePickerDialog.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTaskPresenter.onDetach();
    }

    @Override
    public void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openAddActivity() {
        startActivity(new Intent(getActivity(), AddTaskActivity.class));
    }

    @Override
    public void setupRecyclerView(TaskManager taskManager) {
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewExpandableItemManager =
                new RecyclerViewExpandableItemManager(null);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        final TaskListAdapter ourTaskListAdapter =
                new TaskListAdapter(mRecyclerViewExpandableItemManager, taskManager, mContext);
        ourTaskListAdapter.setEventListener(new TaskListAdapter.EventListener() {
            @Override
            public void onGroupItemRemoved(int groupPosition) {
                ((NavigationActivity) getActivity()).onGroupItemRemoved(groupPosition);
            }

            @Override
            public void onChildItemRemoved(int groupPosition, int childPosition) {
                ((NavigationActivity) getActivity()).onChildItemRemoved(groupPosition, childPosition);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                final int flatPosition = mTaskList.getChildAdapterPosition(v);

                if (flatPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                final long expandablePosition = mRecyclerViewExpandableItemManager.getExpandablePosition(flatPosition);
                final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
                final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

                if (childPosition != RecyclerView.NO_POSITION) {

                    mTaskPresenter.onChildItemClicked(groupPosition, childPosition);

                }

            }
        });

        mAdapter = ourTaskListAdapter;

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(ourTaskListAdapter);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter);

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        animator.setSupportsChangeAnimations(false);

        mTaskList.setLayoutManager(mLayoutManager);
        mTaskList.setAdapter(mWrappedAdapter);
        mTaskList.setItemAnimator(animator);
        mTaskList.setHasFixedSize(false);

        mTaskList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mTaskList);
        mRecyclerViewSwipeManager.attachRecyclerView(mTaskList);
        mRecyclerViewDragDropManager.attachRecyclerView(mTaskList);
        mRecyclerViewExpandableItemManager.attachRecyclerView(mTaskList);
    }

    @Override
    public void showDialog(String groupName, SingleTask task) {
        setupCalendar();

        mSingleTask = task;

        mDialog.setContentView(R.layout.show_task_dialog);
        closeImageBtn = mDialog.findViewById(R.id.dialog_close_btn);
        iconImage = mDialog.findViewById(R.id.dialog_image_icon);
        dialogGroupName = mDialog.findViewById(R.id.group_name);
        descriptionText = mDialog.findViewById(R.id.dialog_description_text);
        dataText = mDialog.findViewById(R.id.dialog_time_text_view);
        saveBtn = mDialog.findViewById(R.id.dialog_btn_save);
        saveBtn.setOnClickListener(e -> {
            String saveDescription = descriptionText.getText().toString();
            String saveDate = dataText.getText().toString();
            mSingleTask.setDate(saveDate);
            mSingleTask.setTaskText(saveDescription);
            mDialog.dismiss();
            updateList();
        });

        String groupDialogName = groupName;
        String description = mSingleTask.getTaskText();

        if (!TextUtils.isEmpty(mSingleTask.getDate())) {
            String data = mSingleTask.getDate();
            dataText.setText(data);
            dataText.setVisibility(View.VISIBLE);
            dataText.setOnClickListener(e -> mDatePickerDialog.show(getFragmentManager(), Constants.DATE_PICKER_DIALOG_TAG));
        }

        int res = 0;
        switch (mSingleTask.getTaskType()) {
            case Constants.MEETING_TASK_TYPE:
                res = R.drawable.ic_accessibility_black_24dp;
                break;
            case Constants.COMMON_TASK_TYPE:
                res = R.drawable.ic_query_builder_black_24dp;
                break;
            case Constants.GOODS_TASK_TYPE:
                res = R.drawable.ic_kitchen_black_24dp;
                break;
        }

        iconImage.setImageResource(res);
        dialogGroupName.setText(groupDialogName);
        descriptionText.setText(description);

        closeImageBtn.setOnClickListener(v1 -> mDialog.dismiss());

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {

    }

    public void notifyGroupItemRestored(int groupPosition) {
        mAdapter.notifyDataSetChanged();

        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mTaskList.scrollToPosition(flatPosition);
    }

    public void notifyChildItemRestored(int groupPosition, int childPosition) {
        mAdapter.notifyDataSetChanged();

        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mTaskList.scrollToPosition(flatPosition);
    }

    public void notifyGroupItemChanged(int groupPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mAdapter.notifyItemChanged(flatPosition);
    }

    public void notifyChildItemChanged(int groupPosition, int childPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mAdapter.notifyItemChanged(flatPosition);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mTaskList != null) {
            mTaskList.setItemAnimator(null);
            mTaskList.setAdapter(null);
            mTaskList = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        mTimePickerDialog.show(getFragmentManager(), Constants.TIME_PICKER_DIALOG_TAG);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        time = hourOfDay + ":" + minute;
        dataText.setText(date + " " + time);
    }
}
