package com.im.smarto.ui.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Anatoly Chernyshev on 01.02.2018.
 */

public class BasePresenter<V extends BaseView>{

    protected V mView;
    protected CompositeDisposable mCompositeDisposable;

    public void onAttach(V view){
        mCompositeDisposable = new CompositeDisposable();
        mView = view;
    }

    public void onDetach(){
        mCompositeDisposable.dispose();
        mView = null;
    }

}
