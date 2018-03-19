package com.im.smarto.ui.base;

/**
 * Created by Anatoly Chernyshev on 01.02.2018.
 */

public class BasePresenter<V extends BaseView>{

    protected V mView;

    public void onAttach(V view){
        mView = view;
    }

    public void onDetach(){
        mView = null;
    }

}
