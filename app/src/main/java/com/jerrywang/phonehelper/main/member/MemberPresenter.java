package com.jerrywang.phonehelper.main.member;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class MemberPresenter implements MemberContract.Presenter {
    private MemberContract.View view;
    private CompositeDisposable mCompositeDisposable;

    public MemberPresenter(MemberContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

}
