package com.sharkwang8.phoneassistant.harassintercept;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.HardwarePropertiesManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharkwang8.phoneassistant.R;
import com.sharkwang8.phoneassistant.bean.CallLogBean;
import com.sharkwang8.phoneassistant.bean.SmsBean;
import com.sharkwang8.phoneassistant.harassintercept.detail.SmsDetailActivity;
import com.sharkwang8.phoneassistant.manager.HarassInterceptManager;
import com.sharkwang8.phoneassistant.util.SpaceItemDecoration;
import com.sharkwang8.phoneassistant.util.StringUtil;
import com.sharkwang8.phoneassistant.util.ToastUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class PhoneFragment extends Fragment implements HarassInterceptPhoneContract.View, SwipeItemClickListener {

    private static  final String TAG = PhoneFragment.class.getName();

    @BindView(R.id.harassintercept_phone_rv)
    SwipeMenuRecyclerView harassinterceptPhoneRv;

    private PhoneAdapter mPhoneAdapter;
    private HarassInterceptPhoneContract.Presenter mPresenter;
    private List<CallLogBean> mLists;
    public PhoneFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PhoneFragment newInstance() {
        PhoneFragment fragment = new PhoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.phone_fragment, container, false);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HarassInterceptPhonePresenter(this,getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void initData() {
        initRecycleView();
        if(mPresenter!=null){
            mPresenter.getSystemPhoneInfor(getActivity());
        }
    }


    @Override
    public void initRecycleView() {
        // RecycleView 布局类型设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        harassinterceptPhoneRv.setLayoutManager(layoutManager);
        //设置默认动画效果
        harassinterceptPhoneRv.setItemAnimator(new DefaultItemAnimator());
        //上下拖拽开关 默认关闭
        harassinterceptPhoneRv.setLongPressDragEnabled(true);
        //RecycleView item 的菜单创建
        harassinterceptPhoneRv.setSwipeMenuCreator(swipeMenuCreator);
        //RecycleView item 监听
        harassinterceptPhoneRv.setSwipeItemClickListener(this);
        //RecycleView item 的菜单监听
        harassinterceptPhoneRv.setSwipeMenuItemClickListener(mMenuItemClickListener);
        harassinterceptPhoneRv.addItemDecoration(new SpaceItemDecoration(20));



        mPhoneAdapter = new PhoneAdapter(getActivity());
        harassinterceptPhoneRv.setAdapter(mPhoneAdapter);

    }

    @Override
    public void showData(List<CallLogBean> lists) {

        if(mPhoneAdapter!=null){
            mLists =lists;
            mPhoneAdapter.setNewData(mPresenter.checkData(lists));
        }
    }

    @Override
    public void refreshData() {
        if(mPresenter!=null){
            mPresenter.getSystemPhoneInfor(getActivity());
        }
    }

    @Override
    public void callPhone(String phone) {
        if(TextUtils.isEmpty(phone)){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data); startActivity(intent);
    }

    /**
     * RecycleView item中右侧 添加菜单
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.swipe_size_80);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem folgItem = new SwipeMenuItem(getActivity())
                    .setBackground(R.color.red1)
                    .setText(R.string.harassintercept_phone_refriend_title)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(folgItem);// 添加菜单到右侧

            SwipeMenuItem resore = new SwipeMenuItem(getActivity())
                    .setBackground(R.color.colorPrimary)
                    .setText(getString(R.string.harassintercept_phone_unrefriend_title))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(resore);// 添加菜单到右侧

            SwipeMenuItem addItem = new SwipeMenuItem(getActivity())
                    .setBackground(R.color.green0)
                    .setText(getString(R.string.harassintercept_phone_delete_title))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addItem);// 添加菜单到右侧

        }
    };


    /**
     * RecyclerView的Item的Menu点击监听
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            List<CallLogBean> lists =new ArrayList<>();
            if(mPhoneAdapter!=null){
                if(lists!=null&&lists.size()>0){
                    lists.clear();
                }
                lists.addAll(mPhoneAdapter.getData());
            }

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {//右侧菜单
                //防止双点击
                if(StringUtil.isFastDoubleClick()){
                    return;
                }
                CallLogBean mBean =lists.get(adapterPosition);
                if(menuPosition==0){//拉黑或恢复
                    if(!mBean.isBack()){//恢复
                        new HarassInterceptManager(getActivity()).blackPhone(mBean.getPhoneNum());
                        mBean.setBack(true);
                        lists.set(adapterPosition,mBean);
                        mPhoneAdapter.setNewData(lists);
                        showMessageTips(getString(R.string.harassintercept_phone_refriend_success));
                    }else{//拉黑
                        showMessageTips(getString(R.string.harassintercept_phone_refriend_fail));
                    }
                }else if(menuPosition==1){
                    if(mBean.isBack()){
                        new HarassInterceptManager(getActivity()).unBlackPhone(mBean.getPhoneNum());
                        mBean.setBack(false);
                        lists.set(adapterPosition,mBean);
                        mPhoneAdapter.setNewData(lists);
                        showMessageTips(getString(R.string.harassintercept_phone_unrefriend_success));
                    }else{
                        showMessageTips(getString(R.string.harassintercept_phone_unrefriend_fail));
                    }
                } else{//删除
                    lists.remove(adapterPosition);
                    List<CallLogBean> list = new ArrayList<>();
                    list.add(mBean);
                    new HarassInterceptManager(getActivity()).deleteCallLogInfoTable(list);
                    mPhoneAdapter.setNewData(lists);
                    showMessageTips(getString(R.string.harassintercept_phone_delete_success));
                }
            }
        }
    };



    @Override
    public void setPresenter(HarassInterceptPhoneContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showMessageTips(String msg) {
        if(!TextUtils.isEmpty(msg)){
            ToastUtil.showToast(getActivity(),msg);
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if(mPhoneAdapter!=null){
            List<CallLogBean> mlist =mPhoneAdapter.getData();
            CallLogBean mCallLogBean =mlist.get(position);
            callPhone(mCallLogBean.getPhoneNum());
        }
    }


}
