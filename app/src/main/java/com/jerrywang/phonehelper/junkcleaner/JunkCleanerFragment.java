package com.jerrywang.phonehelper.junkcleaner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.RadarScanView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 Fragment
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerFragment extends Fragment implements JunkCleanerContract.View {


    @BindView(R.id.junkclearner_rsv)
    RadarScanView junkclearnerRsv;
    @BindView(R.id.junkclearner_elv)
    ExpandableListView junkclearnerElv;
    @BindView(R.id.junkclearner_start_iv)
    ImageView junkclearnerStartIv;
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
    public void initView() {
        junkclearnerRsv.setCircleColor(getResources().getColor(R.color.radarscan_white));
        junkclearnerRsv.setLineColor(getResources().getColor(R.color.radarscan_white));
        junkclearnerRsv.start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(presenter!=null){
            presenter.unsubscribe();
        }
    }
}
