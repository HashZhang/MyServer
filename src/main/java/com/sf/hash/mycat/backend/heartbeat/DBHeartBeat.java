package com.sf.hash.mycat.backend.heartbeat;

import com.sf.hash.mycat.backend.statistics.HeartbeatRecorder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象DB心跳检查类
 * Created by 862911 on 2016/2/2.
 */
public abstract class DBHeartBeat {
    //数据主从同步检查常量
    public static final int DB_SYN_ERROR = -1;
    public static final int DB_SYN_NORMAL = 1;
    //普通语句检查常量
    public static final int OK_STATUS = 1;
    public static final int ERROR_STATUS = -1;
    public static final int TIMEOUT_STATUS = -2;
    public static final int INIT_STATUS = 0;
    //心跳配置常量
    private static final long DEFAULT_HEARTBEAT_TIMEOUT = 30 * 1000L;
    private static final int DEFAULT_HEARTBEAT_RETRY = 10;

    //心跳检测时间
    protected long heartbeatTimeout = DEFAULT_HEARTBEAT_TIMEOUT; // 心跳超时时间
    protected int heartbeatRetry = DEFAULT_HEARTBEAT_RETRY; // 检查连接发生异常到切换，重试次数
    protected String heartbeatSQL;// 静态心跳语句
    protected final AtomicBoolean isStop = new AtomicBoolean(true);//是否停止心跳检测
    protected final AtomicBoolean isChecking = new AtomicBoolean(false);//心跳检测是否正在进行
    protected int errorCount;//错误计数
    protected volatile int status;//普通状态
    protected final HeartbeatRecorder recorder = new HeartbeatRecorder();

    //主从同步状态
    private volatile Integer slaveBehindMaster;//主从同步延迟时间
    private volatile int dbSynStatus = DB_SYN_NORMAL;//默认同步状态

    public long getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    public void setHeartbeatTimeout(long heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public int getHeartbeatRetry() {
        return heartbeatRetry;
    }

    public void setHeartbeatRetry(int heartbeatRetry) {
        this.heartbeatRetry = heartbeatRetry;
    }

    public String getHeartbeatSQL() {
        return heartbeatSQL;
    }

    public void setHeartbeatSQL(String heartbeatSQL) {
        this.heartbeatSQL = heartbeatSQL;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public HeartbeatRecorder getRecorder() {
        return recorder;
    }

    public Integer getSlaveBehindMaster() {
        return slaveBehindMaster;
    }

    public int getDbSynStatus() {
        return dbSynStatus;
    }

    public void setDbSynStatus(int dbSynStatus) {
        this.dbSynStatus = dbSynStatus;
    }

    public void setSlaveBehindMaster(Integer slaveBehindMaster) {
        this.slaveBehindMaster = slaveBehindMaster;
    }

    public int getStatus() {
        return status;
    }

    public boolean isChecking() {
        return isChecking.get();
    }

    public boolean isNeedHeartbeat() {
        return heartbeatSQL != null;
    }

    public boolean isStop() {
        return isStop.get();
    }

    //开始心跳检查
    public abstract void start();
    //结束心跳检查
    public abstract void stop();
    //得到上次活跃时间
    public abstract String getLastActiveTime();
    //得到超时时间
    public abstract long getTimeout();
    //心跳？？？
    public abstract void heartbeat();
}
