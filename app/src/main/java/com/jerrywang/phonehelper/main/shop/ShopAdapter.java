package com.jerrywang.phonehelper.main.shop;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/27.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private List<String> datas = null;
    private OnItemClickListener onItemClickListener;

    public ShopAdapter(List<String> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_recycle_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvGoodName.setText(datas.get(position));
        viewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
        if (position % 2 == 0) {
            viewHolder.ivGoodItem.setImageResource(R.mipmap.ic_goods_item);
        } else {
            viewHolder.ivGoodItem.setImageResource(R.mipmap.ic_goods_item1);
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cvItem;
        public TextView tvGoodName;
        public ImageView ivGoodItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvGoodName = itemView.findViewById(R.id.tv_good_name);
            cvItem = itemView.findViewById(R.id.cv_good_item);
            ivGoodItem = itemView.findViewById(R.id.iv_goods_item);
        }
    }

}