package com.jerrywang.phonehelper.cpucooler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;


public class CpuCoolerFragment extends Fragment implements CpuCoolerContract.View {

    private CpuCoolerContract.Presenter presenter;

    public CpuCoolerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CpuCoolerFragment newInstance() {
        CpuCoolerFragment fragment = new CpuCoolerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(CpuCoolerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showMessageTips(String msg) {

    }
}
