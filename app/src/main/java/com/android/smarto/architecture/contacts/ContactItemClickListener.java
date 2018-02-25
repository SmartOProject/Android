package com.android.smarto.architecture.contacts;

import android.view.View;

/**
 * Created by Anatoly Chernyshev on 24.02.2018.
 */

public interface ContactItemClickListener {

    void onClick(View view, int position, String id);

}
