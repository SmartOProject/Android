package com.android.smarto.architecture.base;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public abstract class View implements BaseView {

    @Override
    public abstract void showProgress();

    @Override
    public abstract void hideProgress();
}
