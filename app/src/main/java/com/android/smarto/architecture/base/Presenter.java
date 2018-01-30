package com.android.smarto.architecture.base;

import android.view.View;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public abstract class Presenter <V extends BaseView> implements BasePresenter<V> {

    private V mView;

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    protected V getView(){
        return mView;
    }

    public abstract void onStart();

}
