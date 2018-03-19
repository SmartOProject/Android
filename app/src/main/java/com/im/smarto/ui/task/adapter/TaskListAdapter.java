package com.im.smarto.ui.task.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.im.smarto.Constants;
import com.im.smarto.R;
import com.im.smarto.ui.task.TaskPresenter;
import com.im.smarto.ui.task.model.SingleTask;
import com.im.smarto.ui.task.model.TaskGroup;
import com.im.smarto.utils.DrawableUtils;
import com.im.smarto.utils.ViewUtils;
import com.im.smarto.widget.ExpandableItemIndicator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableSwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.util.List;

import static com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemMoveAnimationManager.TAG;

/**
 * Created by Anatoly Chernyshev on 08.02.2018.
 */

public class TaskListAdapter extends AbstractExpandableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder>
        implements ExpandableDraggableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder>,
        ExpandableSwipeableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder> {

    private TaskPresenter mTaskPresenter;

    private final RecyclerViewExpandableItemManager mExpandableItemManager;
    private List<TaskGroup> mData;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;
    private Context mContext;

    private TaskGroup mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    private SingleTask mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private interface Draggable extends DraggableItemConstants {
    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    public TaskListAdapter(RecyclerViewExpandableItemManager expandableItemManager,
                           List<TaskGroup> data, TaskPresenter taskPresenter, Context context) {
        mExpandableItemManager = expandableItemManager;
        mTaskPresenter = taskPresenter;
        mData = data;
        mItemViewOnClickListener = v -> onItemViewClick(v);
        mSwipeableViewContainerOnClickListener = v -> onSwipeableViewContainerClick(v);
        this.mContext = context;

        // ExpandableItemAdapter, ExpandableDraggableItemAdapter and ExpandableSwipeableItemAdapter
        // require stable ID, and also have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    @Override
    public int onGetGroupItemSwipeReactionType(GroupViewHolder holder, int groupPosition, int x, int y) {
        if (onCheckGroupCanStartDrag(holder, groupPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public int onGetChildItemSwipeReactionType(ChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        if (onCheckChildCanStartDrag(holder, groupPosition, childPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public void onSwipeGroupItemStarted(GroupViewHolder holder, int groupPosition) {
        notifyDataSetChanged();
    }

    @Override
    public void onSwipeChildItemStarted(ChildViewHolder holder, int groupPosition, int childPosition) {
        notifyDataSetChanged();
    }

    @Override
    public void onSetGroupItemSwipeBackground(GroupViewHolder holder, int groupPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public void onSetChildItemSwipeBackground(ChildViewHolder holder, int groupPosition, int childPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public boolean onCheckGroupCanStartDrag(GroupViewHolder holder, int groupPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckChildCanStartDrag(ChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(GroupViewHolder holder, int groupPosition) {
        return null;
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(ChildViewHolder holder, int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        mTaskPresenter.onMoveGroupItem(fromGroupPosition, toGroupPosition);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        mTaskPresenter.onMoveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
    }

    @Override
    public boolean onCheckGroupCanDrop(int draggingGroupPosition, int dropGroupPosition) {
        return true;
    }

    @Override
    public boolean onCheckChildCanDrop(int draggingGroupPosition, int draggingChildPosition, int dropGroupPosition, int dropChildPosition) {
        return true;
    }

    @Override
    public void onGroupDragStarted(int groupPosition) {
        notifyDataSetChanged();
    }

    @Override
    public void onChildDragStarted(int groupPosition, int childPosition) {
        notifyDataSetChanged();
    }

    @Override
    public void onGroupDragFinished(int fromGroupPosition, int toGroupPosition, boolean result) {
        notifyDataSetChanged();
    }

    @Override
    public void onChildDragFinished(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition, boolean result) {
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).getSingleTaskList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mData.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getSingleTaskList().get(childPosition).getId();
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item_draggable, parent, false);
        v.setOnClickListener(mItemViewOnClickListener);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        v.setOnClickListener(mItemViewOnClickListener);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition, int viewType) {

        // group item
        final TaskGroup item = mData.get(groupPosition);

        // set listeners
        holder.itemView.setOnClickListener(mItemViewOnClickListener);

        // set text
        holder.mTextView.setText(item.getGroupName());

        holder.mNumbersOfTasks.setText(String.valueOf(item.getSingleTaskList().size()));
        holder.mExampleTasks.setText(item.getSingleTaskList().size() == 0 ? "" :
                item.getSingleTaskList().get(0).getTaskText());

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int expandState = holder.getExpandStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((expandState & Expandable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_group_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_group_item_dragging_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_group_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_group_item_swiping_state;
            } else if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
            }

            isExpanded = (expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0;
            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }

        // set swiping properties
        holder.setSwipeItemHorizontalSlideAmount(0);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // child item
        final SingleTask item = mData.get(groupPosition).getSingleTaskList().get(childPosition);

        int drawableIconRes = 0;
        switch(item.getTaskType()){
            case Constants.MEETING_TASK_TYPE:
                drawableIconRes = R.drawable.ic_accessibility_black_24dp;
                break;
            case Constants.COMMON_TASK_TYPE:
                drawableIconRes = R.drawable.ic_query_builder_black_24dp;
                break;
            case Constants.GOODS_TASK_TYPE:
                drawableIconRes = R.drawable.ic_kitchen_black_24dp;
                break;
        }

        // set listeners
        // (if the item is *pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);
        holder.mChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) item.setChecked(true);
                else item.setChecked(false);
        });

        holder.mChecked.setChecked(item.isChecked());
        holder.mTypeIcon.setImageResource(drawableIconRes);

        // set text
        holder.mTextView.setText(item.getTaskText());

        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
        holder.setSwipeItemHorizontalSlideAmount(0);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return !ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public SwipeResultAction onSwipeGroupItem(GroupViewHolder holder, int groupPosition, int result) {
        Log.d(TAG, "onSwipeGroupItem(groupPosition = " + groupPosition + ", result = " + result + ")");

        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
                    return new GroupSwipeRightResultAction(this, groupPosition);
            // other --- do nothing
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new GroupUnpinResultAction(this, groupPosition);
                } else {
                    return null;
                }
        }
    }

    @Override
    public SwipeResultAction onSwipeChildItem(ChildViewHolder holder, int groupPosition, int childPosition, int result) {
        Log.d(TAG, "onSwipeChildItem(groupPosition = " + groupPosition + ", childPosition = " + childPosition + ", result = " + result + ")");

        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
                    return new ChildSwipeRightResultAction(this, groupPosition, childPosition);
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
                } else {
                    return null;
                }
        }
    }

    public interface EventListener {
        void onGroupItemRemoved(int groupPosition);

        void onChildItemRemoved(int groupPosition, int childPosition);

        void onItemViewClicked(View v, boolean pinned);
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public static abstract class BaseViewHolder extends AbstractDraggableSwipeableItemViewHolder
            implements ExpandableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mTextView;
        private int mExpandStateFlags;
        public View mChildContainer;

        public BaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = v.findViewById(android.R.id.text1);
            mChildContainer = v.findViewById(R.id.main_child_container);
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            mExpandStateFlags = flag;
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public static class GroupViewHolder extends BaseViewHolder {
        public ExpandableItemIndicator mIndicator;
        public TextView mNumbersOfTasks;
        public TextView mExampleTasks;

        public GroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
            mNumbersOfTasks = v.findViewById(R.id.number_of_tasks_text);
            mExampleTasks = v.findViewById(R.id.example_of_tasks);
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {
        public ImageView mTypeIcon;
        public CheckBox mChecked;
        public ChildViewHolder(View v) {
            super(v);
            mTypeIcon = v.findViewById(R.id.task_type_icon);
            mChecked = v.findViewById(R.id.is_completed_task);
        }
    }

    public void update(List<TaskGroup> taskGroups){
        mData.clear();
        mData.addAll(taskGroups);
        this.notifyDataSetChanged();
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);  // true --- pinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
        }
    }

    private static class GroupSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private TaskListAdapter mAdapter;
        private final int mGroupPosition;

        GroupSwipeRightResultAction(TaskListAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mExpandableItemManager.notifyGroupItemRemoved(mGroupPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onGroupItemRemoved(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class GroupUnpinResultAction extends SwipeResultActionDefault {
        private TaskListAdapter mAdapter;
        private final int mGroupPosition;

        GroupUnpinResultAction(TaskListAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            TaskGroup item = mAdapter.mData.get(mGroupPosition);

        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private TaskListAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildSwipeRightResultAction(TaskListAdapter adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mExpandableItemManager.notifyChildItemRemoved(mGroupPosition, mChildPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onChildItemRemoved(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildUnpinResultAction extends SwipeResultActionDefault {
        private TaskListAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildUnpinResultAction(TaskListAdapter adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            SingleTask item = mAdapter.mData.get(mGroupPosition).getSingleTaskList().get(mChildPosition);

        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final TaskGroup item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final TaskGroup fromGroup = mData.get(fromGroupPosition);
        final TaskGroup toGroup = mData.get(toGroupPosition);

        final SingleTask item = fromGroup.getSingleTaskList().remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = toGroup.generateNewChildId();
            item.setId(newId);
        }

        toGroup.getSingleTaskList().add(toChildPosition, item);
    }

    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).getSingleTaskList().remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).getId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        TaskGroup group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.getSingleTaskList().size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.getSingleTaskList().size();
        }

        group.getSingleTaskList().add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

}
