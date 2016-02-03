package com.sf.hash.mycat.backend.statistics;

import cn.hotpu.hotdb.util.TimeUtil;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by 862911 on 2016/2/2.
 */
public class HeartbeatRecorder {
    //心跳配置默认常量
    private static final int MAX_RECORD_SIZE = 256;//记录保存最大数目
    private static final long AVG1_TIME = 60 * 1000L;//1分钟
    private static final long AVG2_TIME = 10 * 60 * 1000L;//10分钟
    private static final long AVG3_TIME = 30 * 60 * 1000L;//30分钟

    //记录三个时间段的平均响应时间
    private long avg1;
    private long avg2;
    private long avg3;
    private final List<Record> records;

    public HeartbeatRecorder() {
        this.records = new LinkedList<Record>();
    }

    private static class Record{
        private long value;
        private long time;

        public Record(long value, long time) {
            this.value = value;
            this.time = time;
        }
    }

    //获取当前三个时间段的平均响应时间
    public String get() {
        return new StringBuilder().append(avg1).append(',').append(avg2).append(',').append(avg3).toString();
    }
    //添加响应时间记录
    public void set(long value) {
        if (value < 0) {
            return;
        }
        long time = TimeUtil.currentTimeMillis();
        remove(time);
        int size = records.size();
        if (size == 0) {
            //第一次添加，设置初始值
            records.add(new Record(value, time));
            avg1 = avg2 = avg3 = value;
            return;
        }
        if (size >= MAX_RECORD_SIZE) {
            //如果大于等于，则证明队列中没有超时的记录。删除一个之后添加一个
            records.remove(0);
        }
        records.add(new Record(value, time));
        calculate(time);
    }

    /**
     * 删除超过统计时间段的数据
     */
    private void remove(long time) {
        final List<Record> records = this.records;
        while (records.size() > 0) {
            Record record = records.get(0);
            if (time >= record.time + AVG3_TIME) {
                records.remove(0);
            } else {
                break;
            }
        }
    }

    /**
     * 计算记录的统计数据
     */
    private void calculate(long time) {
        long v1 = 0L, v2 = 0L, v3 = 0L;
        int c1 = 0, c2 = 0, c3 = 0;
        for (Record record : records) {
            long t = time - record.time;
            if (t <= AVG1_TIME) {
                //一分钟以内响应时间
                v1 += record.value;
                ++c1;
            }
            if (t <= AVG2_TIME) {
                //十分钟以内响应时间
                v2 += record.value;
                ++c2;
            }
            if (t <= AVG3_TIME) {
                //三十分钟以内响应时间
                v3 += record.value;
                ++c3;
            }
        }
        avg1 = (v1 / c1);
        avg2 = (v2 / c2);
        avg3 = (v3 / c3);
    }
}
