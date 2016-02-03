package com.sf.hash.mycat.backend.heartbeat;

import com.sf.hash.mycat.backend.heartbeat.DBHeartBeat;
import com.sf.hash.mycat.backend.heartbeat.detector.MySQLDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 862911 on 2016/2/2.
 */
public class MySQLHeartbeat extends DBHeartBeat {

//    private static final int MAX_RETRY_COUNT = 5;//最大重试尝试次数
//    public static final Logger LOGGER = LoggerFactory
//            .getLogger(MySQLHeartbeat.class);
//
//    private final MySQLDataSource source;
//
//    private final ReentrantLock lock;
//    private final int maxRetryCount;
//
//    private MySQLDetector detector;
//
//    public MySQLHeartbeat(MySQLDataSource source) {
//        this.source = source;
//        lock = new ReentrantLock(false);
//        this.maxRetryCount = MAX_RETRY_COUNT;
//        this.status = INIT_STATUS;
//        this.heartbeatSQL = source.getHostConfig().getHeartbeatSQL();
//    }
//
//    public MySQLDetector getDetector() {
//        return detector;
//    }
//
//    public MySQLDataSource getSource() {
//        return source;
//    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getLastActiveTime() {
        return null;
    }

    @Override
    public long getTimeout() {
        return 0;
    }

    @Override
    public void heartbeat() {

    }
}
