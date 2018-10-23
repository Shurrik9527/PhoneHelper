package com.jerrywang.phonehelper.harassintercept;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.bean.CallLogBean;
import com.jerrywang.phonehelper.bean.SmsBean;
import com.jerrywang.phonehelper.harassintercept.detail.SmsDetailActivity;
import com.jerrywang.phonehelper.manager.HarassInterceptManager;
import com.jerrywang.phonehelper.manager.SMSManager;
import com.jerrywang.phonehelper.util.SpaceItemDecoration;
import com.jerrywang.phonehelper.util.StringUtil;
import com.jerrywang.phonehelper.util.ToastUtil;
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
public class SmsFragment extends Fragment implements HarassInterceptSMSContract.View, SwipeItemClickListener {

    @BindView(R.id.harassintercept_sms_rv)
    SwipeMenuRecyclerView harassinterceptSmsRv;
    private  SmsAdapter smsAdapter;
    private HarassInterceptSMSContract.Presenter mPresenter;
    public SmsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SmsFragment newInstance() {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sms_fragment, container, false);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HarassInterceptSMSPresenter(this,getActivity());
    }

    @Override
    public void initData() {
        initRecycleView();
        if(mPresenter!=null){
            mPresenter.getSystemSMSInform(getActivity());
        }
    }

    @Override
    public void initRecycleView() {
        // RecycleView 布局类型设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        harassinterceptSmsRv.setLayoutManager(layoutManager);
        //设置默认动画效果
        harassinterceptSmsRv.setItemAnimator(new DefaultItemAnimator());
        //上下拖拽开关 默认关闭
        harassinterceptSmsRv.setLongPressDragEnabled(true);
        //RecycleView item 的菜单创建
        harassinterceptSmsRv.setSwipeMenuCreator(swipeMenuCreator);
        //RecycleView item 监听
        harassinterceptSmsRv.setSwipeItemClickListener(this);
        //RecycleView item 的菜单监听
        harassinterceptSmsRv.setSwipeMenuItemClickListener(mMenuItemClickListener);
        harassinterceptSmsRv.addItemDecoration(new SpaceItemDecoration(20));
        smsAdapter = new SmsAdapter(getActivity());
        harassinterceptSmsRv.setAdapter(smsAdapter);

    }

    @Override
    public void showData(List<SmsBean> mlist) {
        if(smsAdapter!=null){
            smsAdapter.setNewData(mlist);
        }
    }

    @Override
    public void refreshData() {
        if(mPresenter!=null){
            mPresenter.getSystemSMSInform(getActivity());
        }
    }

    /**
     * RecycleView item中右侧 添加菜单
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.swipe_size);
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
            List<SmsBean> lists =new ArrayList<>();

            if(smsAdapter!=null){
                if(lists!=null&&lists.size()>0){
                    lists.clear();
                }
                lists.addAll(smsAdapter.getData());
            }
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {//右侧菜单
                //防止双点击
                if(StringUtil.isFastDoubleClick()){
                    return;
                }

                SmsBean mBean =lists.get(adapterPosition);
                if(menuPosition==0){//拉黑或恢复
                    if(!mBean.isBack()){//恢复
                        new HarassInterceptManager(getActivity()).blackSMS(mBean.getAddress());
                        if(mPresenter!=null){
                            mPresenter.getSystemSMSInform(getActivity());
                        }
                        showMessageTips(getString(R.string.harassintercept_phone_refriend_success));
                    }else{//拉黑
                        showMessageTips(getString(R.string.harassintercept_phone_refriend_fail));
                    }
                }else if(menuPosition==1){
                    if(mBean.isBack()){
                        new HarassInterceptManager(getActivity()).unBlackSMS(mBean.getAddress());
                        if(mPresenter!=null){
                            mPresenter.getSystemSMSInform(getActivity());
                        }
                        showMessageTips(getString(R.string.harassintercept_phone_unrefriend_success));
                    }else{
                        showMessageTips(getString(R.string.harassintercept_phone_unrefriend_fail));
                    }
                } else {//删除

                    lists.remove(adapterPosition);
                    List<SmsBean> list = new ArrayList<>();
                    list.add(mBean);
                    new HarassInterceptManager(getActivity()).deleteSMSInfo(list);
                    smsAdapter.setNewData(lists);
                    showMessageTips(getString(R.string.harassintercept_phone_delete_success));
                }
            }

        }
    };


    @Override
    public void setPresenter(HarassInterceptSMSContract.Presenter presenter) {
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
        if(smsAdapter!=null){
            List<SmsBean> mlist =smsAdapter.getData();
            SmsBean smsBean =mlist.get(position);
            Intent mIntent = new Intent(getActivity(), SmsDetailActivity.class);
            mIntent.putExtra("BUNDLE",smsBean);
            startActivity(mIntent);
        }
    }

}
