package com.jerrywang.phonehelper.appmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerrywang.phonehelper.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppManagerAdapter extends BaseAdapter {
    private Context context;
    private List<ApplicationInfo> data;
    private LayoutInflater inflater;

    public AppManagerAdapter(Context context, List<ApplicationInfo> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.appmanager_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        final ApplicationInfo applicationInfo = data.get(position);
        holder.ivAppIcon.setImageDrawable(applicationInfo.loadIcon(context.getPackageManager()));
        holder.tvAppName.setText(applicationInfo.loadLabel(context.getPackageManager()));
        holder.tvAppPackageName.setText(applicationInfo.packageName);
        holder.bUnstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("package:" + applicationInfo.packageName);
                //创建Intent意图
                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                //执行卸载程序
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_appicon)
        ImageView ivAppIcon;
        @BindView(R.id.tv_appname)
        TextView tvAppName;
        @BindView(R.id.tv_apppackagename)
        TextView tvAppPackageName;
        @BindView(R.id.b_uninstall)
        Button bUnstall;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
