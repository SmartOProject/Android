package com.android.smarto.architecture.base;

import android.view.View;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public interface BasePresenter <V extends BaseView> {

    void onAttach(V view);
    void onDetach();

}
