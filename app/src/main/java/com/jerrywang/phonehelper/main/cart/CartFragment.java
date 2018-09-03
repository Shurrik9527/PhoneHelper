package com.jerrywang.phonehelper.main.cart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements CartContract.View {

    private CartContract.Presenter presenter;
    
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
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
        View root = inflater.inflate(R.layout.cart_fragment, container, false);
        rvCart = root.findViewById(R.id.rv_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvCart.setHasFixedSize(true);
        rvCart.addItemDecoration(new LinearItemDecoration(getActivity(), LinearItemDecoration.VERTICAL_LIST));
        List<String> datas = new ArrayList<String>();
        datas.add("母婴");
        datas.add("保健品");
        datas.add("美护");
        datas.add("百货");
        datas.add("红酒");
        datas.add("UGG");
        datas.add("活动促销");
        cartAdapter = new CartAdapter(datas);
        rvCart.setAdapter(cartAdapter);

        return root;
    }

    @Override
    public void setPresenter(CartContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
