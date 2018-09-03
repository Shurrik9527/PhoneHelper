package com.jerrywang.phonehelper.main.classify;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;
import com.jerrywang.phonehelper.widget.XRecyclerView;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class Classify2Adapter extends RecyclerView.Adapter<Classify2Adapter.ViewHolder> {

    private Context context;
    private List<String> datas = null;

    public Classify2Adapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_recycle_item_lv2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int raw = position;
        holder.tvClassify2.setText("———— 二级分类" + raw + " ————");
        //创建并设置Adapter
        Classify3Adapter adapter = new Classify3Adapter(datas.subList(0, raw + 1));
        adapter.setOnItemClickListener(new Classify3Adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println(raw + "-" + position);
            }
        });
        holder.rvClassifyContent.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public XRecyclerView rvClassifyContent;
        public TextView tvClassify2;

        public ViewHolder(View itemView) {
            super(itemView);

            rvClassifyContent = itemView.findViewById(R.id.rv_classify_content);
            //创建瀑布流布局管理器
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rvClassifyContent.setLayoutManager(gridLayoutManager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            rvClassifyContent.setHasFixedSize(true);

            tvClassify2 = new TextView(context);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvClassify2.setLayoutParams(p);
            tvClassify2.setPadding(5, 40, 5, 40);
            tvClassify2.setTextSize(14);
            tvClassify2.setGravity(Gravity.CENTER);
            rvClassifyContent.addHeaderView(tvClassify2);

        }
    }
}
