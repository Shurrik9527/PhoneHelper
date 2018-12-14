package com.gochicken3.mobilehelper.aboutus;

import com.gochicken3.mobilehelper.BasePresenter;
import com.gochicken3.mobilehelper.BaseView;

public class AboutUsContract {

    public interface View extends BaseView<Presenter> {
        void initVersionName();
    }

    public interface Presenter extends BasePresenter {

    }
}
