package com.jerrywang.phonehelper.screenlocker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.util.ToastUtil;

import butterknife.ButterKnife;


public class ScreenLockerFragment extends Fragment implements ScreenLockerContract.View {

    private ScreenLockerContract.Presenter presenter;

    public ScreenLockerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ScreenLockerFragment newInstance() {
        ScreenLockerFragment fragment = new ScreenLockerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.screenlocker_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(ScreenLockerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

}
