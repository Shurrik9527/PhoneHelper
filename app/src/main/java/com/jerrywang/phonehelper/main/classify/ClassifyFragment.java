package com.jerrywang.phonehelper.main.classify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;

import java.util.ArrayList;
import java.util.List;


public class ClassifyFragment extends Fragment implements ClassifyContract.View {

    private ClassifyContract.Presenter presenter;
    private RecyclerView rvClassifyTitle;
    private RecyclerView rvClassifyContent;
    private Classify1Adapter classify1Adapter;
    private Classify2Adapter classifyContentAdapter;

    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();

    public ClassifyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClassifyFragment newInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
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
        View root = inflater.inflate(R.layout.classify_fragment, container, false);
        rvClassifyTitle = root.findViewById(R.id.rv_classify_title);
        rvClassifyTitle.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvClassifyTitle.setHasFixedSize(true);
        //创建并设置Adapter
        List<String> datas = new ArrayList<String>();
        datas.add("母婴");
        datas.add("保健品");
        datas.add("美护");
        datas.add("百货");
        datas.add("红酒");
        datas.add("UGG");
        datas.add("活动促销");
        classify1Adapter = new Classify1Adapter(getActivity(), datas);
        classify1Adapter.setOnItemClickListener(new Classify1Adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                classify1Adapter.setChecked(position);
            }
        });

        rvClassifyTitle.setAdapter(classify1Adapter);
        // 设置Item添加和移除的动画
        rvClassifyTitle.setItemAnimator(new DefaultItemAnimator());

        rvClassifyContent = root.findViewById(R.id.rv_classify_content);
        //创建瀑布流布局管理器

        rvClassifyContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvClassifyContent.setHasFixedSize(true);
        //创建并设置Adapter
        classifyContentAdapter = new Classify2Adapter(getActivity(), datas);
        rvClassifyContent.setAdapter(classifyContentAdapter);
        // 设置Item添加和移除的动画
        rvClassifyContent.setItemAnimator(new DefaultItemAnimator());

        return root;
    }

    @Override
    public void openClassfiyList() {
    }

    @Override
    public void setPresenter(ClassifyContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
