package com.sharkwang8.phoneassistant.appmanager;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sharkwang8.phoneassistant.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppManagerFragment extends Fragment implements AppManagerContract.View {

    private AppManagerContract.Presenter presenter;

    @BindView(R.id.lv_apps)
    ListView lvApps;
    private List<ApplicationInfo> data;

    private AppManagerAdapter adapter;

    public AppManagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppManagerFragment newInstance() {
        AppManagerFragment fragment = new AppManagerFragment();
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
        View root = inflater.inflate(R.layout.appmanager_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(AppManagerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        data = new ArrayList<ApplicationInfo>();
        data.addAll(presenter.loadData(getActivity()));
        adapter = new AppManagerAdapter(getActivity(), data);
        lvApps.setAdapter(adapter);
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void refresh() {
        data.clear();
        data.addAll(presenter.loadData(getActivity()));
        adapter.notifyDataSetChanged();
    }

}
