package com.android.smarto.architecture.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by Anatoly Chernyshev on 09.02.2018.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

    }



}
