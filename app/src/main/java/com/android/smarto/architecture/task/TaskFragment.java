package com.android.smarto.architecture.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.smarto.R;
import com.android.smarto.adapters.tasks.TaskListAdapter;
import com.android.smarto.app.App;
import com.android.smarto.architecture.adding.AddTaskActivity;
import com.android.smarto.architecture.base.BaseFragment;
import com.android.smarto.architecture.navigation.NavigationActivity;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.architecture.task.model.TaskGroup;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskFragment extends BaseFragment implements ITaskFragment,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener{

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    @BindView(R.id.task_list_recycler_view) RecyclerView mTaskList;
    @BindView(R.id.action_add_task) FloatingActionButton mFloatingActionButton;

    @Inject
    TaskPresenter<ITaskFragment> mTaskPresenter;

    @Inject
    TaskData mTaskData;

    public TaskFragment() {
        // Required empty public constructor
    }

    @OnClick({R.id.action_add_group, R.id.action_add_task})
    void onClickButton(View v){
        if (v.getId() == R.id.action_add_group) mTaskPresenter.onAddGroupClicked();
            else mTaskPresenter.onAddTaskClicked();
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
    public void onStop() {
        super.onStop();
        mTaskPresenter.updateListOnDb();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        ButterKnife.bind(this, view);
        setupRecyclerView();
        mFloatingActionButton.setOnClickListener(v -> {
            switch (v.getId()){
                case R.id.action_add_task:
                    mTaskPresenter.onAddTaskClicked();
                    break;
                case R.id.action_add_group:
                    mTaskPresenter.onAddGroupClicked();
                    break;
            }
        });

        return view;
    }

    private void setupRecyclerView() {

        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewExpandableItemManager =
                new RecyclerViewExpandableItemManager(null);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);


        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
//        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
//                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));

        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        final TaskListAdapter ourTaskListAdapter =
                new TaskListAdapter(mRecyclerViewExpandableItemManager, mTaskData);
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

            }
        });

        mAdapter = ourTaskListAdapter;

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(ourTaskListAdapter);       // wrap for expanding
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);           // wrap for dragging
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter); // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        animator.setSupportsChangeAnimations(false);

        mTaskList.setLayoutManager(mLayoutManager);
        mTaskList.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mTaskList.setItemAnimator(animator);
        mTaskList.setHasFixedSize(false);

        mTaskList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mTaskList);
        mRecyclerViewSwipeManager.attachRecyclerView(mTaskList);
        mRecyclerViewDragDropManager.attachRecyclerView(mTaskList);
        mRecyclerViewExpandableItemManager.attachRecyclerView(mTaskList);

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

}
