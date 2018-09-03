package com.jerrywang.phonehelper.goods.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;

public class GoodsListFragment extends Fragment implements GoodsListContract.View {

    private GoodsListContract.Presenter presenter;

    public GoodsListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GoodsListFragment newInstance() {
        GoodsListFragment fragment = new GoodsListFragment();
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
        View root = inflater.inflate(R.layout.goods_list_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(GoodsListContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
