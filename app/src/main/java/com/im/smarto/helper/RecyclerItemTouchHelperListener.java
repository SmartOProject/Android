package com.im.smarto.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Anatoly Chernyshev on 23.03.2018.
 */

public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
