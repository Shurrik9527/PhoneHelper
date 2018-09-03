package com.jerrywang.phonehelper.main.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jerrywang.phonehelper.GlideImageLoader;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.XRecyclerView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


public class ShopFragment extends Fragment implements ShopContract.View, SearchView.OnQueryTextListener {

    private ShopContract.Presenter presenter;

    private XRecyclerView rvShop;
    private SwipeRefreshLayout slrShop;
    private ShopAdapter shopRecyclerViewAdapter;
    private SearchView searchView;

    public ShopFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
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
        View root = inflater.inflate(R.layout.shop_fragment, container, false);

        rvShop = root.findViewById(R.id.rv_shop);
        //创建瀑布流布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvShop.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvShop.setHasFixedSize(true);
        //创建并设置Adapter
        shopRecyclerViewAdapter = new ShopAdapter(initData());
        shopRecyclerViewAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println(position);
            }
        });
        View headerView = inflater.inflate(R.layout.shop_include_header, container, false);
        ArrayList<String> imageUrls = new ArrayList<>();
        //添加数据
        imageUrls.add("http://www.esegou.com/statics/attachment/adv/2017/9/22/16//19019686.png");
        imageUrls.add("http://www.esegou.com/statics/attachment/adv/2017/9/22/16//19061308.png");
        Banner banner = headerView.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrls);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        RecyclerView rvShopClassify = headerView.findViewById(R.id.rv_shop_classify);
        rvShopClassify.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvShopClassify.setHasFixedSize(true);
        List<String> datas = new ArrayList<String>();
        datas.add("母婴");
        datas.add("保健品");
        datas.add("美护");
        datas.add("百货");
        datas.add("红酒");
        datas.add("UGG");
        datas.add("活动促销");
        datas.add("更多");
        ShopClassifyAdapter shopClassifyAdapter = new ShopClassifyAdapter(datas);
        shopClassifyAdapter.setOnItemClickListener(new ShopClassifyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println(position);
            }
        });
        rvShopClassify.setAdapter(shopClassifyAdapter);

        rvShop.addHeaderView(headerView);
        rvShop.setAdapter(shopRecyclerViewAdapter);
        // 设置Item添加和移除的动画
        rvShop.setItemAnimator(new DefaultItemAnimator());
        // 设置Item之间间隔样式
//        rvShop.addItemDecoration(new GridItemDecoration(getActivity()));

        //recyclerview滚动监听
       /*rvShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>= layoutManager.getItemCount()) {
                    presenter.getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });*/

        slrShop = root.findViewById(R.id.srl_shop);
        //调整SwipeRefreshLayout的位置
        slrShop.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        //swipeRefreshLayout刷新监听
        slrShop.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getData();
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    protected List<String> initData() {
        List<String> datas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            datas.add(String.valueOf((char) i));
        }
        return datas;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_shop, menu);
        //1.查找指定的MemuItem
        MenuItem menuItem = menu.findItem(R.id.action_search);
        View view = menuItem.getActionView();
        if (view != null) {
            searchView = (SearchView) view;
            //4.设置SearchView 的查询回调接口
            searchView.setOnQueryTextListener(this);
            searchView.setQueryHint("请输入搜索内容");

            //在搜索输入框没有显示的时候 点击Action ,回调这个接口，并且显示输入框
//            searchView.setOnSearchClickListener();
            //当自动补全的内容被选中的时候回调接口
//            searchView.setOnSuggestionListener();
            //可以设置搜索的自动补全，或者实现搜索历史
//            searchView.setSuggestionsAdapter();

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setPresenter(ShopContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void hideRefreshing() {
        //停止swipeRefreshLayout加载动画
        slrShop.setRefreshing(false);
    }

    /**
     * 当用户在输入法中点击搜索按钮时,或者输入回车时,调用这个方法，发起实际的搜索功能
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.search(query);
        searchView.clearFocus();
        return true;
    }

    /**
     * 每一次输入字符，都会调用这个方法，实现搜索的联想功能
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getActivity(), "" + newText, Toast.LENGTH_SHORT).show();
        return true;
    }
}
