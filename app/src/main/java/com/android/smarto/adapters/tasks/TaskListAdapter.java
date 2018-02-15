package com.android.smarto.adapters.tasks;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.smarto.R;
import com.android.smarto.architecture.task.model.SingleTask;
import com.android.smarto.architecture.task.model.TaskData;
import com.android.smarto.architecture.task.model.TaskGroup;
import com.android.smarto.utils.DrawableUtils;
import com.android.smarto.utils.ViewUtils;
import com.android.smarto.widget.ExpandableItemIndicator;
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
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.util.List;

import io.reactivex.Single;

import static com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemMoveAnimationManager.TAG;

/**
 * Created by Anatoly Chernyshev on 08.02.2018.
 */

public class TaskListAdapter extends AbstractExpandableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder>
        implements ExpandableDraggableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder>,
        ExpandableSwipeableItemAdapter<TaskListAdapter.GroupViewHolder, TaskListAdapter.ChildViewHolder> {

    private final RecyclerViewExpandableItemManager mExpandableItemManager;
    private TaskData mDataProvider;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private interface Draggable extends DraggableItemConstants {
    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    @Override
    public int onGetGroupItemSwipeReactionType(GroupViewHolder holder, int groupPosition, int x, int y) {
        if (onCheckGroupCanStartDrag(holder, groupPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public int onGetChildItemSwipeReactionType(ChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        if (onCheckChildCanStartDrag(holder, groupPosition, childPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
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
//            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
//                bgResId = R.drawable.bg_swipe_group_item_left;
//                break;
//            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
//                bgResId = R.drawable.bg_swipe_group_item_right;
//                break;
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
//            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
//                bgResId = R.drawable.bg_swipe_item_left;
//                break;
//            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
//                bgResId = R.drawable.bg_swipe_item_right;
//                break;
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
        mDataProvider.moveGroupItem(fromGroupPosition, toGroupPosition);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        mDataProvider.moveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
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
        return mDataProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mDataProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mDataProvider.getGroupItem(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mDataProvider.getChildItem(groupPosition, childPosition).getId();
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item_draggable, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition, int viewType) {
        // group item
        final TaskGroup item = mDataProvider.getGroupItem(groupPosition);

        // set listeners
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        holder.mContainer.setOnClickListener(mItemViewOnClickListener);

        // set text
        holder.mTextView.setText(item.getGroupName());
        holder.mNumbersOfTasks.setText("Number of tasks: " + mDataProvider.getChildCount(groupPosition));

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
        holder.setSwipeItemHorizontalSlideAmount(
                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // child item
        final SingleTask item = mDataProvider.getChildItem(groupPosition, childPosition);

        // set listeners
        // (if the item is *pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        holder.mContainer.setOnClickListener(mItemViewOnClickListener);

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
        holder.setSwipeItemHorizontalSlideAmount(
                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
        if (mDataProvider.getGroupItem(groupPosition).isPinned()) {
            // return false to raise View.OnClickListener#onClick() event
            return false;
        }

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
                if (mDataProvider.getGroupItem(groupPosition).isPinned()) {
                    // pinned --- back to default position
                    return new GroupUnpinResultAction(this, groupPosition);
                } else {
                    // not pinned --- remove
                    return new GroupSwipeRightResultAction(this, groupPosition);
                }
                // swipe left -- pin
//            case Swipeable.RESULT_SWIPED_LEFT:
//                return new GroupSwipeLeftResultAction(this, groupPosition);
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
                if (mDataProvider.getChildItem(groupPosition, childPosition).isPinned()) {
                    // pinned --- back to default position
                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
                } else {
                    // not pinned --- remove
                    return new ChildSwipeRightResultAction(this, groupPosition, childPosition);
                }
                // swipe left -- pin
//            case Swipeable.RESULT_SWIPED_LEFT:
//                return new ChildSwipeLeftResultAction(this, groupPosition, childPosition);
            // other --- do nothing
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

//        void onGroupItemPinned(int groupPosition);
//
//        void onChildItemPinned(int groupPosition, int childPosition);

        void onItemViewClicked(View v, boolean pinned);
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public static abstract class BaseViewHolder extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mTextView;
        private int mExpandStateFlags;

        public BaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = v.findViewById(android.R.id.text1);
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

        public GroupViewHolder(View v) {
            super(v);
            mIndicator = v.findViewById(R.id.indicator);
            mNumbersOfTasks = v.findViewById(R.id.number_of_tasks_text);
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {
        public ChildViewHolder(View v) {
            super(v);
        }
    }

    public TaskListAdapter(RecyclerViewExpandableItemManager expandableItemManager,
                           TaskData data) {
        mExpandableItemManager = expandableItemManager;
        mDataProvider = data;
        mItemViewOnClickListener = v -> onItemViewClick(v);


        // ExpandableItemAdapter, ExpandableDraggableItemAdapter and ExpandableSwipeableItemAdapter
        // require stable ID, and also have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);  // true --- pinned
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

            mAdapter.mDataProvider.removeGroupItem(mGroupPosition);
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

            TaskGroup item = mAdapter.mDataProvider.getGroupItem(mGroupPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.mExpandableItemManager.notifyGroupItemChanged(mGroupPosition);
            }
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

            mAdapter.mDataProvider.removeChildItem(mGroupPosition, mChildPosition);
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

            SingleTask item = mAdapter.mDataProvider.getChildItem(mGroupPosition, mChildPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.mExpandableItemManager.notifyChildItemChanged(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

}
