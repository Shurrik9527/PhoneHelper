package com.jerrywang.phonehelper.junkcleaner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.base.BaseViewHolder;
import com.jerrywang.phonehelper.bean.AppProcessInfornBean;
import com.jerrywang.phonehelper.bean.JunkCleanerGroupBean;
import com.jerrywang.phonehelper.bean.JunkCleanerInformBean;
import com.jerrywang.phonehelper.bean.JunkCleanerMultiItemBean;
import com.jerrywang.phonehelper.bean.JunkCleanerProcessInformBean;
import com.jerrywang.phonehelper.bean.JunkCleanerTypeBean;
import com.jerrywang.phonehelper.event.JunkCleanerTotalSizeEvent;
import com.jerrywang.phonehelper.event.JunkCleanerTypeClickEvent;
import com.jerrywang.phonehelper.util.DisplayUtil;
import com.jerrywang.phonehelper.util.FormatUtil;
import com.jerrywang.phonehelper.util.RxBus.RxBus;
import com.jerrywang.phonehelper.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 适配器
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerExpandAdapter extends BaseExpandableListAdapter{

    private List<JunkCleanerMultiItemBean> mData;
    private Context mContext;

    public JunkCleanerExpandAdapter(Context context, List<JunkCleanerMultiItemBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (((JunkCleanerTypeBean) (mData.get(groupPosition))).getSubItems() == null) {
            return 0;
        }
        return ((JunkCleanerTypeBean) (mData.get(groupPosition))).getSubItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((JunkCleanerTypeBean) (mData.get(groupPosition))).getSubItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.junkcleaner_item_layout, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 55)));
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }

        final JunkCleanerTypeBean junkCleanerTypeBean = (JunkCleanerTypeBean) mData.get(groupPosition);
        holder.setText(R.id.junkcleaner_item_title_tv, junkCleanerTypeBean.getTitle())
                .setText(R.id.junkcleaner_item_total_size_tv, junkCleanerTypeBean.getTotalSize())
                .setImageResource(R.id.junkcleaner_item_icon_iv, junkCleanerTypeBean.getIconResourceId())
                .setChecked(R.id.junkcleaner_item_cb, junkCleanerTypeBean.isCheck())
                .setVisibility(R.id.junkcleaner_item_pw, junkCleanerTypeBean.isProgressVisible())
                .setVisibility(R.id.junkcleaner_item_cb, !junkCleanerTypeBean.isProgressVisible());
        convertView.setFocusable(true);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new JunkCleanerTypeClickEvent(isExpanded,groupPosition));
            }
        });



        if(junkCleanerTypeBean.isCheck()){
            ((CheckBox)holder.getView(R.id.junkcleaner_item_cb)).setChecked(true);
        }else{
            ((CheckBox)holder.getView(R.id.junkcleaner_item_cb)).setChecked(false);
        }



        ((CheckBox)holder.getView(R.id.junkcleaner_item_cb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(junkCleanerTypeBean.isCheck()){
                    //保存未选清理状态
                    new SharedPreferencesHelper(mContext).put("SAVE_JUNK_CLEANER_ISALL",false);

                    junkCleanerTypeBean.setCheck(false);
                    List<JunkCleanerProcessInformBean> mlists =junkCleanerTypeBean.getSubItems();
                    if(mlists!=null&&mlists.size()>0){
                        for (int i = 0; i < junkCleanerTypeBean.getSubItems().size(); i++) {
                            JunkCleanerProcessInformBean mBean =junkCleanerTypeBean.getSubItem(i);
                            if(mBean!=null){
                                mBean.setCheck(false);
                            }
                        }
                    }
                }else{
                    junkCleanerTypeBean.setCheck(true);
                    List<JunkCleanerProcessInformBean> mlists =junkCleanerTypeBean.getSubItems();
                    if(mlists!=null&&mlists.size()>0){
                        for (int i = 0; i < junkCleanerTypeBean.getSubItems().size(); i++) {
                            JunkCleanerProcessInformBean mBean =junkCleanerTypeBean.getSubItem(i);
                            if(mBean!=null){
                                mBean.setCheck(true);
                            }
                        }
                    }
                }
                mData.set(groupPosition, junkCleanerTypeBean);
                computerJunSize();
                notifyDataSetChanged();
            }
        });
//        ((CheckBox)holder.getView(R.id.junkcleaner_item_cb)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                junkCleanerTypeBean.setCheck(isChecked);
//                if (junkCleanerTypeBean.getSubItems() != null) {
//                    if (junkCleanerTypeBean.isCheck()) {
//                        for (int i = 0; i < junkCleanerTypeBean.getSubItems().size(); i++) {
//                            junkCleanerTypeBean.getSubItem(i).setCheck(true);
//                        }
//                    } else {
//                        for (int i = 0; i < junkCleanerTypeBean.getSubItems().size(); i++) {
//                            junkCleanerTypeBean.getSubItem(i).setCheck(false);
//                        }
//                    }
//                }
//                mData.set(groupPosition, junkCleanerTypeBean);
//                computerJunSize();
//                notifyDataSetChanged();
//
//            }
//        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        BaseViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.junkcleaner_item_child_layout, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 60)));
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }

        final JunkCleanerProcessInformBean junkProcessInfo = ((JunkCleanerTypeBean) (mData.get(groupPosition))).getSubItem(childPosition);
        JunkCleanerInformBean junkInfo = junkProcessInfo.getJunkInfo();
        AppProcessInfornBean appProcessInfo = junkProcessInfo.getAppProcessInfo();
        if (junkInfo != null) {
            holder.setText(R.id.junkcleaner_item_child_title_tv, junkInfo.getmName())
                    .setText(R.id.junkcleaner_item_child_totail_size_tv, FormatUtil.formatFileSize(junkInfo.getmSize()).toString())
                    .setChecked(R.id.junkcleaner_item_child_cb, junkProcessInfo.isCheck())
                    .setVisibility(R.id.junkcleaner_item_child_cp, false);
            if (junkProcessInfo.getItemType()!= JunkCleanerTypeBean.CACHE) {
                holder.setImageDrawable(R.id.junkcleaner_item_child_icon_iv,junkInfo.getmDrawable());
            } else {
                holder.setImageResource(R.id.junkcleaner_item_child_icon_iv, R.mipmap.ic_launcher);
            }
        } else if (appProcessInfo != null) {
            holder.setText(R.id.junkcleaner_item_child_title_tv, appProcessInfo.getAppName())
                    .setText(R.id.junkcleaner_item_child_totail_size_tv, FormatUtil.formatFileSize(appProcessInfo.getMemory()).toString())
                    .setImageDrawable(R.id.junkcleaner_item_child_icon_iv, appProcessInfo.getIcon())
                    .setChecked(R.id.junkcleaner_item_child_cb, junkProcessInfo.isCheck())
                    .setVisibility(R.id.junkcleaner_item_child_cp, false);
        }


        ((CheckBox)holder.getView(R.id.junkcleaner_item_child_cb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(junkProcessInfo.isCheck()){
                    junkProcessInfo.setCheck(false);
                }else{
                    junkProcessInfo.setCheck(true);
                }
                computerJunSize();
            }
        });


//
//        ((CheckBox)holder.getView(R.id.junkcleaner_item_child_cb)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                junkProcessInfo.setCheck(isChecked);
//                computerJunSize();
//
//            }
//        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void computerJunSize() {

        long size = 0L;
        for (int i = 0; i < mData.size(); i++) {
            JunkCleanerTypeBean JunkCleanerTypeBean1 = (JunkCleanerTypeBean) mData.get(i);
            if (JunkCleanerTypeBean1.getSubItems() != null) {
                for (JunkCleanerProcessInformBean info : JunkCleanerTypeBean1.getSubItems()) {
                    if (info.isCheck()) {
                        if (info.getJunkInfo() != null) {
                            size += info.getJunkInfo().getmSize();
                        } else if (info.getAppProcessInfo() != null) {
                            size += info.getAppProcessInfo().getMemory();
                        }
                    }
                }
            }
        }
        RxBus.getDefault().post(new JunkCleanerTotalSizeEvent(FormatUtil.formatFileSize(size).toString()));
    }

    public void showItemDialog() {
        for (int i = 0; i < 5; i++) {
            ((JunkCleanerTypeBean) mData.get(i)).setProgressVisible(true);
        }
        notifyDataSetChanged();
    }

    public void dismissItemDialog(int index) {
        ((JunkCleanerTypeBean) mData.get(index)).setProgressVisible(false);
        notifyDataSetChanged();
    }

    public JunkCleanerTypeBean getItem(int index) {
        return (JunkCleanerTypeBean) (mData.get(index));
    }

    public void setItemTotalSize(int index, String size) {
        getItem(index).setTotalSize(size);
        notifyDataSetChanged();
    }

    public void setData(JunkCleanerGroupBean junkCleanerGroupBean) {
        for (int i = 0; i < 5; i++) {
            JunkCleanerTypeBean JunkCleanerTypeBean = (JunkCleanerTypeBean) mData.get(i);
            ArrayList<JunkCleanerProcessInformBean> infos = junkCleanerGroupBean.getJunkList(i);
            for (int j = 0; j < infos.size(); j++) {
                JunkCleanerTypeBean.addSubItem(infos.get(j));
            }
            mData.set(i, JunkCleanerTypeBean);
        }
        notifyDataSetChanged();
    }

    public List<JunkCleanerMultiItemBean> getData() {
        return mData;
    }
    
}
