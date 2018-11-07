package com.jerrywang.phonehelper.trafficstatistics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.applock.AppLockAdapter;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;
import com.jerrywang.phonehelper.util.FormatUtil;
import com.jerrywang.phonehelper.widget.dialog.MyAlertSingleDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisticsFragment extends Fragment implements TrafficStatisticsContract.View {

    private static final String TAG = TrafficStatisticsFragment.class.getName();
    @BindView(R.id.trafficstatistics_total_tv)
    TextView trafficstatisticsTotalTv;
    @BindView(R.id.trafficstatistics_total_dw_tv)
    TextView trafficstatisticsTotalDwTv;
    @BindView(R.id.trafficstatistics_rv)
    RecyclerView trafficstatisticsRv;
    private TrafficStatisticsContract.Presenter presenter;
    private TrafficStatisticsAdapter mAdapter;
    private static final int REQUEST_CODE = 0X01;

    public TrafficStatisticsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrafficStatisticsFragment newInstance() {
        TrafficStatisticsFragment fragment = new TrafficStatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TrafficStatisticsPresenter(this, getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.trafficstatistics_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void showData(List<TrafficStatisticsBean> mlists) {
        if(mlists!=null){
            mAdapter.setNewData(mlists);
        }
    }

    @Override
    public void showAllTraffic(long traffic) {
        FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(traffic);
        if(trafficstatisticsTotalTv!=null&&mFileSize!=null){
            trafficstatisticsTotalTv.setText(mFileSize.mSize+"");
            trafficstatisticsTotalDwTv.setText(mFileSize.mUnit.name()+"");
        }
    }

    @Override
    public void showPermission() {
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.requestEach(Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if(permission.granted){//授权了
                    if(!hasPermissionToReadNetworkStats()){//去授权
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE);
                    }else{
                        if(presenter!=null){
                            presenter.getTotalSize();
                            presenter.getTrafficStatistics();
                        }
                    }
                }else{
                    MyAlertSingleDialog dialog = new MyAlertSingleDialog(getContext(), "NOTICTION", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new MyAlertSingleDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            getActivity().finish();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean hasPermissionToReadNetworkStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getActivity().getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }

    @Override
    public void setPresenter(TrafficStatisticsContract.Presenter presenter) {
        this.presenter =presenter;
    }

    @Override
    public void initView() {
        mAdapter = new TrafficStatisticsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trafficstatisticsRv.setLayoutManager(layoutManager);
        trafficstatisticsRv.setAdapter(mAdapter);
        showPermission();
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.trafficstatics_setting_menu_item:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(hasPermissionToReadNetworkStats()){
                if(presenter!=null){
                    presenter.getTotalSize();
                    presenter.getTrafficStatistics();
                }
            }else{
                MyAlertSingleDialog dialog = new MyAlertSingleDialog(getContext(), "NOTICTION", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new MyAlertSingleDialog.ConfirmListener() {
                    @Override
                    public void callback() {
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
