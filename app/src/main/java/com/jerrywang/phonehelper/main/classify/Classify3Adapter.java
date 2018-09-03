package com.jerrywang.phonehelper.main.classify;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class Classify3Adapter extends RecyclerView.Adapter<Classify3Adapter.ViewHolder> {

    private List<String> datas = null;
    private Classify3Adapter.OnItemClickListener onItemClickListener;

    public Classify3Adapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_recycle_item_lv3, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvClassify3.setText(datas.get(position));
        holder.cvClassify3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setOnItemClickListener(Classify3Adapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvClassify3;
        public CardView cvClassify3;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClassify3 = itemView.findViewById(R.id.tv_classify3);
            cvClassify3 = itemView.findViewById(R.id.cv_classify3);
        }
    }
}
