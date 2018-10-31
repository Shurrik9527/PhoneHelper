package com.jerrywang.phonehelper.trafficstatistics;

import com.jerrywang.phonehelper.BasePresenter;
import com.jerrywang.phonehelper.BaseView;
import com.jerrywang.phonehelper.bean.TrafficStatisticsBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 流浪监控接口
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisticsContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showData(List<TrafficStatisticsBean> mlists);
        void showAllTraffic(long traffic);
        void showPermission();
        boolean hasPermissionToReadNetworkStats();
    }

    public interface Presenter extends BasePresenter {
        void getTrafficStatistics();
        void getTotalSize();
    }
}
