package com.jerrywang.phonehelper.main.classify;

import android.content.Context;
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

public class Classify1Adapter extends RecyclerView.Adapter<Classify1Adapter.ViewHolder> {
    private Context context;

    private List<String> datas = null;
    private Classify1Adapter.OnItemClickListener onItemClickListener;
    private int checked = 0;

    public Classify1Adapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_recycle_item_lv1, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(datas.get(position));
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
        if(checked == position) {
            holder.vDivider.setVisibility(View.VISIBLE);
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.vDivider.setVisibility(View.INVISIBLE);
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.text_default_color));
        }
    }

    public void setChecked(int checked) {
        this.checked = checked;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setOnItemClickListener(Classify1Adapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public View vDivider;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_classify1);
            vDivider = itemView.findViewById(R.id.v_divider);
        }
    }
}
