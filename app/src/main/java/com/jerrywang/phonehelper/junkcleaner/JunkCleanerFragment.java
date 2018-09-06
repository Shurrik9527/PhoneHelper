package com.jerrywang.phonehelper.junkcleaner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.RadarScanView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class JunkCleanerFragment extends Fragment implements JunkCleanerContract.View {

    @BindView(R.id.junkclearner_rsv)
    RadarScanView junkclearnerRsv;
    Unbinder unbinder;
    private JunkCleanerContract.Presenter presenter;

    public JunkCleanerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JunkCleanerFragment newInstance() {
        JunkCleanerFragment fragment = new JunkCleanerFragment();
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
        View root = inflater.inflate(R.layout.junkclearner_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);

        initView();



        return root;
    }

    @Override
    public void setPresenter(JunkCleanerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void initView() {
        junkclearnerRsv.setCircleColor(getResources().getColor(R.color.radarscan_white));
        junkclearnerRsv.setLineColor(getResources().getColor(R.color.radarscan_white));
        junkclearnerRsv.start();
    }

    @Override
    public void showMessageTips(String msg) {

    }
}
